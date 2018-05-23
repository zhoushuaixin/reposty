package cn.itcast.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	void save(WayBill model);

	Page<WayBill> findPageQuery(WayBill wayBill, Pageable pageable);

	WayBill findByWayBillNum(String wayBilklNum);

	void syncIndex();

	List<WayBill> findWayBills(WayBill wayBill);

	List<Object[]> findCharts();


}
