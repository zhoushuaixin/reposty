package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.PermissionService;
import cn.itcast.bos.service.RoleService;
import cn.itcast.bos.service.UserService;

// 操作数据库
public class BosRealm extends AuthorizingRealm{
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PermissionService permissionService;

	// 认证过滤器（登录）
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		System.out.println("shiro认证...");
		// 获取登录名，使用登录名作为参数，查询数据库，返回唯一的用户对象
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
		String username = usernamePasswordToken.getUsername();
		User user = userService.findByUsername(username);
		// 输入的用户名有误
		if(user==null){
			// shiro，返回null，抛出一个异常(org.apache.shiro.authc.UnknownAccountException)，用户名输入有误
			return null;
		}
		else{
			// 用户名输入正确 ，返回SimpleAuthenticationInfo
			/**
			 * 参数一：Object principal  ：用于将查询的用户信息，存放到principal对象，类似于Session
			 * 参数二：Object credential : 用于存放的密码（数据库中的密码），shiro会拿存放的密码和Subject中存放的密码进行比对，如果成功，说明密码输入正确；如果不成功，抛出异常（org.apache.shiro.authc.IncorrectCredentialsException）
			 * 参数三：String，realmname: 当前Realm的名称
			 */
			return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
		}
	}
	
	// 授权过滤器（粗细颗粒度权限控制）
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		System.out.println("shiro授权...");
		// 主题存放当前用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		// 使用当前登录名，查询当前登录名所具有的角色，将查询的值，存放到shiro中
		List<Role> roleList = roleService.findRoleListByUser(user);
		for(Role role:roleList){
			authorizationInfo.addRole(role.getKeyword());
		}
		// 使用当前登录名，查询当前登录名所具有的权限，将查询的值，存放到shiro中
		List<Permission> permissionList = permissionService.findPermissionListByUser(user);
		for(Permission permission:permissionList){
			authorizationInfo.addStringPermission(permission.getKeyword());
		}
		return authorizationInfo;
	}

	

	
}
