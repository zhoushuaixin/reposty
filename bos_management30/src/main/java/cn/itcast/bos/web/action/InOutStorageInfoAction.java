package cn.itcast.bos.web.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.transit.InOutStorageInfo;
import cn.itcast.bos.service.InOutStorageInfoService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class InOutStorageInfoAction extends BaseAction<InOutStorageInfo> {

	@Autowired
	private InOutStorageInfoService inOutStorageInfoService;
	
	// 属性驱动的方式
	// 流程id
	private String transitInfoId;
	
	public void setTransitInfoId(String transitInfoId) {
		this.transitInfoId = transitInfoId;
	}


	// 保存出入库
	@Action(value="inoutstorage_save",results={@Result(name="success",type="redirect",location="./pages/transit/transitinfo.jsp")})
	public String save(){
		inOutStorageInfoService.save(model,transitInfoId);
		return SUCCESS;
	}
	
}
