package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {

	@Autowired
	private RoleService roleService;
	
	// 查询
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Role> list = roleService.findRoleList();
		pushObjectToValueStack(list);
		return SUCCESS;
	}
	
	// 属性驱动
	// 权限的id数组
	private String [] permissionIds;
	// 菜单id的字符串，多个id值中间使用逗号分开
	private String menuIds;
	
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}


	// 保存
	@Action(value="role_save",results={@Result(name="success",type="redirect",location="./pages/system/role.jsp")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
