<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>添加宣传任务</title>
		<!-- 导入jquery核心类库 -->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.8.3.js"></script>
		<!-- 导入easyui类库 -->
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/default.css">
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
		<script src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
	
		<!--kindEditor文本 编辑器-->
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor/kindeditor.js" ></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/editor/lang/zh_CN.js" ></script>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/js/editor/themes/default/default.css" />
		<script type="text/javascript">
			$(function(){
				$("body").css({visibility:"visible"});
				$("#back").click(function(){
					location.href = "promotion.html";
				});
				// 添加文本编辑器
				KindEditor.ready(function(K) {
		            window.editor = K.create('#description',{
		            	items:[
						        'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
						        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
						        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
						        'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
						        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
						        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
						        'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
						        'anchor', 'link', 'unlink', '|'
						],
						allowFileManager:true,
						uploadJson:"../upload_json.jsp",
					    fileManagerJson:"../file_manager_json.jsp"
						// uploadJson:"${pageContext.request.contextPath}/image_upload.action",
						// fileManagerJson:"${pageContext.request.contextPath}/image_fileManager.action"
		            });
		        });
		        
		        // 点击保存
		        $("#save").click(function(){
		        	if($("#promotionForm").form("validate")){
		        		// 同步数据后可以直接取得textarea的value
						editor.sync();
		        		$("#promotionForm").submit();
		        	}
		        	else{
		        		$.messager.alert("警告","表单校验有误！","warning");
		        	}
		        })
			});
		</script>
	</head>
	<body class="easyui-layout" style="visibility:hidden;">
		<div region="north" style="height:31px;overflow:hidden;" split="false" border="false">
			<div class="datagrid-toolbar">
				<a id="save" icon="icon-save" href="#" class="easyui-linkbutton" plain="true">保存</a>
				<a id="back" icon="icon-back" href="#" class="easyui-linkbutton" plain="true">返回列表</a>
			</div>
		</div>
		<div region="center" style="overflow:auto;padding:5px;" border="false">
			<form id="promotionForm" action="${pageContext.request.contextPath}/promotion_save.action" enctype="multipart/form-data" method="post">
				<table class="table-edit" width="95%" align="center">
					<tr class="title">
						<td colspan="4">宣传任务</td>
					</tr>
					<tr>
						<td>宣传概要(标题):</td>
						<td colspan="3">
							<input type="text" name="title" id="title" class="easyui-validatebox" required="true" />
						</td>
					</tr>
					<tr>
						<td>活动范围:</td>
						<td>
							<input type="text" name="activeScope" id="activeScope" class="easyui-validatebox" />
						</td>
						<td>宣传图片:</td>
						<td>
							<input type="file" name="titleImgFile" id="titleImg" class="easyui-validatebox" required="true"/>
						</td>
					</tr>
					<tr>
						<td>发布时间: </td>
						<td>
							<input type="text" name="startDate" id="startDate" class="easyui-datebox" required="true" />
						</td>
						<td>失效时间: </td>
						<td>
							<input type="text" name="endDate" id="endDate" class="easyui-datebox" required="true" />
						</td>
					</tr>
					<tr>
						<td>宣传内容(活动描述信息):</td>
						<td colspan="3">
							<textarea id="description" name="description" style="width:80%" rows="20"></textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>
