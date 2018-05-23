package cn.itcast.bos.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder.Operator;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.WayBillService;

@Service
@Transactional
public class WayBillServiceImpl implements WayBillService {

	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void save(WayBill wayBill) {
		// 如果流程已经启动，此时不允许修改运单的
		if(wayBill!=null && wayBill.getId()!=null){
			Integer id = wayBill.getId();
			WayBill persistWayBill = wayBillRepository.findOne(id);
			// 表示流程已经启动
			if(persistWayBill.getSignStatus()!=1){
				System.out.println("流程已经启动，不允许修改");
				throw new RuntimeException("流程已经启动，不允许修改，运单号是："+id);
			}
		}
		/*
		 * 运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
		 */
		wayBill.setSignStatus(1);
		wayBillRepository.save(wayBill); // 往数据库中存放数据
		// 往索引库同步数据
		wayBillIndexRepository.save(wayBill);
	}
	
	@Override
	public Page<WayBill> findPageQuery(WayBill wayBill,Pageable pageable) {
		// 当页面没有输入任何条件，查询所有，此时从数据库查询
		if(StringUtils.isBlank(wayBill.getWayBillNum()) && 
				StringUtils.isBlank(wayBill.getSendAddress()) && 
				StringUtils.isBlank(wayBill.getRecAddress()) && 
				StringUtils.isBlank(wayBill.getSendProNum()) && 
				(wayBill.getSignStatus()==null || wayBill.getSignStatus()==0)){
			return wayBillRepository.findAll(pageable);
		}
		// 当页面输入查询的条件，此时查询索引库
		else{
			// 搜索索引库，用到多个索引组合的时候
			BoolQueryBuilder query = new BoolQueryBuilder();
			// 运单号（词条查询）
			if(StringUtils.isNotBlank(wayBill.getWayBillNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 寄件人地址
			if(StringUtils.isNotBlank(wayBill.getSendAddress())){
				// 条件一
				QueryBuilder queryBuilder1 = new WildcardQueryBuilder("sendAddress", "*"+wayBill.getSendAddress()+"*");
				// 条件二：QueryStringQuery：将查询的条件先分词，再检索，然后将再合并
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress")
									.defaultOperator(Operator.AND); // and表示，按照交集的方式合并
				BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
				queryBuilder.should(queryBuilder1).should(queryBuilder2);
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 收件人地址
			if(StringUtils.isNotBlank(wayBill.getRecAddress())){
				// 条件一
				QueryBuilder queryBuilder1 = new WildcardQueryBuilder("recAddress", "*"+wayBill.getRecAddress()+"*");
				// 条件二：QueryStringQuery：将查询的条件先分词，再检索，然后将再合并
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress")
									.defaultOperator(Operator.AND); // and表示，按照交集的方式合并
				BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
				queryBuilder.should(queryBuilder1).should(queryBuilder2);
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 产品类型
			if(StringUtils.isNotBlank(wayBill.getSendProNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 运单状态
			if(wayBill.getSignStatus()!=null && wayBill.getSignStatus()!=0){
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			searchQuery.setPageable(pageable);
			return wayBillIndexRepository.search(searchQuery);
		}
	}
	
	@Override
	public WayBill findByWayBillNum(String wayBilklNum) {
		return wayBillRepository.findByWayBillNum(wayBilklNum);
	}
	
	// 运单数据，数据库的数据同步到索引库
	@Override
	public void syncIndex() {
		List<WayBill> list = wayBillRepository.findAll();
		wayBillIndexRepository.save(list);
	}
	
	@Override
	public List<WayBill> findWayBills(WayBill wayBill) {
		// 当页面没有输入任何条件，查询所有，此时从数据库查询
		if(StringUtils.isBlank(wayBill.getWayBillNum()) && 
				StringUtils.isBlank(wayBill.getSendAddress()) && 
				StringUtils.isBlank(wayBill.getRecAddress()) && 
				StringUtils.isBlank(wayBill.getSendProNum()) && 
				(wayBill.getSignStatus()==null || wayBill.getSignStatus()==0)){
			return wayBillRepository.findAll();
		}
		// 当页面输入查询的条件，此时查询索引库
		else{
			// 搜索索引库，用到多个索引组合的时候
			BoolQueryBuilder query = new BoolQueryBuilder();
			// 运单号（词条查询）
			if(StringUtils.isNotBlank(wayBill.getWayBillNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 寄件人地址
			if(StringUtils.isNotBlank(wayBill.getSendAddress())){
				// 条件一
				QueryBuilder queryBuilder1 = new WildcardQueryBuilder("sendAddress", "*"+wayBill.getSendAddress()+"*");
				// 条件二：QueryStringQuery：将查询的条件先分词，再检索，然后将再合并
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress")
									.defaultOperator(Operator.AND); // and表示，按照交集的方式合并
				BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
				queryBuilder.should(queryBuilder1).should(queryBuilder2);
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 收件人地址
			if(StringUtils.isNotBlank(wayBill.getRecAddress())){
				// 条件一
				QueryBuilder queryBuilder1 = new WildcardQueryBuilder("recAddress", "*"+wayBill.getRecAddress()+"*");
				// 条件二：QueryStringQuery：将查询的条件先分词，再检索，然后将再合并
				QueryBuilder queryBuilder2 = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress")
									.defaultOperator(Operator.AND); // and表示，按照交集的方式合并
				BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
				queryBuilder.should(queryBuilder1).should(queryBuilder2);
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 产品类型
			if(StringUtils.isNotBlank(wayBill.getSendProNum())){
				QueryBuilder queryBuilder = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			// 运单状态
			if(wayBill.getSignStatus()!=null && wayBill.getSignStatus()!=0){
				QueryBuilder queryBuilder = new TermQueryBuilder("signStatus", wayBill.getSignStatus());
				query.must(queryBuilder);// 相当于sql语句中and
			}
			SearchQuery searchQuery = new NativeSearchQuery(query);
			return wayBillIndexRepository.search(searchQuery).getContent();
		}
	}
	
	@Override
	public List<Object[]> findCharts() {
		return wayBillRepository.findCharts();
	}
}
