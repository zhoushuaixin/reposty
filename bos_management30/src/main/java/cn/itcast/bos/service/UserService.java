package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> findUserList();

	void save(User user, String[] roleIds);


}
