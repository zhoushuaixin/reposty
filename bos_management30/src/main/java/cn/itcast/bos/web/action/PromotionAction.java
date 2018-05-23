package cn.itcast.bos.web.action;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	@Autowired
	private PromotionService promotionService;
	
	// 属性驱动
	// 上传的文件
	private File titleImgFile;
	// 上传的文件名称
	private String titleImgFileFileName;
	
	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	// 保存
	@Action(value="promotion_save",results={@Result(name="success",type="redirect",location="./pages/take_delivery/promotion.jsp")})
	public String save() throws Exception{
		// 宣传图片上传
		//文件保存目录路径（绝对路径）：用于上传
		String savePath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";

		//文件保存目录URL（相对路径）：用于保存
		String saveUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
		
		// UUID作为文件名（唯一）
		String fileName = UUID.randomUUID().toString();
		// 后缀
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		fileName = fileName + ext;
		
		// 文件上传
		// 目标文件
		File destFile = new File(savePath+"/"+fileName);
		FileUtils.copyFile(titleImgFile, destFile);
		// 相对路径的保存
		model.setTitleImg(saveUrl+fileName);
		promotionService.save(model);
		return SUCCESS;
	}
	
	// 分页列表展示
	@Action(value="promotion_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
		Page<Promotion> pageData = promotionService.findPageQuery(pageable);
		// 将数据结果封装到total属性和rows属性
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
