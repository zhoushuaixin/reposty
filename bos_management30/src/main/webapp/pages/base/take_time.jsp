<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>收派时间管理</title>
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
		<script type="text/javascript">
			$(function(){
				// 自定义jquery的方法，将Form表单中的内容转换成json
				$.fn.serializeJson=function(){  
		            var serializeObj={};  
		            var array=this.serializeArray();  //name:"uname",value:"zhangsna"  uname:zhangsan  
		            $(array).each(function(){  
		                if(serializeObj[this.name]){  
		                    if($.isArray(serializeObj[this.name])){  
		                        serializeObj[this.name].push(this.value);  
		                    }else{  
		                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
		                    }  
		                }else{  
		                    serializeObj[this.name]=this.value;   
		                }  
		            });  
		            return serializeObj;  
		        }; 
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派时间管理信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/taketime_pageQuery.action",
					idField : 'id',
					columns : columns
				});
				// 添加、修改收派时间
				$('#addWindow').window({
			        title: '增加收派时间',
			        width: 600,
			        modal: true,
			        shadow: true,
			        closed: true,
			        height: 400,
			        resizable:false
			    });
				// 点击【保存】
				$("#save").click(function(){
					if($("#takeTimeForm").form("validate")){
						$("#takeTimeForm").submit();
					}
					else{
						$.messager.alert("警告","当前表单校验有误","warning");
					}
				})
				// 点击【查询】
				$("#searchBtn").click(function(){
					// 传递4个参数给服务器
					// 通常可以通过传递一些参数执行一次查询，通过调用这个方法从服务器加载新数据。
					//var params = $("#searchForm").serialize(); // 传递是一个字符串，格式：id=1&name=张三
					// var params = $("#searchForm").serializeArray();
					var params = $("#searchForm").serializeJson();// 自定义serializeJson的方法
					//alert(params);
					//alert(JSON.stringify(params)); // json的格式
					$("#grid").datagrid("load",params);
					// 关闭
					$("#searchWindow").window("close");
				})
			
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					// alert('增加');
					// 清空表单
					$("#takeTimeForm").form("clear");
					// 添加收派标准名称的校验
					// 将收派标准的名称设置为可写
					$("input[name='name']").attr("readonly",false);
					$("#addWindow").window('open');
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					// alert('修改');
					// 获取选中的datagrid对象
					var rows = $("#grid").datagrid("getSelections");
					if(rows!=null && rows.length!=1){
						$.messager.alert("警告","修改功能只能选择1个值","warning");
						return;
					}
					var row = rows[0]; // json形式
					// alert(row.id+"   "+row.name);
					// 传统做法：使用ajax，将id传递到服务器，使用id作为条件，查询Standard对象，将对象的值填充到standardForm中的id ，name ，minWeight...
					// 现在做法：读取记录填充到表单中。
					$('#takeTimeForm').form('load',row);
					// 去掉收派标准名称的校验
					$("input[name='name']").validatebox({
					    validType: "" 
					});  
					// 将收派标准的名称设置为只读
					$("#editWindow").window('open');
				}
			},{
				id : 'button-delete',
				text : '删除',
				iconCls : 'icon-cancel',
				handler : function(){
					//alert("删除...");
					// 获取选中的datagrid对象
					var rows = $("#grid").datagrid("getSelections");
					if(rows!=null && rows.length==0){
						$.messager.alert("警告","删除功能至少选择1个值","warning");
						return;
					}
					// 数组，传递id
					var array = new Array();
					for(var i=0;i<rows.length;i++){
						var row = rows[i];
						//alert(row.id+"   "+row.name);
						array.push(row.id);
					}
					// 将数组以一个字符串的形式传递，字符串的中间使用逗号分隔
					var ids = array.join(",");
					// alert(ids);
					// 删除：ajax
					
					// 删除：window.location.href（页面跳转）
					window.location.href = "${pageContext.request.contextPath}/takeTime_deleteByids.action?ids="+ids;
				}
				},{
					id : 'button-search',
					text : '查询',
					iconCls : 'icon-search',
					handler : function(){
						$("#searchWindow").window("open");
					}
				}];
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true,
			},{
				field : 'name',
				title : '时间名称',
				width : 120,
				align : 'center'
			}, {
				field : 'normalWorkTime',
				title : '平时上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'normalDutyTime',
				title : '平时休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'satWorkTime',
				title : '周六上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'satDutyTime',
				title : '周六休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'sunWorkTime',
				title : '周日上班时间',
				width : 120,
				align : 'center'
			}, {
				field : 'sunDutyTime',
				title : '周日休息时间',
				width : 120,
				align : 'center'
			}, {
				field : 'status',
				title : '状态',
				width : 120,
				align : 'center',
				formatter : function(data,row, index){
						if(data==null){
							return "正常"
						}else if(data==1){
							return "已作废";
						}else{
							return "正在使用"
						}
					}
			}, {
				field : 'company',
				title : '所属单位',
				width : 120,
				align : 'center'
			} , {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingTime',
				title : '操作时间',
				width : 120,
				formatter:function(value,row,index){
					if(value!=null){
						return value.replace("T"," ")
					}
				}
			}, {
				field : 'operatingCompany',
				title : '操作单位',
				width : 120,
				align : 'center'
			} ] ];
		</script>
	</head>
	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center" border="false">
			<table id="grid"></table>
		</div>
				<!-- 添加收派时间-->
		<div class="easyui-window" title="收派时间添加修改" id="addWindow" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:200px">
			<div style="height:31px;overflow:hidden;" split="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>

			<div style="overflow:auto;padding:5px;">
				<form id="takeTimeForm" 
					action="${pageContext.request.contextPath}/takeTime_save.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">收派信息</td>
							<!--提供隐藏域 装载id -->
							<input type="hidden" name="id" />
						</tr>
						<tr>
							<td>时间名称</td>
							<td>
								<input type="text" name="name" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" class="easyui-validatebox" required="true" />
							</td>
						</tr>
						<tr>
							<td>平时上班时间</td>
							<td>
								<input type="text" name="normalWorkTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
							<td>平常休息时间</td>
							<td>
								<input type="text" name="normalDutyTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
							<td>周六上班时间</td>
							<td>
								<input type="text" name="satWorkTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
							<td>周六休息时间</td>
							<td>
								<input type="text" name="satDutyTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
							<td>周日上班时间</td>
							<td>
								<input type="text" name="sunWorkTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
							<td>周日休息时间</td>
							<td>
								<input type="text" name="sunDutyTime" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
						<tr>
						<td>操作人单位</td>
							<td>
								<input type="text" name="operatingCompany" class="easyui-validatebox" required="true"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div class="easyui-window" title="查询收派时间" closed="true" id="searchWindow" collapsible="false" minimizable="false" maximizable="false" style="width: 400px; top:40px;left:200px">
			<div style="overflow:auto;padding:5px;" border="false">
				<form id="searchForm">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">查询条件</td>
						</tr>
						<tr>
							<td>时间名称</td>
							<td>
								<input type="text" name="name" />
							</td>
						</tr>
						<tr>
							<td>所属单位</td>
							<td>
								<input type="text" name="company" 
										class="easyui-combobox" 
										data-options="valueField:'company',textField:'company',
											url:'${pageContext.request.contextPath}/taketime_findCompany.action'"/>
							</td>
						</tr>
						<tr>
							<td colspan="2"><a id="searchBtn" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a> </td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	<body/>
</html>