<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>合包查询</title>
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
			$(function() {
				$("body").css({
					visibility: "visible"
				});
				// 合包查询
				// 信息表格一
				$('#compose_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					toolbar: toolbar,
					idField: 'id',
					columns: columns
				});

				// 信息表格二
				$('#composechild_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					idField: 'id',
					columns: child_columns
				});
			});

			//定义列
			var columns = [
				[{
					field: 'id',
					title: '合包编号',
					width: 100,
					checkbox: true
				},{
					field: 'packNum',
					title: '合包号',
					width: 100					
				},{
					field: 'pitchNum',
					title: '笼车号',
					width: 100					
				},{
					field: 'packageUnit',
					title: '合包单位',
					width: 100					
				},{
					field: 'pieceUser',
					title: '拆包人',
					width: 100					
				},{
					field: 'pieceUnit',
					title: '拆包单位',
					width: 100				
				},{
					field: 'pieceTime',
					title: '拆包时间',
					width: 100					
				},{
					field: 'recorder',
					title: '记录人',
					width: 100					
				},{
					field: 'recordTime',
					title: '记录时间',
					width: 100					
				}]
			];
			//定义列
			var child_columns = [
				[{
					field: 'id',
					title: '条形编号',
					width: 100,
					checkbox: true
				},{
					field: 'shapeNum',
					title: '条码号',
					width: 100					
				}]
			]
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">
		<div region="north" style="height:31px;overflow:hidden;" split="false">
			<div class="datagrid-toolbar">
				
				<a id="edit" icon="icon-search" href="#" class="easyui-linkbutton" plain="true">查询</a>
				<a id="edit" icon="icon-reload" href="#" class="easyui-linkbutton" plain="true">刷新</a>
				<a id="edit" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印</a>
				<a id="edit" icon="icon-back" href="#" class="easyui-linkbutton" plain="true">返回</a>

			</div>
		</div>
		<div region="west" icon="icon-forward" style="width:880px;overflow:auto;" split="false">
			<table id="compose_grid"></table>
		</div>
		<div region="center" style="overflow:auto;">
			<table id="composechild_grid"></table>
		</div>
		<div region="south" style="overflow:auto;height:30px">
			<input type="checkbox">统计
		</div>

	</body>

</html>