package cn.itcast.bos.service;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {

	void save(Promotion promotion);

	Page<Promotion> findPageQuery(Pageable pageable);


	// webservice的服务
	// 传递当前页和当前页最多显示的记录数，获取分页的结果
	@GET
	@Path("findPageQuery")
	@Produces(value={"application/xml","application/json"})
	@Consumes(value={"application/xml","application/json"})
	PageBean<Promotion> findPageQuery(@QueryParam("page")Integer page,@QueryParam("rows")Integer rows);
	
	@GET
	@Path("findById")
	@Produces(value={"application/xml","application/json"})
	@Consumes(value={"application/xml","application/json"})
	Promotion findById(@QueryParam("id")Integer id);

	void updateStatus(Date date);
}
