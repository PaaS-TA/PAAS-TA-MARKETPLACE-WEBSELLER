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
    <title>프로필 수정</title>

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
        <td>
            <select onchange="selectBox();" id="businessType">
            </select>
        </td>
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
    <button type="button" onclick="updateProfile();">저장</button>
</div>
</body>
</html>
<script type="text/javascript">

    var orgBusinessType;
    var businessTypeValue;

    var getProfile = function () {
        var reqUrl = "<%= SellerConstants.URI_DB_SELLER_PROFILE_DETAIL %>".replace("{id}", "<c:out value='${id}'/>");
        console.log("url 은 ???" + reqUrl);

        procCallAjax(reqUrl, "GET", null, null, callbackGetProfile);
    };

    var callbackGetProfile = function(data){
        console.log("나의 프로필 정보는 ::: " + JSON.stringify(data));

        $('#sellerName').val(data.sellerName);
        orgBusinessType = data.businessType;
        $('#managerName').val(data.managerName);
        $('#emailAddress').val(data.email);
        $('#homepageUrl').val(data.homepageUrl);

    };

    var getBusinessType = function () {
        var groupCode = "<%= SellerConstants.GROUP_CODE_BUSINESS_TYPE %>";
        var reqUrl = "<%= SellerConstants.URI_DB_CUSTOM_CODE_LIST %>".replace("{groupCode}", groupCode);

        procCallAjax(reqUrl, "GET", null, null, callbackGetBusinessType);
    };

    var callbackGetBusinessType = function (data) {
        console.log("비즈니스 코드 List :::" + JSON.stringify(data));

        var BUSINESS_TYPE_LIST = data.items;

        var businessTypeArea = $("#businessType");
        var htmlString = [];

        var option = "<option>선택</option>";
        for(var i = 0; i < BUSINESS_TYPE_LIST.length; i++){
            if(orgBusinessType === BUSINESS_TYPE_LIST[i].unitCode) {
                option += "<option selected='selected' value=" + BUSINESS_TYPE_LIST[i].unitCode + ">" + BUSINESS_TYPE_LIST[i].unitCodeName + "</option>";
            } else {
                option += "<option value=" + BUSINESS_TYPE_LIST[i].unitCode + ">" + BUSINESS_TYPE_LIST[i].unitCodeName + "</option>"
            }
        }

        htmlString.push(option);
        businessTypeArea.html(htmlString);
    };

    var selectBox = function () {
        businessTypeValue = $("#businessType option:selected").val();
        console.log("선택된 값은? " + businessTypeValue);
    };

    var updateProfile = function () {
        var reqUrl = "<%= SellerConstants.URI_WEB_SELLER_PROFILE_UPDATE %>".replace("{id}", "<c:out value='${id}'/>");

        var sellerName = $('#sellerName').val();
        var businessType = businessTypeValue;
        var managerName = $('#managerName').val();
        var email = $('#emailAddress').val();
        var homepageUrl = $('#homepageUrl').val();

        var param = {
            "sellerName": sellerName,
            "businessType": businessType,
            "managerName": managerName,
            "email": email,
            "homepageUrl": homepageUrl
        };

        procCallAjax(reqUrl, "PUT", JSON.stringify(param), null, callbackUpdateProfile);

    };

    var callbackUpdateProfile = function(data){
        console.log("update 완료!!! ");
        if(data.resultCode === RESULT_STATUS_SUCCESS) {
            procMovePage("<%= SellerConstants.URI_WEB_SELLER_PROFILE_DETAIL %>".replace("{id}", data.id));
        } else {
            alert("작업 결과는 " + RESULT_STATUS_FAIL + "입니다.")
        }
    };


    // ON LOAD
    $(document).ready(function() {
        getProfile();
        getBusinessType();
    });

</script>