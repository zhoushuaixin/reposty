package cn.itcast.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.transit.TransitInfo;

public interface TransitInfoService {

	void startWaybillProcess(String wayBillIds);

	Page<TransitInfo> findPageQuery(Pageable pageable);


}
