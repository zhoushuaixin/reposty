package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.TakeTime;

public interface TakeTimeRepository extends JpaRepository<TakeTime,Integer>,JpaSpecificationExecutor<TakeTime> {
	@Query(value="update TakeTime set status = '1' where id = ?1")
	@Modifying
	void updateDeltag(int parseInt);
	
	@Query(value="from TakeTime t where t.status !='1'")
	List<TakeTime> findCompany();
}
