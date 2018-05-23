package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {

	@Autowired
	private MenuService menuService;
	
	// 查询
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Menu> list = menuService.findMenuList();
		pushObjectToValueStack(list);
		return SUCCESS;
	}
	
	// 保存
	@Action(value="menu_save",results={@Result(name="success",type="redirect",location="./pages/system/menu.jsp")})
	public String save(){
		/**org.hibernate.TransientPropertyValueException*/
		if(model.getParentMenu()!=null && model.getParentMenu().getId()==0){
			model.setParentMenu(null);
		}
		menuService.save(model);
		return SUCCESS;
	}
	
	// 动态显示菜单
	@Action(value="menu_showMenu",results={@Result(name="success",type="json")})
	public String showMenu(){
		// 根据当前用户查询
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		List<Menu> list = menuService.findMenuListByUser(user);
		pushObjectToValueStack(list);
		return SUCCESS;
	}
}
