package cn.itcast.bos.service.impl;

import org.hibernate.id.SequenceIdentityGenerator.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.DeliveryInfoRepository;
import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.DeliveryInfoService;

@Service
@Transactional
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

	@Autowired
	private DeliveryInfoRepository deliveryInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Override
	public void save(DeliveryInfo deliveryInfo, String transitInfoId) {
		// 1：保存开始配送的信息
		deliveryInfoRepository.save(deliveryInfo);
		// 2：将开始配送的信息和流程表关联
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		transitInfo.setDeliveryInfo(deliveryInfo);
		// 3：更新流程表的状态
		transitInfo.setStatus("开始配送");
	}
}
