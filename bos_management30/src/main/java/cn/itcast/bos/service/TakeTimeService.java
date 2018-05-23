package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeService {
	void save(TakeTime takeTime);

	void deleteByids(String[] arrayIds);

	Page<TakeTime> findPageQuery(Specification<TakeTime> spec, Pageable pageable);

	List<TakeTime> findCompany();
}
