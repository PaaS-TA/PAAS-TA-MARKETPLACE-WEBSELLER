<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-07
  Time: 오후 4:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.openpaas.paasta.marketplace.web.seller.common.Constants" %>
<html>
<head>
    <meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
    <title>프로필 등록</title>

    <%@include file="../common/commonLibs.jsp" %>
</head>
<body>
<table>
    <tr>
        <td>판매자명</td>
        <td><input type="text" id="sellerName"></td>
    </tr>
    <tr>
        <td>유형</td>
        <td><input type="text" id="groupType"></td>
    </tr>
    <tr>
        <td>담당자명</td>
        <td><input type="text" id="managerName"></td>
    </tr>
    <tr>
        <td>메일 주소</td>
        <td><input type="text" id="emailAddress"></td>
    </tr>
    <tr>
        <td>홈페이지 주소</td>
        <td><input type="text" id="homepageUrl"></td>
    </tr>
</table>
<div>
    <button type="button" onclick="createProfile();">등록하기</button>
</div>
</body>
</html>
<script type="text/javascript">

    var createProfile = function () {
        var reqUrl = "<%= Constants.URI_MARKET_API %>";

        var sellerName = $('#sellerName').val();
        var groupType = $('#groupType').val();
        var managerName = $('#managerName').val();
        var email = $('#emailAddress').val();
        var homepageUrl = $('#homepageUrl').val();

        var param = {
            "sellerName": sellerName,
            "groupType": groupType,
            "managerName": managerName,
            "email": email,
            "homepageUrl": homepageUrl
        };

        console.log("파아라라라라라람 ::: " + JSON.stringify(param));

        procCallAjax(reqUrl, "POST", JSON.stringify(param), null, callbackCreateProfile);

    };

    var callbackCreateProfile = function(data){
        alert("저장 완료!!! " + JSON.stringify(data));
    };



    // ON LOAD
    $(document).ready(function() {

    });

</script>
