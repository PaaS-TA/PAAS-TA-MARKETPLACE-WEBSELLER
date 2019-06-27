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
			<h4 class="c-tit">프로필 상세</h4>
		</div>
		<div class="cBox-cnt">
			<!-- inner -->
			<div class="in pd0 d_block">
				<!-- table -->
				<div class="tbw type1">
					<table class="table">
						<colgroup>
							<col style="width:15%;">
							<col style="width:auto;">
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">판매자명</th>
								<td id="sellerName"></td>
							</tr>
							<tr>
								<th scope="row">유형</th>
								<td id="businessType"></td>
							</tr>
							<tr>
								<th scope="row">담당자명</th>
								<td id="managerName"></td>
							</tr>
							<tr>
								<th scope="row">메일주소</th>
								<td id="emailAddress"></td>
							</tr>
							<tr>
								<th scope="row">홈페이지주소</th>
								<td id="homepageUrl"></td>
							</tr>
							<tr>
								<th scope="row">등록일</th>
								<td id="createdDate"></td>
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
		<button name="button" class="btn btn-color1 btn-md" type="button" id="updateBtn">수정</button>
	</div>
</div>
<!-- //content -->

<script type="text/javascript">
    var getProfile = function() {
        var reqUrl = "<%= SellerConstants.URI_DB_SELLER_PROFILE_DETAIL %>".replace("{id}", "<c:out value='${id}'/>");
        console.log("url 은 ???" + reqUrl);

        procCallAjax(reqUrl, "GET", null, null, callbackGetProfile);
    };

    var callbackGetProfile = function(data){
        console.log("나의 프로필 정보는 ::: " + JSON.stringify(data));

        $('#sellerName').html(data.sellerName);
        $('#businessType').html(data.businessType);
        $('#managerName').html(data.managerName);
        $('#emailAddress').html(data.email);
        $('#homepageUrl').html(data.homepageUrl);
        $('#createdDate').html(data.strCreateDate);
    };

    $("#updateBtn").on("click", function () {
    	procMovePage("<%= SellerConstants.URI_WEB_SELLER_PROFILE_UPDATE %>".replace("{id}", "<c:out value='${id}'/>"));
    });


    // ON LOAD
    $(document).ready(function() {
        getProfile();
    });
</script>