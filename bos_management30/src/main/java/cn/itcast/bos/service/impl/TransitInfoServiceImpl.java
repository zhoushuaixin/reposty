package cn.itcast.bos.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.dao.WayBillRepository;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.TransitInfoService;

@Service
@Transactional
public class TransitInfoServiceImpl implements TransitInfoService {

	@Autowired
	private TransitInfoRepository transitInfoRepository;
	
	@Autowired
	private WayBillRepository wayBillRepository;
	
	@Autowired
	private WayBillIndexRepository wayBillIndexRepository;

	@Override
	public void startWaybillProcess(String wayBillIds) {
		// 1：使用运单id，查询运单的持久化对象
		if(StringUtils.isNotBlank(wayBillIds)){
			String[] arraysWayBillIds = wayBillIds.split(",");
			for(String wayBillId:arraysWayBillIds){
				WayBill wayBill = wayBillRepository.findOne(Integer.parseInt(wayBillId));
				// 表示是待发货的状态
				if(wayBill.getSignStatus()==1){
					// 2：保存流程信息
					TransitInfo transitInfo = new TransitInfo();
					/**
					 * 出入库中转、到达网点、开始配送、正常签收、异常
					 */
					transitInfo.setStatus("出入库中转");
					transitInfo.setWayBill(wayBill);
					transitInfoRepository.save(transitInfo);
					// 3：因为流程已经启动，更新运单的状态（方便寄件人查看）
					// 运单状态： 1 待发货、 2 派送中、3 已签收、4 异常
					wayBill.setSignStatus(2);
					// 4：因为数据库的运单中的状态发生变化，同步更新索引库
					wayBillIndexRepository.save(wayBill); // 存在id
				}
			}
		}
	}
	
	@Override
	public Page<TransitInfo> findPageQuery(Pageable pageable) {
		return transitInfoRepository.findAll(pageable);
	}
}
