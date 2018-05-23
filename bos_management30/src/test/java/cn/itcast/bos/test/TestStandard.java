package cn.itcast.bos.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.StandardRepository;
import cn.itcast.bos.domain.base.Standard;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestStandard {
	
	@Autowired
	StandardRepository standardRepository;

	@Test
	public void findByName(){
		String name = "10-20公斤";
		List<Standard> list = standardRepository.findByName(name);
		for(Standard standard:list){
			System.out.println(standard);
		}
	}
	
	@Test
	public void findByNameLike(){
		String name = "%20%";
		List<Standard> list = standardRepository.findByNameLike(name);
		for(Standard standard:list){
			System.out.println(standard);
		}
	}
	
	@Test
	public void queryName(){
		String name = "%20%";
		List<Standard> list = standardRepository.queryName(name);
		for(Standard standard:list){
			System.out.println(standard);
		}
	}
	
	@Test
	public void queryName2(){
		String name = "%20%";
		List<Standard> list = standardRepository.queryName2(name);
		for(Standard standard:list){
			System.out.println(standard);
		}
	}
	
	// save的方法不仅有保存的功能，如果存在id，修改的功能
	@Test
	public void update1(){
		Standard standard = new Standard();
		standard.setId(4);
		standard.setName("30-45公斤");
		standardRepository.save(standard);
	}
	
	// 快照更新
	@Test
	@Transactional // 事务（默认测试JPA的时候，事务默认是回滚）
	@Rollback(false) // 不回滚（提交）
	public void update2(){
		Standard standard = standardRepository.findOne(4);
		standard.setName("30-40公斤");
	}
	
	// 自定义update语句
	@Test
	@Transactional // 事务（默认测试JPA的时候，事务默认是回滚）
	@Rollback(false) // 不回滚（提交）
	public void update3(){
		 standardRepository.updateMinLengthById(4,35);
	}
}
