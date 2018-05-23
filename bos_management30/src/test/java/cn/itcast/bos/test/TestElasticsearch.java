package cn.itcast.bos.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.WayBillService;

@RunWith(value=SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class TestElasticsearch {
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	WayBillService wayBillService;

	@Test
	public void createIndexAndMapping(){
		// 创建索引
		elasticsearchTemplate.createIndex(WayBill.class);
		elasticsearchTemplate.putMapping(WayBill.class);
	}
	
	// 同步更新索引库
	@Test
	public void saveIndex(){
		// 创建索引
		wayBillService.syncIndex();
	}
	
}
