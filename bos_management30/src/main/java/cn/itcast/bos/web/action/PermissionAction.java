package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.PermissionService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {

	@Autowired
	private PermissionService permissionService;
	
	// 查询
	@Action(value="permission_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Permission> list = permissionService.findPermissionList();
		pushObjectToValueStack(list);
		return SUCCESS;
	}
	
	// 保存
	@Action(value="permission_save",results={@Result(name="success",type="redirect",location="./pages/system/permission.jsp")})
	public String save(){
		permissionService.save(model);
		return SUCCESS;
	}
}
