package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.StandardService;

@Service
@Transactional
public class StandardServiceImpl implements StandardService {

	@Autowired
	StandardRepository standardRepository;
	
	@Override
	@CacheEvict(value="standard",allEntries=true) // 清空standard缓存;allEntries=true：清空缓存，allEntries=false：不清空缓存
	public void save(Standard standard) {
		standardRepository.save(standard);
	}
	
	@Override
	public List<Standard> findByName(String name) {
		// spring data jpa完成条件查询（规范：按照Name字段查询，将接口的方法定义：find + By + 字段的名称（首字母大写）
		// 特点：list不能为空，即便没有数据，list的长度为0
		List<Standard> list = standardRepository.findByName(name);
		return list;
	}
	
	/**
	 * 缓存的原理：key value对，没有key，默认生成一个key
	 * key：缓存起个名称
	 *     第一页：0_3
	 *     第二页：1_3
	 */
	@Override
	@Cacheable(cacheNames="standard",key="#pageable.pageNumber+'_'+#pageable.pageSize")
	public Page<Standard> findPageQuery(Pageable pageable) {
		return standardRepository.findAll(pageable);
	}
	
	@Override
	public void deleteByids(String[] arrayIds) {
		for(String id:arrayIds){
			standardRepository.delete(Integer.parseInt(id)); // 使用主键ID，完成删除
		}
	}
	
	@Override
	@Cacheable(value="standard") // 应用standard缓存策略
	public List<Standard> findAll() {
		return standardRepository.findAll();
	}
	
}
