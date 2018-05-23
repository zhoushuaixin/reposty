package cn.itcast.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.dao.FixedAreaRepository;
import cn.itcast.bos.dao.TakeTimeRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.FixedAreaService;

@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Autowired
	private CourierRepository courierRepository;
	
	@Autowired
	private TakeTimeRepository takeTimeRepository;

	@Override
	public void save(FixedArea fixedArea) {
		fixedAreaRepository.save(fixedArea);
	}
	
	@Override
	public Page<FixedArea> findPageQuery(Specification<FixedArea> spec, Pageable pageable) {
		return fixedAreaRepository.findAll(spec, pageable);
	}
	
	@Override
	public void associationCourierToFixedArea(String fixedAreaId, String courierId, String takeTimeId) {
		// 查询定区的持久化对象
		FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
		// 查询快递员的持久化对象
		Courier courier = courierRepository.findOne(Integer.parseInt(courierId));
		// 查询收派时间的持久化对象
		TakeTime takeTime = takeTimeRepository.findOne(Integer.parseInt(takeTimeId));
		// 建立关联关系
		fixedArea.getCouriers().add(courier); // 定区关联快递员 insert语句
		courier.setTakeTime(takeTime); // update语句
	}
}
