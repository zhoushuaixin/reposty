package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findPageQuery(Pageable pageable);

	Page<Courier> findPageQuery(Specification<Courier> spec,Pageable pageable);

	void deleteByids(String[] arrayIds);

	List<Courier> findnoassociation();

	List<Courier> findCourierByFixedAreaId(String fixedAreaId);
}
