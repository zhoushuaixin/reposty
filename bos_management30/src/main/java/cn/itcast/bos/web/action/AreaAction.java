package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage(value="json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

	@Autowired
	private AreaService areaService;
	
	// 属性驱动
	// input type="file" name="file"
	// 文件
	private File file;
	// 文件名
	private String fileFileName;
	// 文件类型
	private String fileContentType;
	
	public void setFile(File file) {
		this.file = file;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	@Action(value="area_importData",results={@Result(name="success",type="json")})
	public String importData() throws Exception{
		//System.out.println(file);
		//System.out.println(fileFileName);
		//System.out.println(fileContentType);
		// 存放数据
		List<Area> list = new ArrayList<Area>();
		// 使用POI技术解析excel（.xls）读取数据，存放到List中
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		// 解析workbook（工作薄）---> sheet（工作表）---> row（行）---> cell（列/单元格） 
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);// 0 表示第一个工作表
		// 遍历
		for(Row row:sheet){
			// 去掉头行（去掉表头）
			if(row.getRowNum()==0){
				continue; // 继续走
			}
			// 保证第一个列的值不能为null，应为它是主键
			if(row.getCell(0)==null || row.getCell(0).getStringCellValue()==null){ // row.getCell(0).getStringCellValue()表示获取单元格的值
				continue;
			}
			Area area = new Area();
			// 区域编码	省份	城市	区域	邮编
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			// 简码
			// 去掉最后一个字
			String province = area.getProvince().substring(0,area.getProvince().length()-1);
			String city = area.getCity().substring(0,area.getCity().length()-1);
			String district = area.getDistrict().substring(0,area.getDistrict().length()-1);
			String[] headByString = PinYin4jUtils.getHeadByString(province+city+district);
			StringBuffer buffer = new StringBuffer();
			for(String head:headByString){
				buffer.append(head);
			}
			area.setShortcode(buffer.toString());
			// 城市编码
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			list.add(area);
		}
		// 批量保存
		areaService.saveAreas(list);
		return SUCCESS;
	}
	
	// 分页查询区域列表
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows); // 参数一：从0开始，0表示第一页
		// 有条件的分页查询
		Specification<Area> spec = new Specification<Area>() {
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// 用来封装返回的结果
				//Predicate predicate = null;
				// 3个参数，使用集合存放3个条件
				List<Predicate> list = new ArrayList<>();
				// 省
				if(StringUtils.isNotBlank(model.getProvince())){
					Predicate p = cb.like(root.get("province").as(String.class), "%"+model.getProvince()+"%") ;// 参数一：字段名称；参数二：字段值，等同于provinc like ?
					list.add(p);
				}
				// 市
				if(StringUtils.isNotBlank(model.getCity())){
					Predicate p = cb.like(root.get("city").as(String.class), "%"+model.getCity()+"%") ;// 参数一：字段名称；参数二：字段值，等同于city like ?
					list.add(p);
				}
				// 区
				if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p = cb.like(root.get("district").as(String.class), "%"+model.getDistrict()+"%") ;// 参数一：字段名称；参数二：字段值，等同于district like ?
					list.add(p);
				}
				
				if(list!=null && list.size()>0){
					Predicate p [] = new Predicate [list.size()]; // 初始化数组
					query.where(list.toArray(p));
				}
				query.orderBy(cb.desc(root.get("id").as(String.class))); // 降序排列 id字段
				return query.getRestriction();
			}
		};
		Page<Area> pageData = areaService.findPageQuery(spec,pageable);
		// 将数据结果封装到total属性和rows属性
		pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
}
