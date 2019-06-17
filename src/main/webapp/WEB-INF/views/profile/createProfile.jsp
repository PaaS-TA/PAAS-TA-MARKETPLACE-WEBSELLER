<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-07
  Time: 오후 4:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ page import="org.openpaas.paasta.marketplace.web.seller.common.SellerConstants" %> --%>
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
    <button type="button" onclick="createProfile();">등록하기</button>
</div>
</body>
</html>
<script type="text/javascript">

    var businessTypeValue;
    
    var getBusinessType = function () {
        var groupCode = "<%= SellerConstants.GROUP_CODE_BUSINESS_TYPE %>";
        var reqUrl = "<%= SellerConstants.URI_DB_CUSTOM_CODE_LIST %>".replace("{groupCode}", groupCode);

        procCallAjax(reqUrl, "GET", null, null, callbackGetBusinessTypeList);
    };

    var callbackGetBusinessTypeList = function (data) {
        console.log("업체유형 목록 :::" + JSON.stringify(data));

        var businessTypeList = data.items;

        var businessTypeArea = $("#businessType");
        var htmlArray = [];
        var option = "<option selected='selected'>선택</option>";

        for(var i = 0; i < businessTypeList.length; i++){
            option += "<option value=" + businessTypeList[i].unitCode + ">" + businessTypeList[i].unitCodeName + "</option>"
        }

        htmlArray.push(option);
        businessTypeArea.html(htmlArray);
    };

    var selectBox = function () {
    	businessTypeValue = $("#businessType option:selected").val();
      	console.log("선택된 값은? " + businessTypeValue);
    };

    var createProfile = function () {
        var reqUrl = "<%= SellerConstants.URI_WEB_SELLER_PROFILE_CREATE %>";

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
        /* alert(JSON.stringify(param)); */

        procCallAjax(reqUrl, "POST", JSON.stringify(param), null, callbackCreateProfile);

    };

    var callbackCreateProfile = function(data){
        console.log("저장 완료!!! " + JSON.stringify(data));
        /* alert(JSON.stringify(data)); */
        if (data.resultCode === "SUCCESS") {
        	procMovePage("<%= SellerConstants.URI_WEB_SELLER_PROFILE_DETAIL %>".replace("{id}", data.id));
        } else {
        	alert("오류 발생!!! : [" + data.resultMessage + "]");
        	return;
        }
    };

    
    // ON LOAD
    $(document).ready(function() {
        getBusinessType();
    });

</script>