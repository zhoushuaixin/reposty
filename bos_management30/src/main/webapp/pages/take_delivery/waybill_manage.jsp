<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="keywords" content="jquery,ui,easy,easyui,web">
		<meta name="description" content="easyui help you build your web page easily!">
		<title>运单管理</title>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/ext/portal.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">

		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.portal.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/ext/jquery.cookie.js"></script>
		<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
		
		<!--添加highcharts-->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/highcharts-3d.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/highcharts/js/modules/exporting.js" ></script>
		<style type="text/css">
			#container, #sliders {
			    min-width: 310px; 
			    max-width: 800px;
			    margin: 0 auto;
			}
			#container {
			    height: 400px; 
			}
		</style>
		<script type="text/javascript">
			$.fn.serializeJson=function(){  
				var serializeObj={};  
				var array=this.serializeArray();  
				var str=this.serialize();  
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
			function doSearch() {
				// 将form内容，转换为json 
				var queryParams = $("#searchForm").serializeJson();
				
				// 绑定数据表格 重新加载数据 
				$('#tt').datagrid('load', queryParams);
			}
			
			$(function(){
				// 开启运输管理 按钮
				$("#transitBtn").click(function(){
					// 获取数据表格所有选中行 
					var rows = $("#tt").datagrid('getSelections');
					if(rows.length == 0){
						// 没有选中
						$.messager.alert('警告','必须选中运单信息','warning');
					}else{
						// 选中运单 ，获取选中运单id 
						var array = new Array();
						for(var i=0; i<rows.length; i++){
						    if(rows[i].signStatus!=1){  // 运单的状态不是‘待发货’
								continue;
							}
							array.push(rows[i].id); // 运单id
						}
						var wayBillIds = array.join(","); // 运单id中间用逗号分开
						$.post("${pageContext.request.contextPath}/transit_create.action",{wayBillIds: wayBillIds}, function(data){
							// 返回值 {success:true, msg: ...}
							if(data.success == true){
								$.messager.show({
									title:'操作成功',
  									msg: data.msg
								});
							}else{
								$.messager.show({
									title:'操作失败',
  									msg: data.msg
								});
							}
						});
					}
				});
				  
				// 导出Excel 按钮 
				$("#exportXlsBtn").click(function(){
					// 下载效果 
					$("#searchForm").attr("action", "${pageContext.request.contextPath}/report_exportXls.action");
					$("#searchForm").submit();
				});
				
				// 导出 PDF 按钮 
				$("#exportPdfBtn").click(function(){
					// 下载效果 
					$("#searchForm").attr("action", "${pageContext.request.contextPath}/report_exportPdf.action");
					$("#searchForm").submit();
				});
				
				// 结合模板 导出 PDF 按钮 
				$("#exportJasperPdfBtn").click(function(){
					// 下载效果 
					$("#searchForm").attr("action", "${pageContext.request.contextPath}/report_exportJasperPdf.action");
					$("#searchForm").submit();
				});
				
				// 使用highcharts生成柱状图
				$("#exportHighChartBtn").click(function(){
					$.post("${pageContext.request.contextPath}/report_highcharts.action",{},function(data){
						var xname = [];
						var ydata = [];
						if(data!=null && data.length>0){
							for(var i=0;i<data.length;i++){
								xname.push(data[i].name);
								ydata.push(data[i].data);
							}
						}
						// highcharts的配置
						var chart = new Highcharts.Chart({
						    chart: {
						        renderTo: 'container',
						        type: 'column',
						        options3d: {
						            enabled: true,
						            alpha: 15,
						            beta: 15,
						            depth: 50,
						            viewDistance: 25
						        }
						    },
						    title: {
						        text: '运单状态统计报表'
						    },
						    subtitle: {
						        text: '根据运单状态统计'
						    },
						    plotOptions: {
						        column: {
						            depth: 25
						        }
						    },
						    xAxis: {
					            //categories: [name]
					        	categories: xname
					        },
					        yAxis: {
					            min: 0,
					            title: {
					                text: '运单数量'
					            }
					        },
						    series: [{
						    	name: '数量',
						        data: ydata
						    }]
						});
						
						function showValues() {
						    $('#alpha-value').html(chart.options.chart.options3d.alpha);
						    $('#beta-value').html(chart.options.chart.options3d.beta);
						    $('#depth-value').html(chart.options.chart.options3d.depth);
						}
						
						// Activate the sliders
						$('#sliders input').on('input change', function () {
						    chart.options.chart.options3d[this.id] = parseFloat(this.value);
						    showValues();
						    chart.redraw(false);
						});
						
						showValues();
					})
					$("#waybillWindow").window("open");
				})
				
				
			});
		</script>
	</head>

	<body>
		<div id="tb">
			<a id="save" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true">修改</a>
			<a id="cut" icon="icon-cut" href="#" class="easyui-linkbutton" plain="true">作废</a>
			<a id="help" icon="icon-help" href="#" class="easyui-linkbutton" plain="true">任务提示</a>
			<a id="help" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印查询结果</a>
			<a id="help" icon="icon-cancel" href="#" class="easyui-linkbutton" plain="true">取消</a>
			<a id="help" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			<a id="help" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印标签</a>
			<a id="help" icon="icon-print" href="#" class="easyui-linkbutton" plain="true">打印工作单</a>
			<a id="transitBtn" icon="icon-edit" href="#" class="easyui-linkbutton" plain="true">开始中转配送</a>
			<a id="exportXlsBtn" icon="icon-print" href="#" 
				class="easyui-linkbutton" plain="true">导出Excel报表</a>
			<a id="exportPdfBtn" icon="icon-print" href="#" 
				class="easyui-linkbutton" plain="true">导出PDF报表</a>	
			<a id="exportJasperPdfBtn" icon="icon-print" href="#" 
				class="easyui-linkbutton" plain="true">结合模板导出PDF报表</a>	
				
			<a id="exportHighChartBtn" icon="icon-print" href="#" 
				class="easyui-linkbutton" plain="true">运单统计情况</a>	
			
			<br />
			<form id="searchForm" method="post">
				运单号：<input name="wayBillNum" style="line-height:26px;border:1px solid #ccc">
				发货地：<input name="sendAddress" style="line-height:26px;border:1px solid #ccc" >
				收货地：<input name="recAddress" style="line-height:26px;border:1px solid #ccc" >
				
				<select class="easyui-combobox" style="width:150px" name="sendProNum">
					<option value="">请选择快递产品类型</option>
					<option value="速运当日">速运当日</option>
					<option value="速运次日">速运次日</option>
					<option value="速运隔日">速运隔日</option>
				</select>
				
				<select class="easyui-combobox" style="width:150px" name="signStatus">
					<option value="0">请选择运单状态</option>
					<option value="1">待发货</option>
					<option value="2">派送中</option>
					<option value="3">已签收</option>
					<option value="4">异常</option>
				</select>
				
				<a href="javascript:void" class="easyui-linkbutton" 
					plain="true" onclick="doSearch()">查询</a> 
			</form>
		</div>
		<table id="tt" class="easyui-datagrid" url="${pageContext.request.contextPath}/wayBill_pageQuery.action" fit="true" toolbar="#tb" rownumbers="true" pagination="true">
			<thead>
				<tr>
					<th field="ck" checkbox="true"></th>
					<th field="wayBillNum" width="80">运单编号</th>
					<th field="sendName" width="80">寄件人</th>
					<th field="sendMobile" width="80">寄件人电话</th>
					<th field="sendCompany" width="80">寄件人公司</th>
					<th field="sendAddress" width="120">寄件人详细地址</th>
					<th field="recName" width="80">收件人</th>
					<th field="recMobile" width="80">收件人电话</th>
					<th field="recCompany" width="80">收件人公司</th>
					<th field="recAddress" width="120">收件人详细地址</th>
					<th field="sendProNum" width="80">产品类型</th>
					<th field="payTypeNum" width="80">支付类型</th>
					<th field="weight" width="80"> 重量</th>
					<th field="num" width="80"> 原件数</th>
					<th field="feeitemnum" width="80">实际件数</th>
					<th field="actlweit" width="80">实际重量</th>
					<th field="vol" width="80">体积</th>
					<th field="floadreqr" width="80">配载要求</th>
					<th field="wayBillType" width="80">运单类型</th>
				</tr>
			</thead>
		</table>
		<div class="easyui-window" title="运单统计" id="waybillWindow" modal="true" closed="true" collapsible="false" minimizable="false" maximizable="false" style="top:20px;left:100px;width: 800px; height: 400px">
			<div id="container"></div>
		</div>
	</body>

</html>