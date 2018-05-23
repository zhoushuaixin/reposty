<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>角色添加</title>
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
		<!-- 导入ztree类库 -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/js/ztree/zTreeStyle.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/ztree/jquery.ztree.all-3.5.js" type="text/javascript"></script>
		<script type="text/javascript">
			$(function(){
				// 授权树初始化
				var setting = {
					data : {
						simpleData : {
							enable : true
						}
					},
					check : {
						enable : true,
					}
				};
				
				$.ajax({
					// url : '${pageContext.request.contextPath}/data/menu.json',
					url: '${pageContext.request.contextPath}/menu_list.action',
					type : 'POST',
					dataType : 'json', // 查询响应的数据类型
					success : function(data) {
						// alert(data);
						// var zNodes = eval("(" + data + ")"); // 将一个json字符串转换成json对象
						// alert(zNodes);
						$.fn.zTree.init($("#menuTree"), setting, data);
					},
					error : function(msg) {
						alert('树加载异常!');
					}
				});
				
				
				
				// 点击保存
				$('#save').click(function(){
					// location.href='role.html';
					// 获取ztree勾选节点的集合
					var treeObj = $.fn.zTree.getZTreeObj("menuTree");
					var nodes = treeObj.getCheckedNodes(true);
					// 获取所有节点id
					var array = new Array();
					for(var i=0; i<nodes.length; i++){
						array.push(nodes[i].id);
					}
					var menuIds = array.join(","); // 菜单id的字符串，多个id中间用逗号 分开
					$("input[name='menuIds']").val(menuIds);
					
					// 提交form
					if($("#roleForm").form('validate')){
						$("#roleForm").submit();
					}
				});
				
				/**
				 * 创建checkbox，将创建的checkbox存放到td id="permissionTD"
				 *      <td id="permissionTD">
							<input type="checkbox" name="permissionIds" value="1" /> 添加快递员 
							<input type="checkbox" name="permissionIds" value="2" /> 快递员列表查询
							<input type="checkbox" name="permissionIds" value="3" /> 添加区域 
						</td>
				 */
				// 查询所有的权限
				$.post("${pageContext.request.contextPath}/permission_list.action",{},function(data){
					$(data).each(function(){
						// 创建checkbox
						var checkbox = $("<input type='checkbox' name='permissionIds'/>");
						checkbox.val(this.id);// 赋值id
						$("#permissionTD").append(checkbox);
						$("#permissionTD").append(this.name);
					})
				})
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="roleForm" method="post" action="${pageContext.request.contextPath}/role_save.action">
				<table class="table-edit" width="80%" align="center">
					<tr class="title">
						<td colspan="2">角色信息</td>
					</tr>
					<tr>
						<td>名称</td>
						<td>
							<input type="text" name="name" class="easyui-validatebox" data-options="required:true" />
						</td>
					</tr>
					<tr>
						<td>关键字</td>
						<td>
							<input type="text" name="keyword" class="easyui-validatebox" data-options="required:true" />
						</td>
					</tr>
					<tr>
						<td>描述</td>
						<td>
							<textarea name="description" rows="4" cols="60"></textarea>
						</td>
					</tr>
					<tr>
						<td>权限选择</td>
						<td id="permissionTD">
						</td>
					</tr>
					<tr>
						<td>菜单授权</td>
						<td>
							<input type="hidden" id="menuIds" name="menuIds"/>
							<ul id="menuTree" class="ztree"></ul>
						</td>
					</tr>
					
				</table>
			</form>
		</div>
	</body>

</html>