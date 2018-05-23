package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.domain.transit.TransitInfo;
import cn.itcast.bos.service.TransitInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TransitInfoAction extends BaseAction<TransitInfo> {

	@Autowired
	private TransitInfoService transitInfoService;
	
	// 属性驱动
	// 运单id的字符串（中间使用逗号分开）
	private String wayBillIds;
	
	public void setWayBillIds(String wayBillIds) {
		this.wayBillIds = wayBillIds;
	}

	// 开启中转配送流程（将运单的状态从1（待发货）变成2（配送中）），如果流程的状态变成2，表示流程已经启动
	@Action(value="transit_create",results={@Result(name="success",type="json")})
	public String create(){
		Map<String, Object> map = new HashMap<>();
		try {
			transitInfoService.startWaybillProcess(wayBillIds);
			map.put("success", true);
			map.put("msg", "运单启动成功，运单号是："+wayBillIds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "运单启动失败，运单号是："+wayBillIds);
		}
		pushObjectToValueStack(map);
		return SUCCESS;
	}
	
	// 查询
	@Action(value="transit_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
		Page<TransitInfo> pageData = transitInfoService.findPageQuery(pageable);
		// 将数据结果封装到total属性和rows属性
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
