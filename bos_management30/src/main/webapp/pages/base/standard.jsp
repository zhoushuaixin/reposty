<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>取派标准</title>
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
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({visibility:"visible"});
				
				// 收派标准信息表格
				$('#grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [3,50,100],
					pagination : true,
					toolbar : toolbar,
					// url : "${pageContext.request.contextPath}/data/standard.json",
					url :"${pageContext.request.contextPath}/standard_pageQuery.action",
					idField : 'id',
					columns : columns
				});
				
				// 点击【保存】
				$("#save").click(function(){
					if($("#standardForm").form("validate")){
						$("#standardForm").submit();
					}
					else{
						$.messager.alert("警告","当前表单验证有误","warning");
					}
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
					$("#standardForm").form("clear");
					// 添加收派标准名称的校验
					$("input[name='name']").validatebox({
					    validType: "remote['${pageContext.request.contextPath}/standard_validatename.action','name']"   
					});  
					// 将收派标准的名称设置为可写
					$("input[name='name']").attr("readonly",false);
					$("#standardWindow").window('open');
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
					$('#standardForm').form('load',row);
					// 去掉收派标准名称的校验
					$("input[name='name']").validatebox({
					    validType: "" 
					});  
					// 将收派标准的名称设置为只读
					$("input[name='name']").attr("readonly",true);
					$("#standardWindow").window('open');
				}
			},{
				id : 'button-delete',
				text : '作废',
				iconCls : 'icon-cancel',
				handler : function(){
					// alert('作废');
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
					window.location.href = "${pageContext.request.contextPath}/standard_deleteByids.action?ids="+ids;
				}
			}];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				checkbox : true
			},{
				field : 'name',
				title : '标准名称',
				width : 120,
				align : 'center'
			}, {
				field : 'minWeight',
				title : '最小重量',
				width : 120,
				align : 'center'
			}, {
				field : 'maxWeight',
				title : '最大重量',
				width : 120,
				align : 'center'
			}, {
				field : 'minLength',
				title : '最小长度',
				width : 120,
				align : 'center'
			}, {
				field : 'maxLength',
				title : '最大长度',
				width : 120,
				align : 'center'
			}, {
				field : 'operator',
				title : '操作人',
				width : 120,
				align : 'center'
			}, {
				field : 'operatingTime',
				title : '操作时间',
				width : 120,
				align : 'center'
			}, {
				field : 'company',
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
		
		<div class="easyui-window" title="对收派标准进行添加或者修改" id="standardWindow" collapsible="false" minimizable="false" maximizable="false" modal="true" closed="true" style="width:600px;top:50px;left:200px">
			<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>

			<div region="center" style="overflow:auto;padding:5px;" border="false">
				
				<form id="standardForm" name="standardForm" action="${pageContext.request.contextPath}/standard_save.action" method="post">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">收派标准信息
								<!--提供隐藏域 装载id -->
								<input type="hidden" name="id" />
							</td>
						</tr>
						<tr>
							<td>收派标准名称</td>
							<td>
								<input type="text" name="name" class="easyui-validatebox" validtype="remote['${pageContext.request.contextPath}/standard_validatename.action','name']" required="true" missingMessage="收派标准不能为空" invalidMessage="收派标准名称已存在"/>
							</td>
						</tr>
						<tr>
							<td>最小重量</td>
							<td>
								<input type="text" name="minWeight" 
										class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最大重量</td>
							<td>
								<input type="text" name="maxWeight" class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最小长度</td>
							<td>
								<input type="text" name="minLength" class="easyui-numberbox" required="true" />
							</td>
						</tr>
						<tr>
							<td>最大长度</td>
							<td>
								<input type="text" name="maxLength" class="easyui-numberbox" required="true" />
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</body>

</html>