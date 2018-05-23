<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>基本档案信息管理</title>
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
				
				// 基础档案信息表格
				$('#archives_grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					toolbar : toolbar,
					url : "${pageContext.request.contextPath}/data/archives.json",
					idField : 'id',
					columns : columns
				});
				
				// 子档案信息表格
				$('#sub_archives_grid').datagrid( {
					iconCls : 'icon-forward',
					fit : true,
					border : false,
					rownumbers : true,
					striped : true,
					pageList: [30,50,100],
					pagination : true,
					url : "${pageContext.request.contextPath}/data/sub_archives.json",
					idField : 'id',
					columns : child_columns
				});
			});	
			
			//工具栏
			var toolbar = [ {
				id : 'button-add',
				text : '增加',
				iconCls : 'icon-add',
				handler : function(){
					alert('增加');
				}
			}, {
				id : 'button-edit',
				text : '修改',
				iconCls : 'icon-edit',
				handler : function(){
					alert('修改');
				}
			},{
				id : 'button-save',
				text : '保存',
				iconCls : 'icon-save',
				handler : function(){
					alert('保存');
				}
			} ];
			
			// 定义列
			var columns = [ [ {
				field : 'id',
				title : '基础档案编号',
				width : 120,
				align : 'center'
			},{
				field : 'archiveName',
				title : '基础档案名称',
				width : 120,
				align : 'center'
			}, {
				field : 'hasChild',
				title : '是否分级',
				width : 120,
				align : 'center'
			}, {
				field : 'remark',
				title : '备注',
				width : 300,
				align : 'center'
			} ] ];
			
			var child_columns = [ [ {
				field : 'id',
				title : '档案编码',
				width : 120,
				align : 'center'
			},{
				field : 'subArchiveName',
				title : '档案名称',
				width : 120,
				align : 'center'
			}, {
				field : 'archive.id',
				title : '上级编码',
				width : 120,
				align : 'center'
			}, {
				field : 'mnemonicCode',
				title : '助记码',
				width : 120,
				align : 'center'
			}, {
				field : 'mothballed',
				title : '封存',
				width : 120,
				align : 'center'
			}, {
				field : 'remark',
				title : '备注',
				width : 300,
				align : 'center'
			} ] ];
		</script>
	</head>
	
	<body class="easyui-layout" style="visibility:hidden;">
		<div region="center">
			<table id="archives_grid"></table>
		</div>
		<div region="south" style="height: 250px;">
			<table id="sub_archives_grid"></table>
		</div>
	</body>
</html>