package cn.itcast.bos.service.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.AreaRepository;
import cn.itcast.bos.dao.FixedAreaRepository;
import cn.itcast.bos.dao.OrderRepository;
import cn.itcast.bos.dao.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.constant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private FixedAreaRepository fixedAreaRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private WorkBillRepository workBillRepository;
	
	// MQ的模板
	@Autowired
	@Qualifier("jmsQueueTemplate")
	JmsTemplate jmsTemplate;
	
	@Override
	public void save(Order order) {
		// System.out.println(order);
		// 订单号（唯一），方案一：UUID；方案二：时间戳
		order.setOrderNum(UUID.randomUUID().toString());
		// 下单时间
		order.setOrderTime(new Date());
		// 订单状态 1 待取件 2 运输中 3 已签收 4 异常
		order.setStatus("1");
		
		// 使用省市区，获取对应省市区的持久化对象（寄件人的省市区）
		Area sendArea = order.getSendArea();
		Area persistSendArea = areaRepository.findByProvinceAndCityAndDistrict(sendArea.getProvince(),sendArea.getCity(),sendArea.getDistrict());
		order.setSendArea(persistSendArea);
		// 使用省市区，获取对应省市区的持久化对象（收件人的省市区）
		Area recArea = order.getRecArea();
		Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(recArea.getProvince(),recArea.getCity(),recArea.getDistrict());
		order.setRecArea(persistRecArea);
		// 自动分单
		/**2.2． 自动分单逻辑 -- 基于CRM地址完全匹配自动分单（1）*/
		// 使用客户ID和寄件人的地址，查询CRM表，获取定区id
		String fixedAreaId = WebClient.create(Constants.CRM_MANAGEMENT_URL+"/services/customerService/findByIdAndAddress?id="+order.getCustomer_id()+"&address="+order.getSendAddress())
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.get(String.class);
		if(StringUtils.isNotBlank(fixedAreaId)){
			// 获取当前定区查询定区对象
			FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
			// 使用定区对象获取定区关联的快递员
			if(fixedArea!=null){
				Iterator<Courier> iterator = fixedArea.getCouriers().iterator();
				while(iterator.hasNext()){
					Courier courier = iterator.next();
					// 不管是哪个快递员，查找第一个
					// 保存订单，同时关联快递员，完成自动分单
					saveOrder(order,courier);
					// 生成工单
					saveWorkBill(order);
					return; // 不往下执行
				}
			}
		}
		/**2.3． 自动分单逻辑 -- 基于分区关键字匹配自动分单（2）--重要*/
		// 通过区域对象获取分区
		for(SubArea subArea:persistSendArea.getSubareas()){
			// 根据寄件人地址是否包含分区的关键字
			if(order.getSendAddress().contains(subArea.getKeyWords())){
				// 通过分区，找到定区，从而找到快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				while(iterator.hasNext()){
					Courier courier = iterator.next();
					// 不管是哪个快递员，查找第一个
					// 保存订单，同时关联快递员，完成自动分单
					saveOrder(order,courier);
					// 生成工单
					saveWorkBill(order);
					return; // 不往下执行
				}
				
			}
			// 根据寄件人地址是否包含分区的辅助关键字
			if(order.getSendAddress().contains(subArea.getAssistKeyWords())){
				// 通过分区，找到定区，从而找到快递员
				Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
				while(iterator.hasNext()){
					Courier courier = iterator.next();
					// 不管是哪个快递员，查找第一个
					// 保存订单，同时关联快递员，完成自动分单
					saveOrder(order,courier);
					// 生成工单
					saveWorkBill(order);
					return; // 不往下执行
				}
				
			}
		}
		// 自动分单逻辑都没有完成，进入到人工分单
		// 分单类型 1 自动分单 2 人工分单
		order.setOrderType("2");
		orderRepository.save(order);
	}

	// 生成工单，给快递员发送短信通知
	private void saveWorkBill(final Order order) {
		WorkBill workBill = new WorkBill();
		// 工单类型 新,追,销
		workBill.setType("新");
		/*
		 * // 取件状态
		 * 新单:没有确认货物状态的 
		   已通知:自动下单下发短信 
		   已确认:接到短信,回复收信确认信息 
		   已取件:已经取件成功,发回确认信息 生成工作单
		 * 已取消:销单
		 */
		workBill.setPickstate("新单");
		// 工单生成时间
		workBill.setBuildtime(new Date());
		// 追单次数
		workBill.setAttachbilltimes(0);
		// 订单备注
		workBill.setRemark(order.getRemark());
		// 短信序号
		final String number = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(number);
		// 关联快递员
		workBill.setCourier(order.getCourier());
		// 关联订单
		workBill.setOrder(order);
		// 发送短信给快递员（手机号）
		jmsTemplate.send("bos_couriersms", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				// 发送的快递员的电话号
				mapMessage.setString("telephone", order.getCourier().getTelephone());
				// 速运快递 短信序号：${num},取件地址：${sendAddres},联系人：${sendName},手机:${sendMobile}，快递员捎话：${sendMobileMsg}
				mapMessage.setString("num", number);
				mapMessage.setString("sendAddres", order.getSendAddress());
				mapMessage.setString("sendName", order.getSendName());
				mapMessage.setString("sendMobile", order.getSendMobile());
				mapMessage.setString("sendMobileMsg", order.getSendMobileMsg());
				return mapMessage;
			}
		});
		
		workBill.setPickstate("已通知");
		workBillRepository.save(workBill);
	}

	// 保存订单，同时关联快递员，完成自动分单
	private void saveOrder(Order order, Courier courier) {
		// 分单类型 1 自动分单 2 人工分单
		order.setOrderType("1");
		// 订单关联快递员（外键）
		order.setCourier(courier);
		// 保存订单
		orderRepository.save(order);
	}
	
	@Override
	public Order findByOrderNum(String orderNum) {
		return orderRepository.findByOrderNum(orderNum);
	}

}
