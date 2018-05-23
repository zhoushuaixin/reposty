package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.SubAreaService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class SubAreaAction extends BaseAction<SubArea> {

	// 接收上传文件
	private File file;

	public void setFile(File file) {
		this.file = file;
	}

	@Autowired
	private SubAreaService subAreaService;

	// 批量区域数据导入
	@Action(value = "subArea_importSubArea")
	public String importSubArea() throws IOException {
		List<SubArea> subAreas = new ArrayList<SubArea>();
		// 编写解析代码逻辑
		// 基于.xls 格式解析 HSSF
		// 1、 加载Excel文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 2、 读取一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);//获取第一个sheet对象
		// 3、 读取sheet中每一行，一行数据 对应 一个区域对象
		for (Row row : sheet) {
			// 第一行表头跳过
			if (row.getRowNum() == 0) {
				// 第一行 跳过
				continue;
			}
			// 跳过空值的行，要求此行作废
			if (row.getCell(0) == null
					|| StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			SubArea subArea = new SubArea();
			subArea.setId(row.getCell(0).getStringCellValue());//区域编号
			
			FixedArea fixedArea = new FixedArea();
			fixedArea.setId(row.getCell(1).getStringCellValue());
			subArea.setFixedArea(fixedArea);//定区编码
			
			Area area = new Area();
			area.setId(row.getCell(2).getStringCellValue());
			subArea.setArea(area);//区域编码
			
			subArea.setKeyWords(row.getCell(3).getStringCellValue());// 关键字
			subArea.setStartNum(row.getCell(4).getStringCellValue());// 初始化
			subArea.setEndNum(row.getCell(5).getStringCellValue());// 结束号
			
			subArea.setSingle(row.getCell(6).getStringCellValue().toCharArray()[0]);// 单双号
			subArea.setAssistKeyWords(row.getCell(7).getStringCellValue());// 辅助关键字（位置信息）
			
			
			subAreas.add(subArea);
		}
		// 调用业务层
		subAreaService.saveSubAreas(subAreas);
		return NONE;
	}
	
	@Action(value="subArea_findAll",results={@Result(name="success",type="json")})
	public String findAll(){
		Pageable pageable = new PageRequest(page-1, rows);
		// 这里不做条件的查询了
		Page<SubArea> pageData = subAreaService.findAll(pageable);
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
}
