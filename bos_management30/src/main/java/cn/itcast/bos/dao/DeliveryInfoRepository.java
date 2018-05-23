package cn.itcast.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.bos.domain.transit.DeliveryInfo;

public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfo,Integer>,JpaSpecificationExecutor<DeliveryInfo> {


}
