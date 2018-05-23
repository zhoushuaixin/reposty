package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;


public interface CourierRepository extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier>{

	@Query(value="update Courier set deltag = '1' where id = ?1")
	@Modifying
	void updateDeltag(int id);

	List<Courier> findByFixedAreasIsNull();

	/**
	 * from Courier c inner join c.fixedAreas f where f.id = ?
	 * 表示返回查询结果，结果集中封装的是Object[]，数组的第一个值是Courier，数组的第二个值是FixedArea对象
	 * from Courier c inner join fetch c.fixedAreas f where f.id = ?
	 * fetch将返回的结果进行封装，封装成只返回from后面的对象
	 */
	@Query(value="from Courier c inner join fetch c.fixedAreas f where f.id = ?")
	List<Courier> findCourierByFixedAreaId(String fixedAreaId);

	@Query(value="from Courier c left join fetch c.fixedAreas f where f.id is null")
	List<Courier> queryByNoCourier();
	
}