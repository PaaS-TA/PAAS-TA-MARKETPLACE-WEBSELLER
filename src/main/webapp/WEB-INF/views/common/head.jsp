<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page import="org.openpaas.paasta.marketplace.web.seller.common.SellerConstants" %>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="details.userId" var="userId"/>
    <sec:authentication property="details.username" var="username"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <script>
        location.href='/common/error/unauthorized';
    </script>
</sec:authorize>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="format-detection" content="telephone=no"/>
<meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
<title>PaaS-TA Marketplace</title>

<!-- common -->
<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-slider.css">
<link rel="stylesheet" type="text/css" href="/resources/css/jquery.jscrollpane.css">
<link rel="stylesheet" type="text/css" href="/resources/css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="/resources/css/css.css">

<!--[if lt IE 9]>
	<script type="text/javascript" src="/resources/js/html5shiv.min.js"></script>
	<script type="text/javascript" src="/resources/js/respond.min.js"></script>
<![endif]-->

<script type="text/javascript" src="/resources/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/resources/js/jquery-ui.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/resources/js/bootstrap-slider.js"></script>
<script type="text/javascript" src="/resources/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="/resources/js/jquery.jscrollpane.min.js"></script>

<!-- custom -->
<script type="text/javascript" src="/resources/js/common.js"></script>
<script type="text/javascript" src="/resources/js/user.js"></script>

<script type="text/javascript">

    var USER_ID = "${userId}";
    var USER_NAME = "${username}";
    var RESULT_STATUS_SUCCESS  = "<%= SellerConstants.RESULT_STATUS_SUCCESS %>";
    var RESULT_STATUS_FAIL     = "<%= SellerConstants.RESULT_STATUS_FAIL %>";

    var _csrf_token = document.getElementsByName("_csrf")[0].getAttribute("content");
    var _csrf_header = document.getElementsByName("_csrf_header")[0].getAttribute("content");

</script>