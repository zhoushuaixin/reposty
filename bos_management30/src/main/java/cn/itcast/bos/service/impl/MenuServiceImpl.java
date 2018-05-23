package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Override
	public List<Menu> findMenuList() {
		return menuRepository.findAll();
	}
	
	@Override
	public void save(Menu menu) {
		menuRepository.save(menu);
	}
	
	@Override
	public List<Menu> findMenuListByUser(User user) {
		// 如果是admin，查询所有的菜单
		if(user.getUsername().equals("admin")){
			return menuRepository.findAll(new Sort(Direction.ASC, "priority"));
		}
		else{
			return menuRepository.findMenuListByUser(user.getId());
		}
	}
}
