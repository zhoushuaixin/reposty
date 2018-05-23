package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.InOutStorageInfoRepository;
import cn.itcast.bos.dao.TransitInfoRepository;
import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.InOutStorageInfoService;

@Service
@Transactional
public class InOutStorageInfoServiceImpl implements InOutStorageInfoService {

	@Autowired
	private InOutStorageInfoRepository inOutStorageInfoRepository;
	
	@Autowired
	private TransitInfoRepository transitInfoRepository;

	@Override
	public void save(InOutStorageInfo inOutStorageInfo, String transitInfoId) {
		// 1：保存出入库的信息
		inOutStorageInfoRepository.save(inOutStorageInfo);
		// 2：建立出入库的信息和流程的关联关系
		TransitInfo transitInfo = transitInfoRepository.findOne(Integer.parseInt(transitInfoId));
		transitInfo.getInOutStorageInfos().add(inOutStorageInfo);
		// 入库、出库、到达网点
		// 3：如果出入库的操作为【到达网点】，此时更新流程表的状态为【到达网点】
		if(inOutStorageInfo.getOperation().equals("到达网点")){
			transitInfo.setStatus("到达网点");
			transitInfo.setOutletAddress(inOutStorageInfo.getAddress());// 更新到达网点的地址
		}
	}
}
