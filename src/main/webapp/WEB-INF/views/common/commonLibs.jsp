<%--
  Common library

  author: REX
  version: 1.0
  since: 2018.08.02
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="isAuthenticated()">
    <sec:authentication property="details.userId" var="userId"/>
    <sec:authentication property="details.username" var="username"/>
    <sec:authentication property="details.serviceInstanceId" var="serviceInstanceId"/>
    <sec:authentication property="details.organizationGuid" var="organizationGuid"/>
    <sec:authentication property="details.spaceGuid" var="spaceGuid"/>
    <sec:authentication property="details.nameSpace" var="nameSpace"/>
</sec:authorize>
<sec:authorize access="!isAuthenticated()">
    <script>
        location.href='/common/error/unauthorized';
    </script>
</sec:authorize>

<!--[if lt IE 9]>
<script type="text/javascript" src="/resources/js/html5shiv.min.js"></script>
<script type="text/javascript" src="/resources/js/respond.min.js"></script>
<![endif]-->

<%--CSS--%>
<link rel="shortcut icon" type="image/x-icon" href="<c:url value="/resources/images/favicon.ico"/>">

<%--JS--%>
<script type="text/javascript" src='<c:url value="/resources/js/jquery-1.12.4.min.js"/>'></script>
<script type="text/javascript" src='<c:url value="/resources/js/jquery.cookie.js"/>'></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js"/>"></script>

<script type="text/javascript">

    var USER_ID = "${userId}";
    var USER_NAME = "${username}";
    var SERVICE_INSTANCE_ID = "${serviceInstanceId}";
    var ORGANIZATION_GUID = "${organizationGuid}";
    var SPACE_GUID = "${spaceGuid}";
    var NAME_SPACE = "${nameSpace}";

    var _csrf_token = document.getElementsByName("_csrf")[0].getAttribute("content");
    var _csrf_header = document.getElementsByName("_csrf_header")[0].getAttribute("content");

</script>

<script type="text/javascript" src='<c:url value="/resources/js/common.js"/>'></script>
