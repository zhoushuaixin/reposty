package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos.service.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {

	@Autowired
	private FixedAreaService fixedAreaService;
	
	// 定区保存
	@Action(value="fixedArea_save",results={@Result(name="success",type="redirect",location="pages/base/fixed_area.jsp")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
	
	// 分页查询定区列表
	@Action(value="fixedArea_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
		// 有条件的分页查询
		Specification<FixedArea> spec = new Specification<FixedArea>() {
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 用来封装返回的结果
				//Predicate predicate = null;
				// 3个参数，使用集合存放3个条件
				List<Predicate> list = new ArrayList<>();
				// 定区编码
				if(StringUtils.isNotBlank(model.getId())){
					Predicate p = cb.equal(root.get("id").as(String.class), model.getId()) ;// 参数一：字段名称；参数二：字段值，等同于id = ?
					list.add(p);
				}
				// 所属单位
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%") ;// 参数一：字段名称；参数二：字段值，等同于company like ?
					list.add(p);
				}
				if(list!=null && list.size()>0){
					Predicate p [] = new Predicate [list.size()]; // 初始化数组
					query.where(list.toArray(p));
				}
				query.orderBy(cb.desc(root.get("id").as(String.class))); // 降序排列 id字段
				return query.getRestriction();
			}
		};
		Page<FixedArea> pageData = fixedAreaService.findPageQuery(spec,pageable);
		// 将数据结果封装到total属性和rows属性
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	// 查询没有被关联的客户
	@Action(value="fixedArea_noassociationSelect",results={@Result(name="success",type="json")})
	public String noassociationSelect(){
		// 使用webservice调用CRM系统的数据
		Collection<? extends Customer> collection = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerService/findNoAssociationCustomers")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		pushObjectToValueStack(collection);
		return SUCCESS;
	}
	
	// 使用定区id，查询已经被关联到该定区的客户
	@Action(value="fixedArea_associationSelect",results={@Result(name="success",type="json")})
	public String associationSelect(){
		// 使用webservice调用CRM系统的数据
		Collection<? extends Customer> collection = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerService/findHasAssociationFixedAreaCustomers/"+model.getId())
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		pushObjectToValueStack(collection);
		return SUCCESS;
	}
	
	// 属性驱动
	// 客户id的数组
	private String [] customerIds;
	
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}

	// 保存，定区关联客户
	@Action(value="fixedArea_associationCustomersToFixedArea",results={@Result(name="success",type="redirect",location="pages/base/fixed_area.jsp")})
	public String associationCustomersToFixedArea(){
		// 获取定区id
		String fixedAreaId = model.getId();
		// 获取客户id，组织一个字符串，中间使用逗号分开
		// System.out.println(customerIds);
		String customerIdsStr = StringUtils.join(customerIds,",");
		
		// 调用webservice
		WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerService/associationCustomersToFixedArea?customerIdStr="+customerIdsStr+"&fixedAreaId="+fixedAreaId)
				.type(MediaType.APPLICATION_JSON) // 可省略
				.put(null);// 不指定对象传递参数
		return SUCCESS;
	}
	
	// 快递员id
	private String courierId;
	// 收派时间id
	private String takeTimeId;
	
	public void setCourierId(String courierId) {
		this.courierId = courierId;
	}

	public void setTakeTimeId(String takeTimeId) {
		this.takeTimeId = takeTimeId;
	}

	@Action(value="fixedArea_associationCourierToFixedArea",results={@Result(name="success",type="redirect",location="pages/base/fixed_area.jsp")})
	public String associationCourierToFixedArea(){
		// 定区id
		String fixedAreaId = model.getId();
		// 快递员id
		// 收派时间的id
		fixedAreaService.associationCourierToFixedArea(fixedAreaId,courierId,takeTimeId);
		return SUCCESS;
	}
}
