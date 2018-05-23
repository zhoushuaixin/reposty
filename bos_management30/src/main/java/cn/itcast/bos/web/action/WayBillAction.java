package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {
	// 日志
	Logger logger = Logger.getLogger(WayBillAction.class);

	@Autowired
	private WayBillService wayBillService;
	
	@Action(value="wayBill_save",results={@Result(name="success",type="json")})
	public String save(){
		
		Map<String, Object> map = new HashMap<>();
		try {
			// 防止瞬时态异常
			if(model.getOrder()!=null && model.getOrder().getId()==null){
				model.setOrder(null);
			}
			wayBillService.save(model);
			map.put("success", true);
			map.put("msg", "运单保存成功，运单号是："+model.getWayBillNum());
			logger.info("运单保存成功，运单号是："+model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "运单保存失败，失败的运单号是："+model.getWayBillNum());
			logger.error("运单保存失败，失败的运单号是："+model.getWayBillNum());
		}
		pushObjectToValueStack(map);
		return SUCCESS;
	}
	
	// 分页列表展示
	@Action(value="wayBill_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows,new Sort(Direction.ASC, "weight")); // 参数一：从0开始，0表示第一页
		// 同时传递查询条件
		Page<WayBill> pageData = wayBillService.findPageQuery(model,pageable);
		// 将数据结果封装到total属性和rows属性
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	// 输入运单号，使用运单号完成运单数据的回显
	@Action(value="wayBill_findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		// 获取订单号
		String wayBilklNum = model.getWayBillNum();
		
		WayBill wayBill = wayBillService.findByWayBillNum(wayBilklNum);
		Map<String, Object> map = new HashMap<>();
		if(wayBill!=null){
			map.put("success", true);
			map.put("wayBillData", wayBill); // 存放WayBill
		}
		else{
			map.put("success", false);
		}
		pushObjectToValueStack(map);
		return SUCCESS;
	}
}
