package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {

	void save(FixedArea fixedArea);

	Page<FixedArea> findPageQuery(Specification<FixedArea> spec, Pageable pageable);

	void associationCourierToFixedArea(String fixedAreaId, String courierId, String takeTimeId);


}
