<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>到达时间录入</title>
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
				// 到达时间录入			

				$("#grid").datagrid({
					columns: [
						[{
							field: 'id',
							title: '编号',
							width: 100,
							checkbox: true
						}, {
							field: 'transitNum',
							title: '交接单号',
							width: 100
						}, {
									field: 'arrivePoint',
									title: '到达口岸',
									width: 100
								}, {
									field: 'transitType',
									title: '配载方式',
									width: 100
								}, {
									field: 'busTime',
									title: '航班车次',
									width: 100
								}, {
									field: 'ticketsNum',
									title: '货票号',
									width: 100
								}, {
									field: 'leaveTime',
									title: '预计离港时间',
									width: 130
								}, {
									field: 'planarriveTime',
									title: '预计到港时间',
									width: 130
								}, {
									field: 'realarriveTime',
									title: '实际到港时间',
									width: 200
								}, {
									field: 'recorder',
									title: '录入人',
									width: 100
								}, {
									field: 'recordTime',
									title: '录入时间',
									width: 100
								}]
					],
					pagination: true,
					toolbar: [{
								id: 'searchBtn',
								text: '查询',
								iconCls: 'icon-search'
							}, {
								id: 'saveBtn',
								text: '保存',
								iconCls: 'icon-save',
								handler: function() {
									alert('保存成功');
								}
							}, {
								id: 'refreshBtn',
								text: '刷新',
								iconCls: 'icon-reload',
								handler : function(){
								$("#grid").datagrid('reload');						
							}

							}, {
								id: 'printBtn',
								text: '打印',
								iconCls: 'icon-print',
								handler: function() {
									alert('要打印吗');
								}
							}, {
								id: 'backBtn',
								text: '返回',
								iconCls: 'icon-back',

							}]
				});
			});
		</script>
	</head>

	<body class="easyui-layout">
		<div region="center">
			<table id="grid"></table>
		</div>
		
	</body>

</html>