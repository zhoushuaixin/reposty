package cn.itcast.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill,Integer>,JpaSpecificationExecutor<WayBill> {

	WayBill findByWayBillNum(String wayBilklNum);

	@Query(value="select count(t.signStatus),case when t.signStatus = 1 then '待发货' when t.signStatus = 2 then '派送中' when t.signStatus = 3 then '正常签收' else '异常' end from WayBill t group by t.signStatus order by t.signStatus asc")
	List<Object[]> findCharts();


}
