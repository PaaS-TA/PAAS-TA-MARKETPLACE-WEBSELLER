<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-13
  Time: 오후 1:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ page import="org.openpaas.paasta.marketplace.web.seller.common.Constants" %> --%>
<html>
<head>
    <meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
    <title>상품 상세 조회</title>

    <%@include file="../common/commonLibs.jsp" %>
</head>
<body>
	<table class="board_detail" border="1">
   		<tr>
   			<td>카테고리명</td>
   			<td><span class="categoryName"></span></td>
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
    		<td><span class="iconFile"></span></td>
   		</tr>
   		<tr>
   			<td>스크린샷</td>
    		<td><span class="screenshotFiles"></span></td>
   		</tr>
   		<tr>
   			<td>상품개요</td>
   			<td><span class="simpleDescription"></span></td>
   		</tr>
   		<tr>
   			<td>상품상세</td>
    		<td><span class="detailDescription"></span></td>
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
    		<td><span class="unitPrice"></span></td>
   		</tr>
   		<tr>
   			<td>전시여부</td>
    		<td><span class="displayYn"></span></td>
   		</tr>
   		<tr>
   			<td>등록일자</td>
    		<td><span class="createDate"></span></td>
   		</tr>
   	</table>
<div>
    <button type="button" id="updateBtn">수정</button>
</div>
</body>
</html>
<script type="text/javascript">

    var getProduct = function() {
        var reqUrl = "<%= SellerConstants.URI_DB_SELLER_PRODUCT_DETAIL %>".replace("{id}", "<c:out value='${id}'/>");
        console.log("url 은 ???" + reqUrl);

        procCallAjax(reqUrl, "GET", null, null, callbackGetProduct);
    };

    var callbackGetProduct = function(data){
        console.log("상품 정보는 ::: " + JSON.stringify(data));

        $('.categoryName').html(data.category.categoryName);
        $('.versionInfo').html(data.versionInfo);
        $('.productName').html(data.productName);
        $('.iconFile').html(data.iconFileName);
        var htmlString = [];
        for (var i = 0; i < data.screenshots.length; i++) {
        	htmlString.push(data.screenshots[i].screenshotFileName + ",");
        }
        $('.screenshotFiles').html(htmlString);
        $('.simpleDescription').html(data.simpleDescription);
        $('.detailDescription').html(data.detailDescription);
        $('.productType').html(data.productType);
        $('.productFile').html(data.productFileName);
        $('.envFile').html(data.envFileName);
        $('.meteringType').html(data.meteringType);
        $('.unitPrice').html(data.unitPrice);
        $('.displayYn').html(data.displayYn == 'Y'? '전시' : '미전시');
        $('.createDate').html(data.strCreateDate);
    };

    $("#updateBtn").on("click", function () {
       procMovePage("<%= SellerConstants.URI_WEB_SELLER_PRODUCT_UPDATE %>".replace("{id}", "<c:out value='${id}'/>"));
    });


    // ON LOAD
    $(document).ready(function() {
        getProduct();
    });
</script>