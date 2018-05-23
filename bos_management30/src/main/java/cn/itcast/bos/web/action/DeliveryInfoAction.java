package cn.itcast.bos.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.DeliveryInfo;
import cn.itcast.bos.service.DeliveryInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class DeliveryInfoAction extends BaseAction<DeliveryInfo> {

	@Autowired
	private DeliveryInfoService deliveryInfoService;
	
	// 属性驱动的方式
	// 流程id
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}


	// 保存开始配送
	@Action(value="delivery_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.jsp")})
	public String save(){
		deliveryInfoService.save(model,transitInfoId);
		return SUCCESS;
	}
}
