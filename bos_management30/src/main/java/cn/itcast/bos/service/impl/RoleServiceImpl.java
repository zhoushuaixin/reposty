package cn.itcast.bos.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.MenuRepository;
import cn.itcast.bos.dao.PermissionRepository;
import cn.itcast.bos.dao.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Role> findRoleListByUser(User user) {
		// 如果是系统管理员admin，此时查询所有的角色
		if(user.getUsername().equals("admin")){
			return roleRepository.findAll();
		}
		else{
			return roleRepository.findRoleListByUser(user.getId());
		}
		
	}
	
	@Override
	public List<Role> findRoleList() {
		return roleRepository.findAll();
	}
	
	
	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		// 1：保存角色
		roleRepository.save(role);
		// 2:建立角色和权限的关联关系
		if(permissionIds!=null){
			for(String permissionId:permissionIds){
				// 获取权限的持久对象
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
				role.getPermissions().add(permission);
			}
		}
		// 3：建立角色和菜单的关联关系
		if(StringUtils.isNotBlank(menuIds)){
			String[] arrayMenuIds = menuIds.split(",");
			for(String menuId:arrayMenuIds){
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
				role.getMenus().add(menu);
			}
		}
		
	}
}
