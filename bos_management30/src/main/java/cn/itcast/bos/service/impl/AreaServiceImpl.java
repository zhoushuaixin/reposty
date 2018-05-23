package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.AreaService;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaRepository areaRepository;

	@Override
	public void saveAreas(List<Area> list) {
		areaRepository.save(list);
	}
	
	@Override
	public Page<Area> findPageQuery(Specification<Area> spec, Pageable pageable) {
		return areaRepository.findAll(spec, pageable);
	}
}
