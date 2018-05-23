package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

public interface MenuService {

	List<Menu> findMenuList();

	void save(Menu menu);

	List<Menu> findMenuListByUser(User user);


}
