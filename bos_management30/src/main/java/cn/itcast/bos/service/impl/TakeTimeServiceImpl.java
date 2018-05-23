package cn.itcast.bos.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.dao.TakeTimeRepository;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.TakeTimeService;
import oracle.net.aso.a;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

	@Autowired
	private TakeTimeRepository takeTimeRepository;
	@Override
	//@CacheEvict(value="takeTime",allEntries=true) // 清空standard缓存;allEntries=true：清空缓存，allEntries=false：不清空缓存
	public void save(TakeTime takeTime) {
		// TODO Auto-generated method stub
		takeTime.setOperatingTime(new Date());
		takeTime.setStatus("0");
		takeTimeRepository.save(takeTime);
		// 下单时间
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	@Override
	public void deleteByids(String[] arrayIds) {
		// TODO Auto-generated method stub
		for(String id:arrayIds){
			// 更新deltag字段从null变成1
			takeTimeRepository.updateDeltag(Integer.parseInt(id));
		}
	}

	@Override
	public Page<TakeTime> findPageQuery(Specification<TakeTime> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return takeTimeRepository.findAll(spec, pageable);
	}

	@Override
	public List<TakeTime> findCompany() {
		// TODO Auto-generated method stub
		return takeTimeRepository.findCompany();
	}

}
