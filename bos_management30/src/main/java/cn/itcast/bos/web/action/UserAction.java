package cn.itcast.bos.web.action;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.UserService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {

	@Autowired
	private UserService userService;
	
	@Action(value="user_login",results={@Result(name="success",type="redirect",location="./index.jsp"),
			@Result(name="error",type="redirect",location="./login.jsp")})
	public String login(){
		// 应用程序-->Subject-->SecurityManager-->Realm--数据库
		// Subject（用户信息）
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken authenticationToken = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		try {
			// 登录认证，认证用户名和密码，如果认证成功，不会抛出异常
			subject.login(authenticationToken);
		} catch (AuthenticationException e) {
			// 如果用户名或密码输入有误，抛出异常
			e.printStackTrace();
			if(e instanceof UnknownAccountException){
				System.out.println("用户名输入有误！");
			}
			if(e instanceof IncorrectCredentialsException){
				System.out.println("密码输入有误！");
			}
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Action(value="user_logout",results={@Result(name="success",type="redirect",location="./login.jsp")})
	public String logout(){
		// 清空subject
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return SUCCESS;
	}
	
	// 查询
	@Action(value="user_list",results={@Result(name="success",type="json")})
	public String list(){
		List<User> list = userService.findUserList();
		pushObjectToValueStack(list);
		return SUCCESS;
	}
	
	// 属性驱动
	// 角色的id数组
	private String [] roleIds;

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	// 保存
	@Action(value="user_save",results={@Result(name="success",type="redirect",location="./pages/system/userlist.jsp")})
	public String save(){
		userService.save(model,roleIds);
		return SUCCESS;
	}
}
