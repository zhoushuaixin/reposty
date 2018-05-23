<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>盘库</title>
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
				// 交接单信息表格
				$('#detail_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					idField: 'id',
					columns: columns
				});
				// 发货信息表格
				$('#realdata_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					idField: 'id',
					columns: realdata_columns
				});
				// 出库交接信息表格				
				$('#outorder_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					idField: 'id',
					columns: outorder_columns
				});
				//合包号信息表格
				$('#composeorder_grid').datagrid({
					iconCls: 'icon-forward',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [30, 50, 100],
					pagination: true,
					idField: 'id',
					columns: composeorder_columns
				});
			});
			// 定义列
			var columns = [
				[{
					field: 'id',
					title: '工作单号',
					width: 100,
					checkbox: true

				}, {
					field: 'composer',
					title: '合包号',
					width: 100
				}, {
					field: 'inorder',
					title: '入库交接单号',
					width: 100
				}, {
					field: 'number',
					title: '件数',
					width: 100
				}, {
					field: 'weight',
					title: '重量',
					width: 100
				}, {
					field: 'equare',
					title: '体积',
					width: 100
				}, {
					field: 'type',
					title: '类型',
					width: 50
				}, {
					field: 'destination',
					title: '到达地',
					width: 100
				}, {
					field: 'iner',
					title: '入库人',
					width: 100
				}, {
					field: 'intime',
					title: '入库时间',
					width: 100
				}]
			];
			// 定义发货信息列
			var realdata_columns = [
				[{
					field: 'id',
					title: '工作单号',
					width: 100,
					checkbox: true

				}, {
					field: 'composer',
					title: '合包号',
					width: 100
				}, {
					field: 'inorder',
					title: '入库交接单号',
					width: 100
				}, {
					field: 'number',
					title: '件数',
					width: 100
				}, {
					field: 'weight',
					title: '重量',
					width: 100
				}, {
					field: 'equare',
					title: '体积',
					width: 100
				}, {
					field: 'type',
					title: '类型',
					width: 50
				}, {
					field: 'destination',
					title: '到达地',
					width: 100
				}, {
					field: 'iner',
					title: '入库人',
					width: 100
				}, {
					field: 'intime',
					title: '入库时间',
					width: 100
				}]
			];

			// 定义出库交接单号列
			var outorder_columns = [
				[{
					field: 'id',
					title: '盘点单号',
					width: 100,
					checkbox: true

				}, {
					field: 'number',
					title: '总件数',
					width: 100
				}, {
					field: 'maker',
					title: '盘库人',
					width: 100
				}, {
					field: 'makertime',
					title: '盘库时间',
					width: 100
				}, {
					field: 'operateTime',
					title: '操作单位',
					width: 100
				}]
			];

			// 定义合包交接单号列
			var composeorder_columns = [
				[{
					field: 'id',
					title: '工作单号',
					width: 100,
					checkbox: true

				}, {
					field: 'composeOrder',
					title: '合包号',
					width: 100
				}, {
					field: 'inlinkorder',
					title: '入库交接单号',
					width: 100
				}, {
					field: 'number',
					title: '件数',
					width: 100
				}, {
					field: 'weight',
					title: '重量',
					width: 100
				}, {
					field: 'equare',
					title: '体积',
					width: 100
				}, {
					field: 'type',
					title: '类型',
					width: 100
				}, {
					field: 'destination',
					title: '到达地',
					width: 100
				}, {
					field: 'iner',
					title: '入库人',
					width: 100
				}, {
					field: 'intime',
					title: '入库时间',
					width: 100
				}]
			];
		</script>
	</head>

	<body>
		<div class="datagrid-toolbar">
			<a id="save" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true">盘库</a>
			<a id="edit" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			<a id="edit" icon="icon-cut" href="#" class="easyui-linkbutton" plain="true">删除</a>
			<a id="edit" icon="icon-cancel" href="#" class="easyui-linkbutton" plain="true">清空</a>
			<a id="edit" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true">对比</a>
			<a id="edit" icon="icon-search" href="#" class="easyui-linkbutton" plain="true">查询</a>
			<a id="edit" icon="icon-reload" href="#" class="easyui-linkbutton" plain="true">刷新</a>
			<a id="edit" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印</a>
			<a id="edit" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印显示</a>
			<a id="edit" href="#" class="easyui-linkbutton" plain="true">首页</a>
			<a id="edit" href="#" class="easyui-linkbutton" plain="true">上一页</a>
			<a id="edit" href="#" class="easyui-linkbutton" plain="true">下一页</a>
			<a id="edit" href="#" class="easyui-linkbutton" plain="true">尾页</a>
			<a id="edit" href="#" class="easyui-linkbutton" plain="true">跳到</a>
		</div>

		<div class="easyui-tabs">
			<div title="卡片">
				<table cellpadding="7" style="padding: 20px;">
					<tr>
						<td>盘点单号：</td>
						<td><input class="easyui-textbox" type="text"></input>
						</td>
						<td>总件数：</td>
						<td><input class="easyui-textbox" type="text"></input>
						</td>
						<td>盘库人：</td>
						<td><input class="easyui-textbox" type="text"></input>
						</td>
						<td>盘库时间：</td>
						<td><input class="easyui-textbox" type="text"></input>
						</td>
						<td>盘库单位：</td>
						<td><input class="easyui-textbox" type="text"></input>
						</td>
					</tr>

				</table>
				<div class="easyui-tabs">
					<div title="账面数据" style="height:300px">

						<table id="detail_grid"></table>
						<p style="float:right;color:#fff">账面数据</p>
					</div>

					<div title="实盘数据">

						<table id="realdata_grid"></table>
						<p style="float:right;color:#fff">实盘数据</p>

					</div>

				</div>
				<table class="table-edit">
					<tr>
						<td><input type="checkbox">追加导入数据</td>
						<td>
							<a href="#" class="easyui-linkbutton">接收数据</a>
						</td>
						<td>
							<a href="#" class="easyui-linkbutton">导入数据</a>
						</td>
						<td><input type="text" name="address" required="true" placeholder="可输入条码/合包号" /></td>
						<td>
							<a href="#" class="easyui-linkbutton">+</a>
						</td>

					</tr>

				</table>

			</div>

			<div title="列表" style="padding:10px">
				<div class="easyui-layout" style="height:750px;">
					<div data-options="region:'north'" style="height:300px">
						<table id="outorder_grid"></table>

					</div>

					<div data-options="region:'center'">

						<table id="receiveorder_grid"></table>

					</div>
				</div>
			</div>

		</div>
	</body>

</html>