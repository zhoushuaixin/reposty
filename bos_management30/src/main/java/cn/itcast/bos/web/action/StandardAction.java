package cn.itcast.bos.web.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.StandardService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Actions // 表示全局跳转（可以省略）
@Controller
@Scope(value="prototype") // 多实例
public class StandardAction extends BaseAction<Standard>{
	
	@Autowired
	StandardService standardService;

	// 保存
	@Action(value="standard_save",results={@Result(name="success",type="redirect",location="pages/base/standard.jsp")})
	public String save(){
		// System.out.println(standard);
		standardService.save(model);
		return SUCCESS;
	}

	// 验证收派标准名称不能出现重复(ajax)
	@Action(value="standard_validatename",results={@Result(name="success",type="json")})
	public String validatename(){
		// 对应easyui的校验机制，如果返回true：表示校验成功，如果返回false：表示校验失败
		// 获取页面收派标准名称的值
		String name = model.getName();
		// 使用收派标准名称作为查询条件，判断当前收派标准名称在数据库中是否存在，后台执行： from Standard where name = ?
		List<Standard> list = standardService.findByName(name);
		// 没有查询到结果（当前输入的值可以保存）
		if(list!=null && list.size()==0){
			// 将结果存放到栈顶
			ServletActionContext.getContext().getValueStack().push(true);
		}
		else{
			ServletActionContext.getContext().getValueStack().push(false);
		}
		return SUCCESS;
	}

	// 分页查询收派标准列表
	@Action(value="standard_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
		Page<Standard> pageData = standardService.findPageQuery(pageable);
		// 将数据结果封装到total属性和rows属性
		/**
		 *  {                                                      
				"total":100,	
				"rows":[ 
					{"id":"001","name":"10-20公斤","minWeight":"10","maxWeight":"20","minLength":"11","maxLength":"22","operator":"张三","operatingTime":"2016-08-18","company":"杭州分部"}
				]
			}
		 */
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}

	// 属性驱动
	private String ids;  // 字符串，存放多个id，中间使用逗号分隔
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	// 使用ids完成批量删除
	@Action(value="standard_deleteByids",results={@Result(name="success",type="redirect",location="pages/base/standard.jsp")})
	public String deleteByids(){
		String [] arrayIds = ids.split(",");
		standardService.deleteByids(arrayIds);
		return SUCCESS;
	}
	
	// 查询所有的收派标准，用于页面easyui-combox的下拉列表框显示
	@Action(value="standard_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		List<Standard> list = standardService.findAll();
		ServletActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
}
