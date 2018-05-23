package cn.itcast.bos.web.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.TakeTimeService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Actions // 表示全局跳转（可以省略）
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {

	@Autowired
	private TakeTimeService takeTimeService;
	@Action(value="takeTime_save",results={@Result(name="success",type="redirect",location="pages/base/take_time.jsp")})
	public String save(){
		takeTimeService.save(model);
		return SUCCESS;
	}
	// 属性驱动
	private String ids;  // 字符串，存放多个id，中间使用逗号分隔
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	// 使用ids完成批量删除（假删除，执行update）
		@Action(value="takeTime_deleteByids",results={@Result(name="success",type="redirect",location="pages/base/take_time.jsp")})
		public String deleteByids(){
			String [] arrayIds = ids.split(",");
			takeTimeService.deleteByids(arrayIds);
			return SUCCESS;
		}
		// 分页查询快递员列表
		@Action(value="taketime_pageQuery",results={@Result(name="success",type="json")})
		public String pageQuery(){
			Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
			// 无条件的分页
			// Page<Courier> pageData = courierService.findPageQuery(pageable);
			// 有条件的分页查询
			Specification<TakeTime> spec = new Specification<TakeTime>() {
				/**
				 * 传递：
				 * 		Root<Courier> root：（连接语句的时候需要字段，获取字段的名称）代表Criteria查询的根对象，Criteria查询的查询根定义了实体类型，能为将来导航获得想要的结果，它与SQL查询中的FROM子句类似
				 * 		CriteriaQuery<?> query： （简单的查询可以使用CriteriaQuery）代表一个specific的顶层查询对象，它包含着查询的各个部分，比如：select 、from、where、group by、order by等
				 * 		CriteriaBuilder cb：（复杂的查询可以使用CriteriaBuilder构建）用来构建CritiaQuery的构建器对象
				 * 返回：Predicate：封装查询条件
				 * 
				 */
				@Override
				public Predicate toPredicate(Root<TakeTime> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					// 用来封装返回的结果
					//Predicate predicate = null;
					// 4个参数，使用集合存放4个条件
					List<Predicate> list = new ArrayList<>();
					// 收派时间名称
					 list.add(cb.notEqual(root.get("status"),"1"));
					if(StringUtils.isNotBlank(model.getName())){
						Predicate p = cb.equal(root.get("name").as(String.class), model.getName()) ;// 参数一：字段名称；参数二：字段值，等同于courierNum=?
						list.add(p);
					}
					//所属单位	
					if(StringUtils.isNotBlank(model.getCompany())){
						Predicate p = cb.like(root.get("company").as(String.class),"%"+ model.getCompany()+"%") ;// 参数一：字段名称；参数二：字段值，等同于company like ?
						list.add(p);
					}
					// 方案一
//					// 存在查询条件
//					if(list!=null && list.size()>0){
//						Predicate p [] = new Predicate [list.size()]; // 初始化数组
//						predicate = cb.and(list.toArray(p)) ;// 相当于： 条件1 and 条件2
//					}
//					return predicate;
					// 方案二
					if(list!=null && list.size()>0){
						Predicate p [] = new Predicate [list.size()]; // 初始化数组
						query.where(list.toArray(p));
					}
					query.orderBy(cb.desc(root.get("id").as(Integer.class))); // 降序排列 id字段
					return query.getRestriction();
				}
			};
			Page<TakeTime> pageData = takeTimeService.findPageQuery(spec,pageable);
			// 将数据结果封装到total属性和rows属性
			pushPageDataToValueStack(pageData);
			return SUCCESS;
		}
		@Action(value="taketime_findCompany",results={@Result(name="success",type="json")})
		public String findCompany(){
			List<TakeTime>list=takeTimeService.findCompany();
			ServletActionContext.getContext().getValueStack().push(list);
			return SUCCESS;
		}
}
