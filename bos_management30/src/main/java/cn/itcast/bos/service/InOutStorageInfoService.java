package cn.itcast.bos.service;

import cn.itcast.bos.domain.transit.InOutStorageInfo;

public interface InOutStorageInfoService {

	void save(InOutStorageInfo inOutStorageInfo, String transitInfoId);


}
