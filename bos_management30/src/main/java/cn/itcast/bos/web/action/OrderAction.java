package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	@Autowired
	private OrderService orderService;
	
	@Action(value="order_findByOrderNum",results={@Result(name="success",type="json")})
	public String findByOrderNum(){
		// 获取订单号
		String orderNum = model.getOrderNum();
		// 输入订单号，使用订单号完成订单数据的回显
		Order order = orderService.findByOrderNum(orderNum);
		Map<String, Object> map = new HashMap<>();
		if(order!=null){
			// 随机生成一个运单号
			String wayBillNum = "suyunwaybill"+RandomStringUtils.randomNumeric(32);
			WayBill wayBill = new WayBill();
			wayBill.setWayBillNum(wayBillNum);
			
			order.setWayBill(wayBill);
			map.put("success", true);
			map.put("orderData", order); // 存放Order
		}
		else{
			map.put("success", false);
		}
		pushObjectToValueStack(map);
		return SUCCESS;
	}
}
