package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;


public interface StandardRepository extends JpaRepository<Standard, Integer>{

	List<Standard> findByName(String name);

	List<Standard> findByNameLike(String name);
	
	// 默认执行的是HQL语句
	// nativeQuery=false 配置JPQL（类似于HQL）也是默认值， nativeQuery=true 配置SQL 、 
	@Query(value="from Standard where name like ?",nativeQuery=false)
	List<Standard> queryName(String name);
	
	@Query
	List<Standard> queryName2(String name);

	@Query(value="update Standard set minLength = ?2 where id = ?1")
	@Modifying // @query表示可以执行update方法
	void updateMinLengthById(int id, int minLength);
}