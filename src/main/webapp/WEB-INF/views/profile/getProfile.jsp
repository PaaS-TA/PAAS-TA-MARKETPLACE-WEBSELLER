<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-13
  Time: 오후 1:51
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
    <title>프로필 상세 조회</title>

    <%@include file="../common/commonLibs.jsp" %>
</head>
<body>
<table>
    <tr>
        <td>판매자명</td>
        <td>
            <span class="sellerName"></span>
        </td>
    </tr>
    <tr>
        <td>유형</td>
        <td>
            <span class="businessUnit"></span>
        </td>
    </tr>
    <tr>
        <td>담당자명</td>
        <td>
            <span class="managerName"></span>
        </td>
    </tr>
    <tr>
        <td>메일 주소</td>
        <td>
            <span class="emailAddress"></span>
        </td>
    </tr>
    <tr>
        <td>홈페이지 주소</td>
        <td>
            <span class="homepageUrl"></span>
        </td>
    </tr>
    <tr>
        <td>등록일</td>
        <td>
            <span class="createdDate"></span>
        </td>
    </tr>
</table>
<div>
    <button type="button" id="updateBtn">수정</button>
</div>
</body>
</html>
<script type="text/javascript">

    var getProfile = function(){
        var reqUrl = "<%= Constants.URI_MARKET_API_PROFILE %>"+ "/" + "<c:out value='${id}'/>";
        console.log("url 은 ???" + reqUrl);

        procCallAjax(reqUrl, "GET", null, null, callbackGetProfile);
    };

    var callbackGetProfile = function(data){
        console.log("나의 프로필 정보는 ::: " + JSON.stringify(data));

        $('.sellerName').html(data.sellerName);
        $('.businessUnit').html(data.businessType);
        $('.managerName').html(data.managerName);
        $('.emailAddress').html(data.email);
        $('.homepageUrl').html(data.homepageUrl);
        $('.createdDate').html(data.strCreateDate);
    };

    $("#updateBtn").on("click", function () {
       procMovePage("<%= Constants.URI_SELLER_PROFILE %>" + "/" + "<c:out value='${id}'/>" + "/update");
    });


    // ON LOAD
    $(document).ready(function() {
        getProfile();
    });
</script>