package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<Permission> findPermissionListByUser(User user) {
		// 如果是超级管理员admin，此时查询所有的权限
		if(user.getUsername().equals("admin")){
			return permissionRepository.findAll();
		}
		else{
			return permissionRepository.findPermissionListByUser(user.getId());
		}
	}
	
	@Override
	public List<Permission> findPermissionList() {
		return permissionRepository.findAll();
	}
	
	@Override
	public void save(Permission permission) {
		permissionRepository.save(permission);
	}
}
