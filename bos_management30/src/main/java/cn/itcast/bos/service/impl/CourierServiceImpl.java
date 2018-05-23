package cn.itcast.bos.service.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {

	@Autowired
	CourierRepository courierRepository;
	

	@Override
	@RequiresPermissions(value="courier:add")
	public void save(Courier courier) {
		Subject subject = SecurityUtils.getSubject();
		if(subject.isPermitted("courier:add")){
			courierRepository.save(courier);
		}
		else{
			System.out.println("对不起权限不足");
		}
	}
	
	@Override
	public Page<Courier> findPageQuery(Pageable pageable) {
		return courierRepository.findAll(pageable);
	}
	
	// 有条件
	@Override
	public Page<Courier> findPageQuery(Specification<Courier> spec,Pageable pageable) {
		return courierRepository.findAll(spec, pageable);
	}
	
	@Override
	public void deleteByids(String[] arrayIds) {
		for(String id:arrayIds){
			// 更新deltag字段从null变成1
			courierRepository.updateDeltag(Integer.parseInt(id));
		}
	}
	
	@Override
	public List<Courier> findnoassociation() {
		// 方案一：使用spring data jpa的规范
		// List<Courier> list = courierRepository.findByFixedAreasIsNull();
		// 方案二 specification
//		Specification<Courier> spec = new Specification<Courier>() {
//			
//			@Override
//			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//				Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
//				return p;
//			}
//		};
//		List<Courier> list = courierRepository.findAll(spec);
		// 方案三
		List<Courier> list = courierRepository.queryByNoCourier();
		return list;
	}
	
	@Override
	public List<Courier> findCourierByFixedAreaId(String fixedAreaId) {
		// 使用HQL语句，完成多表关联
		List<Courier> list = courierRepository.findCourierByFixedAreaId(fixedAreaId);
		return list;
	}
}
