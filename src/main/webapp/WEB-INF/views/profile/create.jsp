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
			<h4 class="c-tit">프로필 등록</h4>
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
		<button name="button" class="btn btn-color1 btn-md" type="button" onclick="createProfile();">등록</button>
	</div>
</div>
<!-- //content -->

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