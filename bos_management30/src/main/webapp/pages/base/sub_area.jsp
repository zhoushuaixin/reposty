<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>管理分区</title>
		<!-- 导入jquery核心类库 -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
		<!-- 导入easyui类库 -->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/ext/portal.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.portal.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.cookie.js"></script>
		<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
		<!-- 导入一键上传 -->
		<!-- 导入ocupload -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/ocupload/jquery.ocupload-1.1.2.js"></script>
		<script type="text/javascript">
			function doAdd(){
				$('#addWindow').window("open");
			}
			
			function doEdit(){
				alert("修改...");
			}
			
			function doDelete(){
				alert("删除...");
			}
			
			function doSearch(){
				$('#searchWindow').window("open");
			}
			
			function doExport(){
				alert("导出");
			}
			
			function doImport(){
				alert("导入");
			}
			
			//工具栏
			var toolbar = [ {
				id : 'button-search',	
				text : '查询',
				iconCls : 'icon-search',
				handler : doSearch
			}, {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : doAdd
			}, {
				id : 'button-edit',	
				text : '修改',
				iconCls : 'icon-edit',
				handler : doEdit
			},{
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : doDelete
			},{
				id : 'button-import',
				text : '导入',
				iconCls : 'icon-redo'
			},{
				id : 'button-export',
				text : '导出',
				iconCls : 'icon-undo',
				handler : doExport
			}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			}, {
				field : 'showid',
				title : '分拣编号',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					return row.id;
				}
			},{
				field : 'area.province',
				title : '省',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					if(row.area != null ){
						return row.area.province;
					}
					return "" ;
				}
			}, {
				field : 'area.city',
				title : '市',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					if(row.area != null ){
						return row.area.city;
					}
					return "" ;
				}
			}, {
				field : 'area.district',
				title : '区',
				width : 120,
				align : 'center',
				formatter : function(data,row ,index){
					if(row.area != null ){
						return row.area.district;
					}
					return "" ;
				}
			}, {
				field : 'keyWords',
				title : '关键字',
				width : 120,
				align : 'center'
			}, {
				field : 'startnum',
				title : '起始号',
				width : 100,
				align : 'center'
			}, {
				field : 'endnum',
				title : '终止号',
				width : 100,
				align : 'center'
			} , {
				field : 'single',
				title : '单双号',
				width : 100,
				align : 'center'
			} , {
				field : 'assistKeyWords',
				title : '辅助关键字',
				width : 100,
				align : 'center'
			} ] ];
			
			$(function(){
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 分区管理数据表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : true,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					// url : "${pageContext.request.contextPath}/data/sub_area.json",
					url : "${pageContext.request.contextPath}/subArea_findAll.action",
					idField : 'id',
					columns : columns,
					onDblClickRow : doDblClickRow
				});
				
				// 添加、修改分区
				$('#addWindow').window({
			        title: '添加修改分区',
			        width: 600,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				
				// 查询分区
				$('#searchWindow').window({
			        title: '查询分区',
			        width: 400,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				$("#btn").click(function(){
					alert("执行查询...");
				});
				
				// 为导入按钮，添加一键上传效果 
			    $("#button-import").upload({
			    	// 默认name=’file’ 
			    	action : '${pageContext.request.contextPath}/subArea_importSubArea.action',
                    // 默认enctype=’multipart/form-data’ 
			    	// 在选择文件的时候触发的事件
			    	onSelect :function(){
			    		// 选中文件后，关闭自动提交 
			    		this.autoSubmit = false ;
			    		// 判定文件格式 ，以.xls 或者 .xlsx 结尾 
			    		var filename = this.filename();
			    		var regex = /^.*\.(xls|xlsx)$/ ;
			    		if(regex.test(filename)){
			    			// 满足
			    			this.submit();
			    		}else{
			    			//不满足
			    			$.messager.alert("警告","只能上传.xls或.xlsx结尾的文件！","warning");
			    		}
			    	},
			    	onComplete : function(response){
			    		alert("文件上传成功！");
			    	}
			    });
			});
		
			function doDblClickRow(){
				alert("双击表格数据...");
			}
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
		<!-- 添加 修改分区 -->
		<div class="easyui-window" title="分区添加修改" id="addWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div style="height:31px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>

			<div style="overflow:auto;padding:5px;" border="false">
				<form>
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">分区信息</td>
						</tr>
						<tr>
							<td>分拣编码</td>
							<td>
								<input type="text" name="id" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>选择区域</td>
							<td>
								<input class="easyui-combobox" name="area.id" data-options="valueField:'id',textField:'name',url:'${pageContext.request.contextPath}/data/area.json'" />
							</td>
						</tr>
						<tr>
							<td>关键字</td>
							<td>
								<input type="text" name="keyWords" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>起始号</td>
							<td>
								<input type="text" name="startnum" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>终止号</td>
							<td>
								<input type="text" name="endnum" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>单双号</td>
							<td>
								<select class="easyui-combobox" name="single" style="width:150px;">
									<option value="0">单双号</option>
									<option value="1">单号</option>
									<option value="2">双号</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>位置信息</td>
							<td>
								<input type="text" name="position" class="easyui-validatebox" required="true" style="width:250px;" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<!-- 查询分区 -->
		<div class="easyui-window" title="查询分区窗口" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div style="overflow:auto;padding:5px;" border="false">
				<form>
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">查询条件</td>
						</tr>
						<tr>
							<td>省</td>
							<td>
								<input type="text" name="province" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>市</td>
							<td>
								<input type="text" name="city" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>区（县）</td>
							<td>
								<input type="text" name="district" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>定区编码</td>
							<td>
								<input type="text" name="decidedzone.id" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>关键字</td>
							<td>
								<input type="text" name="keyWords" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td colspan="2"><a id="btn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>

</html>