<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="ko">
<head>
	<tiles:insertAttribute name="head" />
</head>

<body>
<!-- wrap -->
	<div id="wrap" class="">
		<!-- header -->
		<tiles:insertAttribute name="header" />
		<!-- //header -->
	
		<!-- container -->
		<div id="container">
			<!-- left cnt -->
			<tiles:insertAttribute name="left" />
			<!-- //left cnt -->
	
			<!-- right Cnt -->
			<div class="rightCnt">
				<!-- title area -->
				<tiles:insertAttribute name="rightTitle" />
				<!-- //title area -->
	
				<!-- content -->
				<tiles:insertAttribute name="body" />
				<!-- //content -->
			</div>
			<!-- right Cnt -->
		</div>
		<!-- //container -->
	</div>
<!-- //wrap -->
</body>
</html>
