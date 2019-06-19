<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-14
  Time: 오후 2:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@ page import="org.openpaas.paasta.marketplace.web.seller.common.Constants" %>--%>
<html>
<head>
    <meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
    <title>상품 수정</title>

    <%@include file="../common/commonLibs.jsp" %>
</head>
<body>
<form id="frm">
    <table class="board_detail" border="1">
   		<tr>
   			<td>카테고리명</td>
   			<td>
   				<select onchange="selectBox();" id="categoryId" name="categoryId"></select>
   			</td>
   		</tr>
   		<tr>
   			<td>버전정보</td>
   			<td><span class="versionInfo"></span></td>
   		</tr>
   		<tr>
   			<td>상품명</td>
    		<td><span class="productName"></span></td>
   		</tr>
   		<tr>
   			<td>아이콘</td>
    		<td>
    			<span class="iconFile"></span>
    			<input type="file" id="iconFile" name="iconFile">
    		</td>
   		</tr>
   		<tr>
   			<td>스크린샷</td>
    		<td>
    			<span class="screenshotFiles"></span>
    			<input type="file" id="screenshotFiles" name="screenshotFiles" multiple="multiple">
    		</td>
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
    		<td><span class="productType"></span></td>
   		</tr>
   		<tr>
   			<td>상품파일</td>
    		<td><span class="productFile"></span></td>
   		</tr>
   		<tr>
   			<td>환경파일</td>
    		<td><span class="envFile"></span></td>
   		</tr>
   		<tr>
   			<td>미터링유형</td>
   			<td><span class="meteringType"></span></td>
   		</tr>
   		<tr>
   			<td>미터링금액</td>
    		<td><input type="text" id="unitPrice" name="unitPrice" value="1000"></td>
   		</tr>
   		<tr>
   			<td>전시여부</td>
    		<td>
    			<input type="radio" id="displayY" name="displayYn" value="Y">전시
    			<input type="radio" id="displayN" name="displayYn" value="N">미전시
    		</td>
   		</tr>
   		<tr>
   			<td>등록일자</td>
    		<td><span class="createDate"></span></td>
   		</tr>
   	</table>
   	<input type='hidden' id="_csrf" name="_csrf" value="${_csrf.token}">
   	<input type='hidden' id="_csrf_header" name="_csrf_header" value="${_csrf.headerName}">
</form>
<div>
	<button type="button" onclick="updateProduct();">수정하기</button>
</div>
</body>
</html>
<script type="text/javascript">

    var orgCategory;
    var categoryValue;

    var getProduct = function () {
        var reqUrl = "<%= SellerConstants.URI_DB_SELLER_PRODUCT_DETAIL %>".replace("{id}", "<c:out value='${id}'/>");
        console.log("url 은 ???" + reqUrl);

        procCallAjax(reqUrl, "GET", null, null, callbackGetProduct);
    };

    var callbackGetProduct = function(data){
        console.log("상품 정보는 ::: " + JSON.stringify(data));

        $('#categoryId').val(data.category.categoryName);
        orgCategory = data.category.id;
        $('.versionInfo').html(data.versionInfo);
        $('.productName').html(data.productName);
        $('.iconFile').html(data.iconFileName);
        var htmlString = [];
        for (var i = 0; i < data.screenshots.length; i++) {
        	htmlString.push(data.screenshots[i].screenshotFileName);
        }
        $('.screenshotFiles').html(htmlString);
        $('#simpleDescription').html(data.simpleDescription);
        $('#detailDescription').html(data.detailDescription);
        $('.productType').html(data.productType);
        $('.productFile').html(data.productFileName);
        $('.envFile').html(data.envFileName);
        $('.meteringType').html(data.meteringType);
        $('#unitPrice').html(data.unitPrice);
        if (data.displayYn === 'Y') {
        	$('#displayY').prop("checked", true);
        } else {
        	$('#displayN').prop("checked", true);
        }
        $('.createDate').html(data.strCreateDate);
    };

    var getCategory = function () {
        var reqUrl = "<%= SellerConstants.URI_DB_CATEGORY_LIST %>";

        procCallAjax(reqUrl, "GET", null, null, callbackGetCategory);
    };

    var callbackGetCategory = function (data) {
        console.log("카테고리 List :::" + JSON.stringify(data));

        var categoryList = data.items;

        var categoryArea = $("#categoryId");
        var htmlString = [];

        var option = "<option>선택</option>";
        for(var i = 0; i < categoryList.length; i++){
            if(orgCategory === categoryList[i].id) {
                option += "<option selected='selected' value=" + categoryList[i].id + ">" + categoryList[i].categoryName + "</option>";
            } else {
                option += "<option value=" + categoryList[i].id + ">" + categoryList[i].categoryName + "</option>"
            }
        }

        htmlString.push(option);
        categoryArea.html(htmlString);
    };

    var selectBox = function () {
    	categoryValue = $("#categoryId option:selected").val();
        console.log("선택된 값은? " + categoryValue);
    };
    
    var updateProduct = function () {
        var reqUrl = "<%= SellerConstants.URI_WEB_SELLER_PRODUCT_UPDATE %>".replace("{id}", "<c:out value='${id}'/>");
        var form = $('#frm')[0];
        var formData = new FormData(form);

        $.ajax({
        	url : reqUrl,
        	type : 'PUT',
        	data : formData,
        	contentType : false,
        	processData : false
        }).done(function(data) {
        	console.log("update 완료!!! ");
            if(data.resultCode === "SUCCESS") {
                procMovePage("<%= SellerConstants.URI_WEB_SELLER_PRODUCT_DETAIL %>".replace("{id}", data.id));
            } else {
            	alert("오류 발생!!! : [" + data.resultMessage + "]");
            	return;
            }
        });
    };


    // ON LOAD
    $(document).ready(function() {
        getProduct();
        getCategory();
    });

</script>