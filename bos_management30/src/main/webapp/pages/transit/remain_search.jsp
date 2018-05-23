<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>库存查询</title>
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
				// 库存查询			

				$("#grid").datagrid({
					columns: [
						[{
							field: 'id',
							title: '编号',
							width: 100,
							checkbox: true
						}, {
							field: 'status',
							title: '状态',
							width: 100
						}, {
							field: 'operateUnit',
							title: '操作单位',
							width: 100
						}, {
							field: 'connectOrder',
							title: '交接单号',
							width: 100
						}, {
							field: 'workOrder',
							title: '工作单号',
							width: 100
						}, {
							field: 'remainNum',
							title: '在库件数',
							width: 100
						}, {
							field: 'realNum',
							title: '实际件数',
							width: 100
						}, {
							field: 'weight',
							title: '重量',
							width: 100
						}, {
							field: 'remainer',
							title: '入库人',
							width: 100
						}, {
							field: 'remainTime',
							title: '入库时间',
							width: 100
						}, {
							field: 'operateTime',
							title: '操作时间',
							width: 100
						}, {
							field: 'direction',
							title: '方向',
							width: 100
						}]
					],
					pagination: true,
					toolbar: [{
							id: 'searchBtn',
							text: '查询',
							iconCls: 'icon-search'
						}, {
							id: 'reloadBtn',
							text: '刷新',
							iconCls: 'icon-reload',
							
						}, {
							id: 'printBtn',
							text: '打印',
							iconCls: 'icon-print',
							handler: function() {
								alert('要打印吗？');
							}
						}, {
							id: 'backBtn',
							text: '返回',
							iconCls: 'icon-back',
							
						}
						
					]
				});
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div region="center" style="height:500px">
			<table id="grid"></table>
		</div>
		
	</body>

</html>