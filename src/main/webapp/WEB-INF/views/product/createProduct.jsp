<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
	<title>상품 등록</title>

	<%@include file="../common/commonLibs.jsp" %>
</head>
<body>
<form id="frm" method="post" action="<%= SellerConstants.URI_WEB_SELLER_PRODUCT_CREATE %>" enctype="multipart/form-data">
	<table class="board_detail" border="1">
   		<tr>
   			<td>카테고리ID</td>
   			<td>
   				<select onchange="selectBox();" id="categoryId" name="categoryId"></select>
   			</td>
   		</tr>
   		<tr>
   			<td>버전정보</td>
   			<td><input type="text" id="versionInfo" name="versionInfo" value="2.0"></td>
   		</tr>
   		<tr>
   			<td>상품명</td>
    		<td><input type="text" id="productName" name="productName" value="test"></td>
   		</tr>
   		<tr>
   			<td>아이콘</td>
    		<td><input type="file" id="iconFile" name="iconFile"></td>
   		</tr>
   		<tr>
   			<td>스크린샷</td>
    		<td><input type="file" id="screenshotFiles" name="screenshotFiles" multiple="multiple"></td>
   		</tr>
   		<tr>
   			<td>상품개요</td>
   			<td><input type="text" id="simpleDescription" name="simpleDescription" value="simple test"></td>
   		</tr>
   		<tr>
   			<td>상품상세</td>
    		<td><input type="text" id="detailDescription" name="detailDescription" value="detail test"></td>
   		</tr>
   		<tr>
   			<td>상품유형</td>
    		<td><input type="text" id="productType" name="productType" value="WEB"></td>
   		</tr>
   		<tr>
   			<td>상품파일</td>
    		<td><input type="file" id="productFile" name="productFile"></td>
   		</tr>
   		<tr>
   			<td>환경파일</td>
    		<td><input type="file" id="envFile" name="envFile"></td>
   		</tr>
   		<tr>
   			<td>미터링유형</td>
   			<td><input type="text" id="meteringType" name="meteringType" value="DAY"></td>
   		</tr>
   		<tr>
   			<td>미터링금액</td>
    		<td><input type="text" id="unitPrice" name="unitPrice" value="1000"></td>
   		</tr>
   		<tr>
   			<td>전시여부</td>
    		<td>
    			<input type="radio" id="displayYn" name="displayYn" value="Y" checked="checked">전시
    			<input type="radio" id="displayYn" name="displayYn" value="N">미전시
    		</td>
   		</tr>
   	</table>
   	<input type='hidden' id="_csrf" name="_csrf" value="${_csrf.token}">
   	<input type='hidden' id="_csrf_header" name="_csrf_header" value="${_csrf.headerName}">
   	<input type='submit' id="submit" value="등록하기">
</form>
</body>
</html>
<script type="text/javascript">

	var cateogryValue;
	
	var getCategory = function () {
	    var reqUrl = "<%= SellerConstants.URI_DB_CATEGORY_LIST %>";
	
	    procCallAjax(reqUrl, "GET", null, null, callbackGetCategoryList);
	};
	
	var callbackGetCategoryList = function (data) {
	    console.log("카테고리 목록 :::" + JSON.stringify(data));
	
	    var categoryList = data.items;
	
	    var categoryArea = $("#categoryId");
	    var htmlArray = [];
	    var option = "<option selected='selected'>선택</option>";
	
	    for(var i = 0; i < categoryList.length; i++){
	        option += "<option value=" + categoryList[i].id + ">" + categoryList[i].categoryName + "</option>"
	    }
	
	    htmlArray.push(option);
	    categoryArea.html(htmlArray);
	};
	
	var selectBox = function () {
		cateogryValue = $("#categoryId option:selected").val();
	  	console.log("선택된 카테고리는? " + cateogryValue);
	};


	// ON LOAD
    $(document).ready(function() {
    	getCategory();
    });

</script>