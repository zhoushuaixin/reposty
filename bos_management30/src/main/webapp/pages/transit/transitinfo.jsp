<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>运输配送管理</title>
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

		<!--百度地图api-->
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=bp09g732UD7Kh0UbG5GoaUyMGbMizI0A"></script>
		<style type="text/css">
			#allmap {
				height: 92%;
				width: 100%;
			}
		</style>
		<script type="text/javascript">
			$(function() {
				var allmap;
				var allstart;
				var allend;
				// 先将body隐藏，再显示，不会出现页面刷新效果
				$("body").css({
					visibility: "visible"
				});

				var transitToolbar = [{
					id: 'button-inoutstore',
					text: '出入库',
					iconCls: 'icon-add',
					handler: function() {
						/**
						 *      <input type="hidden" name="transitInfoId" id="inOutStoreId" />
								<span id="inOutStoreTransitInfoView"></span>
						 */
						var row = $("#transitGrid").datagrid("getSelected");
						if(row==null){
							$.messager.alert("警告","必须选择一条流程记录进行查看","warning");
							return;
						}
						else{
							if(row.status != "出入库中转"){
								$.messager.alert("警告","只有是【出入库中转】的状态才能操作出入库","warning");
								return;
							}
							else{
								// 流程信息的保存（出入库、开始配送信息、签收录入），一定用到流程的id
								$("#inOutStoreId").val(row.id);
								// 输出运单的信息存放到span元素中
								$("#inOutStoreTransitInfoView").text(""); // 清空
								$("#inOutStoreTransitInfoView").append("运单号："+row.wayBill.wayBillNum+"<br>");
								$("#inOutStoreTransitInfoView").append("货物类型："+row.wayBill.goodsType+"<br>");
								$("#inOutStoreTransitInfoView").append("寄件人地址："+row.wayBill.sendAddress+"<br>");
								$("#inOutStoreTransitInfoView").append("收件人地址："+row.wayBill.recAddress+"<br>");
								$("#inOutStoreTransitInfoView").append("物流信息："+row.transferInfo+"<br>");
								$("#inoutstoreWindow").window('open');
							}
						}
						
					}
				}, {
					id: 'button-delivery',
					text: '开始配送',
					iconCls: 'icon-print',
					handler: function() {
						/**
						 * <input type="hidden" name="transitInfoId" id="deliveryId" />
						   <span id="deliveryTransitInfoView"></span>
						 */
						var row = $("#transitGrid").datagrid("getSelected");
						if(row==null){
							$.messager.alert("警告","必须选择一条流程记录进行查看","warning");
							return;
						}
						else{
							if(row.status != "到达网点"){
								$.messager.alert("警告","只有是【到达网点】的状态才能操作开始配送","warning");
								return;
							}
							else{
								
								// 流程信息的保存（出入库、开始配送信息、签收录入），一定用到流程的id
								$("#deliveryId").val(row.id);
								// 输出运单的信息存放到span元素中
								$("#deliveryTransitInfoView").text(""); // 清空
								$("#deliveryTransitInfoView").append("运单号："+row.wayBill.wayBillNum+"<br>");
								$("#deliveryTransitInfoView").append("货物类型："+row.wayBill.goodsType+"<br>");
								$("#deliveryTransitInfoView").append("寄件人地址："+row.wayBill.sendAddress+"<br>");
								$("#deliveryTransitInfoView").append("收件人地址："+row.wayBill.recAddress+"<br>");
								$("#deliveryTransitInfoView").append("物流信息："+row.transferInfo+"<br>");
								$("#deliveryWindow").window('open');
							}
						}
						
					}
				}, {
					id: 'button-sign',
					text: '签收录入',
					iconCls: 'icon-save',
					handler: function() {
						/**
						 * <input type="hidden" name="transitInfoId" id="signId" />
						   <span id="signTransitInfoView"></span>
						 */
						var row = $("#transitGrid").datagrid("getSelected");
						if(row==null){
							$.messager.alert("警告","必须选择一条流程记录进行查看","warning");
							return;
						}
						else{
							if(row.status != "开始配送"){
								$.messager.alert("警告","只有是【开始配送】的状态才能操作签收录入","warning");
								return;
							}
							else{
								
								// 流程信息的保存（出入库、开始配送信息、签收录入），一定用到流程的id
								$("#signId").val(row.id);
								// 输出运单的信息存放到span元素中
								$("#signTransitInfoView").text(""); // 清空
								$("#signTransitInfoView").append("运单号："+row.wayBill.wayBillNum+"<br>");
								$("#signTransitInfoView").append("货物类型："+row.wayBill.goodsType+"<br>");
								$("#signTransitInfoView").append("寄件人地址："+row.wayBill.sendAddress+"<br>");
								$("#signTransitInfoView").append("收件人地址："+row.wayBill.recAddress+"<br>");
								$("#signTransitInfoView").append("物流信息："+row.transferInfo+"<br>");
								$("#signWindow").window('open');
							}
						}
						
					}
				}, {
					id: 'button-transit',
					text: '运输路径',
					iconCls: 'icon-search',
					handler: function() {
						var row = $("#transitGrid").datagrid("getSelected");
						if(row==null){
							$.messager.alert("警告","必须选择一个运输路程，查看运输路径","warning");
							return;
						}
						// 地图显示
						// 百度地图API功能
						var map = new BMap.Map("allmap");
						var start = row.wayBill.sendAddress;
						var end = row.wayBill.recAddress;
						map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
						map.enableScrollWheelZoom();   //启用滚轮放大缩小，默认禁用
						map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
						
						//三种驾车策略：最少时间，最短距离，避开高速
						var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
						
						var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: true},policy: routePolicy[0]});
						driving.search(start,end);
						
						
						// 弹出窗口
						$("#transitPathWindow").window('open');
						
						allmap = map;
						allstart = start;
						allend = end;
					}
				}, {
					id: 'button-path',
					text: '查看配送信息',
					iconCls: 'icon-search',
					handler: function() {
						/**
						 * <span id="transitInfoView"></span>
						 */
						var row = $("#transitGrid").datagrid("getSelected");
						
						// 输出运单的信息存放到span元素中
						$("#transitInfoView").text(""); // 清空
						$("#transitInfoView").append("运单号："+row.wayBill.wayBillNum+"<br>");
						$("#transitInfoView").append("货物类型："+row.wayBill.goodsType+"<br>");
						$("#transitInfoView").append("寄件人地址："+row.wayBill.sendAddress+"<br>");
						$("#transitInfoView").append("收件人地址："+row.wayBill.recAddress+"<br>");
						$("#transitInfoView").append("物流信息："+row.transferInfo+"<br>");
						$("#deliveryInTimePathWindow").window('open');
					}
				}];

				var transitColumns = [
					[{
						field: 'id',
						title: '编号',
						align: 'center',
						width: 30
					}, {
						field: 'wayBillNum',
						title: '运单号',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.wayBillNum;
							}
						}
					}, {
						field: 'sendName',
						title: '寄件人姓名',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.sendName;
							}
						}

					}, {
						field: 'sendAddress',
						title: '寄件地址',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.sendAddress;
							}
						}
					}, {
						field: 'recName',
						title: '收件人姓名',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.recName;
							}
						}
					}, {
						field: 'recAddress',
						title: '收件地址',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.recAddress;
							}
						}
					}, {
						field: 'goodsType',
						title: '托寄物',
						width: 100,
						align: 'center',
						formatter: function(data, row, index) {
							if(row.wayBill != null) {
								return row.wayBill.goodsType;
							}
						}
					}, {
						field: 'status',
						title: '运输状态',
						align: 'center',
						width: 100
					}, {
						field: 'outletAddress',
						title: '网点地址',
						align: 'center',
						width: 100
					}, {
						field: 'transferInfo',
						title: '物流信息',
						align: 'center',
						width: 260
					}]
				];

				// 运输配送管理 表格定义 
				$('#transitGrid').datagrid({
					iconCls: 'icon-forward',
					// url: '${pageContext.request.contextPath}/data/transit.json',
					url : '${pageContext.request.contextPath}/transit_pageQuery.action',
					fit: true,
					border: false,
					rownumbers: true,
					striped: true,
					pageList: [20, 50, 100],
					pagination: true,
					idField: 'id',
					singleSelect: true,
					toolbar: transitToolbar,
					columns: transitColumns
				});
				// 出入库保存按钮点击事件
				$("#inoutStoreSave").click(function() {
					if($("#inoutStoreForm").form('validate')) {
						$("#inoutStoreForm").submit();
					}
				});

				// 开始配送保存按钮点击事件
				$("#deliverySave").click(function() {
					if($("#deliveryForm").form('validate')) {
						$("#deliveryForm").submit();
					}
				});

				// 签收录入按钮点击事件
				$("#signSave").click(function() {
					if($("#signForm").form('validate')) {
						$("#signForm").submit();
					}
				});
				// 点击查询途经点的事件
				$("#result").click(function(){
					allmap.clearOverlays(); 
					var viapoint =$ ("#viapoint").val(); // 字符串，中间使用逗号分开
					var array = viapoint.split(",");
					var routePolicy = [BMAP_DRIVING_POLICY_LEAST_TIME,BMAP_DRIVING_POLICY_LEAST_DISTANCE,BMAP_DRIVING_POLICY_AVOID_HIGHWAYS];
					search(allstart,allend,routePolicy[0],array); 
					// 定义函数
					function search(start,end,route,array){ 
						var driving = new BMap.DrivingRoute(allmap, {renderOptions:{map: allmap, autoViewport: true},policy: route});
						// 此时没有途经点
						if(array==""){
							driving.search(start,end);
						}
						// 否则有途径点
						else{
							driving.search(start, end,{waypoints:array});//waypoints表示途经点
						}
						
					}
				})
			});
		</script>
	</head>

	<body class="easyui-layout" style="visibility:hidden;">

		<div data-options="region:'center'">
			<table id="transitGrid"></table>
		</div>

		<div class="easyui-window" title="出入库" id="inoutstoreWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="inoutStoreSave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="inoutStoreForm" method="post" action="${pageContext.request.contextPath}/inoutstorage_save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">入库操作</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="inOutStoreId" />
								<span id="inOutStoreTransitInfoView"></span>
						</tr>
						<tr>
							<td>操作</td>
							<td>
								<select name="operation" class="easyui-combobox">
									<option value="入库">入库</option>
									<option value="出库">出库</option>
									<option value="到达网点">到达网点</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>仓库或网点地址</td>
							<td>
								<input type="text" name="address" size="40" />
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="开始配送" id="deliveryWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="deliverySave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="deliveryForm" method="post" action="${pageContext.request.contextPath}/delivery_save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">开始配送</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="deliveryId" />
								<span id="deliveryTransitInfoView"></span>
						</tr>
						<tr>
							<td>快递员工号</td>
							<td>
								<input type="text" required="true" name="courierNum" />
							</td>
						</tr>
						<tr>
							<td>快递员姓名</td>
							<td>
								<input type="text" required="true" name="courierName" />
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="签收录入" id="signWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:100px;left:200px;width: 600px; height: 300px">
			<div region="north" style="height:30px;overflow:hidden;" split="false" border="false">
				<div class="datagrid-toolbar">
					<a id="signSave" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				</div>
			</div>
			<div region="center" style="overflow:auto;padding:5px;" border="false">
				<form id="signForm" method="post" action="${pageContext.request.contextPath}/sign_save.action">
					<table class="table-edit" width="80%" align="center">
						<tr class="title">
							<td colspan="2">签收录入</td>
						</tr>
						<tr>
							<td>运单信息</td>
							<td>
								<input type="hidden" name="transitInfoId" id="signId" />
								<span id="signTransitInfoView"></span>
						</tr>
						<tr>
							<td>签收人</td>
							<td>
								<input type="text" required="true" name="signName" />
							</td>
						</tr>
						<tr>
							<td>签收日期</td>
							<td>
								<input type="text" class="easyui-datebox" required="true" name="signTime" />
							</td>
						</tr>
						<tr>
							<td>签收状态</td>
							<td colspan="3">
								<select class="easyui-combobox" style="width:150px" name="signType">
									<option value="正常">正常</option>
									<option value="返单">返单</option>
									<option value="转发签收">转发签收</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>异常备注</td>
							<td>
								<textarea name="errorRemark" rows="4" cols="40"></textarea>
							</td>
						</tr>
						<tr>
							<td>描述</td>
							<td>
								<textarea rows="3" cols="40" name="description"></textarea>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

		<div class="easyui-window" title="运输路径查看" id="transitPathWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:100px;width: 800px; height: 400px">
			<!--在窗口中显示地图-->
			<div id="allmap"></div>
			<div id="driving_way">
				请输入途经点（用,号分开）：<input type="text" id="viapoint" size="70" value="" />
				<input type="button" id="result" value="查询" />
			</div>
		</div>

		<div class="easyui-window" title="查看配送信息" id="deliveryInTimePathWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:100px;width: 800px; height:400px">
			<span id="transitInfoView"></span>
		</div>
	</body>

</html>