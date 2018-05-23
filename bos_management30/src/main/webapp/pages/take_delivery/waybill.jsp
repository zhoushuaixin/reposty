<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta charset="UTF-8">
		<title>运单录入</title>
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
				$("#no").click(function(){
					$("#waybillForm").form("reset");
					$("input[name='wayBillNum']").attr("readonly",false);
					$("input[name='order.orderNum']").attr("readonly",false);
				})
				
				// 自定义jquery的方法，将Form表单中的内容转换成json
//				$.fn.serializeJson=function(){  
//		            var serializeObj={};  
//		            var array=this.serializeArray();  
//		            $(array).each(function(){  
//		                if(serializeObj[this.name]){  
//		                    if($.isArray(serializeObj[this.name])){  
//		                        serializeObj[this.name].push(this.value);  
//		                    }else{  
//		                        serializeObj[this.name]=[serializeObj[this.name],this.value];  
//		                    }  
//		                }else{  
//		                    serializeObj[this.name]=this.value;   
//		                }  
//		            });  
//		            return serializeObj;  
//		        }; 

				// 对save按钮条件 点击事件
				$('#save').click(function() {
					// 对form 进行校验
					if($('#waybillForm').form('validate')) {
						// 使用ajax完成提交
						var params = $("#waybillForm").serialize();
						// alert(params);
						$.post("${pageContext.request.contextPath}/wayBill_save.action",params,function(data){
							// 响应的结果
							if(data.success){
								$.messager.show({
									title:'运单消息',
									msg:data.msg,
									timeout:5000, // 毫秒
									// showType：定义将如何显示该消息。可用值有：null,slide,fade,show。默认：slide。
									showType:'fade'
								});
								// 清空表单
								$("#waybillForm").form("clear");
								// 输入框的只读设置可写
								$("input[name='wayBillNum']").attr("readonly",false);
								$("input[name='order.orderNum']").attr("readonly",false);
							}
							else{
								$.messager.show({
									title:'运单消息',
									msg:data.msg,
									timeout:5000, // 毫秒
									// showType：定义将如何显示该消息。可用值有：null,slide,fade,show。默认：slide。
									showType:'fade'
								});
							}
						})
					}
				});
				
				/**输入订单号，使用订单号完成订单数据的回显，用于保存运单*/
				$("#orderNum").blur(function(){
					// 如果订单号的输入框已经是只读，此时不往下执行
					if($("#orderNum").attr("readonly")){
						return;
					}
					// 输入订单号，使用订单号完成订单数据的回显
					var orderNum = $(this).val();
					// alert(orderNum);
					$.post("${pageContext.request.contextPath}/order_findByOrderNum.action",{"orderNum":orderNum},function(data){
						// 存在数据，完成回显
						if(data.success){
							// 读取记录填充到表单中。
							$("#waybillForm").form("load",data.orderData);
							// 没有回显的内容，完成回显
							if(data.orderData.courier!=null){
								$("input[name='order.courier.company']").val(data.orderData.courier.company);
								$("input[name='order.courier.name']").val(data.orderData.courier.name);
							}
							else{
								$("input[name='order.courier.company']").val("");
								$("input[name='order.courier.name']").val("");
							}
							/**
							 * <input type="hidden" name="order.id" id="orderId" /><!--存放订单id-->
							   <input type="hidden" name="id" id="id" /><!--存放运单id-->
							 */
							$("input[name='order.id']").val(data.orderData.id);
							// 订单回显的时候，新增一个运单，把隐藏域设置为null，此时才能完成保存
							$("input[name='id']").val(""); 
							// 生成一个运单号（唯一），用作保存
							$("#wayBillNum").val(data.orderData.wayBill.wayBillNum);
							// 设置订单号的输入框为只读
							$("#orderNum").attr("readonly",true);
							// 设置运单号的输入框为只读
							$("#wayBillNum").attr("readonly",true);
						}
						// 不存在数据，清空表单
						else{
							// 清除表单数据。
							$("#waybillForm").form("clear");
						}
					});
				})
				
				/**输入运单号，使用运单号完成运单数据的回显，用于更新运单*/
				$("#wayBillNum").blur(function(){
					
					// 输入运单号，使用运单号完成运单数据的回显
					var wayBillNum = $(this).val();
					$.post("${pageContext.request.contextPath}/wayBill_findByWayBillNum.action",{"wayBillNum":wayBillNum},function(data){
						// 存在数据，完成回显
						if(data.success){
							// 读取记录填充到表单中。
							$("#waybillForm").form("load",data.wayBillData);
							// 回显订单的内容
							if(data.wayBillData.order!=null){
								// 设置订单号，和隐藏域的订单id
								$("input[name='order.orderNum']").val(data.wayBillData.order.orderNum);
								$("input[name='order.id']").val(data.wayBillData.order.id);
								if(data.wayBillData.order.courier!=null){
									$("input[name='order.courier.company']").val(data.wayBillData.order.courier.company);
									$("input[name='order.courier.name']").val(data.wayBillData.order.courier.name);
								}
								else{
									$("input[name='order.courier.company']").val("");
									$("input[name='order.courier.name']").val("");
								}
							}
							else{
								$("input[name='order.orderNum']").val("");
								$("input[name='order.id']").val("");
							}
							// 设置订单号的输入框为只读
							$("#orderNum").attr("readonly",true);
							// 设置运单号的输入框为只读
							$("#wayBillNum").attr("readonly",true);
							
						}
						// 不存在数据，清空表单
						else{
							// 清除表单数据。
							$("#waybillForm").form("clear");
						}
					});
				})
			});
		</script>
	</head>

	<body>
		<div class="datagrid-toolbar">
			<a id="add" icon="icon-add" href="#" class="easyui-linkbutton" plain="true">新增</a>
			<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
			<a id="no" icon="icon-no" href="#" class="easyui-linkbutton" plain="true">取消</a>
		</div>
		<div style="width:95%;margin:10px auto">
			<form id="waybillForm" method="post" 
				action="${pageContext.request.contextPath}/waybill_save.action" >
			<div class="table-top">
				<table class="table-edit" width="95%">
					<tr class="title">
						<td colspan="6">单号信息</td>
					</tr>
					<tr>
						<td>订单号</td>
						<td>
							<input type="hidden" name="order.id" id="orderId" /><!--存放订单id-->
							<input type="hidden" name="id" id="id" /><!--存放运单id-->
							<input type="text" name="order.orderNum" id="orderNum" style="width:300px"/>
						</td>
						<td>运单号</td>
						<td>
							<input type="text" name="wayBillNum" id="wayBillNum" style="width:400px"/>
						</td>
					</tr>
					<tr>
						<td>到达地</td>
						<td><input type="text" name="arriveCity" required="true" /></td>
						<td>产品时限</td>
						<td>
							<select class="easyui-combobox" name="sendProNum">
									<option value="速运当日">速运当日</option>
									<option value="速运次日">速运次日</option>
									<option value="速运隔日">速运隔日</option>
								</select>
						</td>
						<td>配载要求</td>
						<td>
							<select class="easyui-combobox" name="floadreqr">
									<option value="无">无</option>
									<option value="禁航空">禁航空</option>
									<option value="禁铁路航空">禁铁路航空</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>受理单位</td>
						<td><input type="text" name="order.courier.company" required="true" /></td>
						<td>快递员</td>
						<td><input type="text" name="order.courier.name" required="true" /></td>
					</tr>
				</table>
			</div>
			<div class="table-center" style="margin-top:15px">
				<div class="col-md-5">
					<table class="table-edit send" width="95%">
						<tr class="title">
							<td colspan="4">寄件人信息</td>
						</tr>
						<tr>
							<td>寄件人</td>
							<td><input type="text" name="sendName" required="true" /></td>
							<td>地址</td>
							<td><input type="text" name="sendAddress" required="true" /></td>
						</tr>
						<tr>
							<td>公司</td>
							<td><input type="text" name="sendCompany" required="true" /></td>
							<td>电话</td>
							<td><input type="text" name="sendMobile" required="true" /></td>
						</tr>
					</table>

					<table class="table-edit receiver" width="95%">
						<tr class="title">
							<td colspan="4">收件人信息</td>
						</tr>
						<tr>
							<td>收件人</td>
							<td><input type="text" name="recName" required="true" /></td>
							<td>地址</td>
							<td><input type="text" name="recAddress" required="true" /></td>
						</tr>
						<tr>
							<td>公司</td>
							<td><input type="text" name="recCompany" required="true" /></td>
							<td>电话</td>
							<td><input type="text" name="recMobile" required="true" /></td>
						</tr>
					</table>
					<table class="table-edit number" width="95%">
						<tr class="title">
							<td colspan="4">货物信息</td>
						</tr>
						<tr>
							<td>原件数</td>
							<td><input type="text" name="num" required="true" /></td>
							<td>实际重量</td>
							<td><input type="text" name="actlweit" required="true" /></td>
						</tr>

						<tr>
							<td>货物</td>
							<td><input type="text" name="goodsType" required="true" /></td>
							<td>体积</td>
							<td><input type="text" name="vol" required="true" /></td>
						</tr>
					</table>
				</div>
				<div class="col-md-7">
					<table class="table-edit security" width="95%">
						<tr class="title">
							<td colspan="6">包装信息</td>
						</tr>
						<tr>
							<td>保险类型</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">不保险</option>
									<option value="1">委托投保</option>
									<option value="2">自带投保</option>
									<option value="3">保价</option>
								</select>
							</td>
							<td>保险费</td>
							<td><input type="text" name="secuityprice" required="true" /></td>
							<td>声明价值</td>
							<td><input type="text" name="value" required="true" /></td>
						</tr>

						<tr>
							<td>原包装</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">木箱</option>
									<option value="1">纸箱</option>
									<option value="2">快递袋</option>
									<option value="3">其他</option>
								</select>
							</td>
							<td>实际包装</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">木箱</option>
									<option value="1">纸箱</option>
									<option value="2">快递袋</option>
									<option value="3">其他</option>
								</select>
							</td>
						</tr>
						<tr>
							<td>包装费</td>
							<td><input type="text" name="packageprice" required="true" /></td>
							<td>包装要求</td>
							<td><input type="text" name="packageprice" required="true" /></td>
						</tr>
					</table>

					<table class="table-edit max" width="95%">
						<tr>
							<td>实际件数</td>
							<td><input type="text" name="realNum" required="true" /></td>
							<td>计费重量</td>
							<td><input type="text" name="priceWeight" required="true" /></td>
						</tr>
						<tr>
							<td>预收费</td>
							<td><input type="text" name="planprice" required="true" /></td>
							<td><button class="btn btn-default">计算</button></td>
						</tr>
					</table>

					<table class="table-edit money" width="95%">
						<tr class="title">
							<td colspan="6">计费信息</td>
						</tr>
						<tr>
							<td>结算方式</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">现结</option>
									<option value="1">代付</option>
									<option value="2">网络</option>
									
								</select>
							</td>
							<td>代收款</td>
							<td><input type="text" name="priceWeight" required="true" /></td>
							<td>到付款</td>
							<td><input type="text" name="priceWeight" required="true" /></td>
						</tr>

					</table>
					<table class="table-edit feedback" width="95%">
						<tr class="title">
							<td colspan="4">配送信息</td>
						</tr>
						<tr>
							<td><input type="checkbox">反馈签收</td>
							<td><input type="checkbox">节假日可收货</td>
							<td>送达时限</td>
							<td><input type="text" class="easyui-datebox" data-options="editable:false" /></td>
						</tr>

						<tr>
							<td>处理方式</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">无</option>
									<option value="1">不可开箱验货</option>
									<option value="2">开开箱单不可开内包</option>
									<option value="3">可开箱和内包</option>
								</select>
							</td>
							<td>签单返回</td>
							<td>
								<select class="easyui-combobox">
									<option value="0">箱单</option>
									<option value="1">原单</option>
									<option value="2">附原单</option>
									<option value="3">网络派送单</option>
								</select>
							</td>
						</tr>
					</table>
					<table class="table-edit tips" width="95%">
						<tr>
							<td>重要提示</td>
							<td><textarea style="width:250px;height: 80px;"></textarea></td>
						</tr>

					</table>
				</div>
			</div>
			<div class="clearfix"></div>
			</form>			
		</div>
	</body>

</html>