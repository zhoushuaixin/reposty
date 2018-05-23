package cn.itcast.bos.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.PromotionService;

@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;

	@Override
	public void save(Promotion promotion) {
		promotionRepository.save(promotion);
	}
	
	@Override
	public Page<Promotion> findPageQuery(Pageable pageable) {
		return promotionRepository.findAll(pageable);
	}
	
	@Override
	public PageBean<Promotion> findPageQuery(Integer page, Integer rows) {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		
		// 封装返回的结果集
		PageBean<Promotion> pageBean = new PageBean<>();
		pageBean.setTotalElements(pageData.getTotalElements());
		pageBean.setContents(pageData.getContent());
		return pageBean;
	}
	
	@Override
	public Promotion findById(Integer id) {
		return promotionRepository.findOne(id);
	}
	
	@Override
	public void updateStatus(Date date) {
		promotionRepository.updateStatus(date);
	}
}
