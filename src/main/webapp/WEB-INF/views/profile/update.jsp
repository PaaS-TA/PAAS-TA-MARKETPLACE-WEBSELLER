<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-05-07
  Time: 오후 4:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.openpaas.paasta.marketplace.web.seller.common.SellerConstants" %>

<!-- content -->
<div class="content">
	
	<!-- cBox1 -->
	<div class="cBox type1 appCnt-info">
		<div class="cBox-hd">
			<h4 class="c-tit">프로필 수정</h4>
		</div>
		<div class="cBox-cnt pt20">
			<!-- inner -->
			<div class="in pd0 d_block">
				<!-- table top -->
				<div class="tb-top">
					<div class="pull-right">
						<span class="point2"><i class="i_star">필수입력</i> 는 필수입력 사항입니다.</span>
					</div>
				</div>
				<!-- //table top -->

				<!-- table -->
				<div class="tbw type1">
					<table class="table">
						<colgroup>
							<col style="width:15%;">
							<col style="width:auto;">
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">판매자명<i class="i_star">필수입력</i></th>
								<td><input class="form-control" type="text" id="sellerName" value=""></td>
							</tr>
							<tr>
								<th scope="row">유형<i class="i_star">필수입력</i></th>
								<td>
									<select id="businessType" onchange="selectBox();">
									</select> 
								</td>
							</tr>
							<tr>
								<th scope="row">담당자명</th>
								<td><input class="form-control" type="text" id="managerName" value=""></td>
							</tr>
							<tr>
								<th scope="row">메일주소<i class="i_star">필수입력</i></th>
								<td><input class="form-control" type="text" id="emailAddress" value=""></td>
							</tr>
							<tr>
								<th scope="row">홈페이지주소</th>
								<td><input class="form-control" type="text" id="homepageUrl" value=""></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- //table -->
			</div>
			<!-- //inner -->
		</div>
	</div>
	<!-- //cBox1 -->

	<div class="cont_btnBox">
		<button name="button" class="btn btn-color1 btn-md" type="button" onclick="updateProfile();">저장</button>
	</div>
</div>
<!-- //content -->

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
        if(data.resultCode === "SUCCESS") {
            procMovePage("<%= SellerConstants.URI_WEB_SELLER_PROFILE_DETAIL %>".replace("{id}", data.id));
        } else {
        	alert("오류 발생!!! : [" + data.resultMessage + "]");
        	return;
        }
    };


    // ON LOAD
    $(document).ready(function() {
        getProfile();
        getBusinessType();
    });
</script>