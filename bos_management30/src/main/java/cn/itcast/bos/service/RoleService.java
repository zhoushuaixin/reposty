package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RoleService {

	List<Role> findRoleListByUser(User user);

	List<Role> findRoleList();

	void save(Role role, String[] permissionIds, String menuIds);


}
