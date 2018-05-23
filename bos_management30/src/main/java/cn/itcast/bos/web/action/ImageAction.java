package cn.itcast.bos.web.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Actions // 表示全局跳转（可以省略）
@Controller
@Scope(value="prototype") // 多实例
public class ImageAction extends BaseAction<Object>{
	
	// 上传的文件
	private File imgFile;
	// 上传的文件名
	private String imgFileFileName;
	// 上传的文件类型
	private String imgFileContentType;
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}

	// 图片上传
	@Action(value="image_upload",results={@Result(name="success",type="json")})
	public String upload() throws Exception{
		//文件保存目录路径（绝对路径）：用于上传
		String savePath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";

		//文件保存目录URL（相对路径）：用于保存
		String saveUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
		
		// UUID作为文件名（唯一）
		String fileName = UUID.randomUUID().toString();
		// 后缀
		String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
		fileName = fileName + ext;
		
		// 文件上传
		// 目标文件
		File destFile = new File(savePath+"/"+fileName);
		FileUtils.copyFile(imgFile, destFile);
		
		// 响应结果
		/**
		 *  //成功时
			{
			        "error" : 0,
			        "url" : "http://www.example.com/path/to/file.ext"
			}
			//失败时
			{
			        "error" : 1,
			        "message" : "错误信息"
			}
		 */
		Map<String, Object> map = new HashMap<>();
		map.put("error", 0);
		map.put("url", saveUrl+fileName);
		pushObjectToValueStack(map);
		return SUCCESS;
	}
	
	// 图片管理
	@Action(value="image_fileManager",results={@Result(name="success",type="json")})
	public String fileManager(){
		//根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  = ServletActionContext.getRequest().getContextPath() + "/upload/";
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		//根据path参数，设置各路径和URL
		String path = ServletActionContext.getRequest().getParameter("path") != null ? ServletActionContext.getRequest().getParameter("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		//目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("moveup_dir_path", moveupDirPath);
		result.put("current_dir_path", currentDirPath);
		result.put("current_url", currentUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		pushObjectToValueStack(result);
		return SUCCESS;
	}

}
