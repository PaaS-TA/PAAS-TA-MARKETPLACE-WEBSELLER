<%--
  Created by IntelliJ IDEA.
  User: hrjin
  Date: 2019-04-29
  Time: 오후 4:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta id="_csrf" name="_csrf" content="${_csrf.token}"/>
    <!-- default header name is X-CSRF-TOKEN -->
    <meta id="_csrf_header" name="_csrf_header" content="${_csrf.headerName}"/>
    <title>판매자 포탈</title>

    <%@include file="../common/commonLibs.jsp" %>
</head>
<body>
<div>
    <h1>상품을 등록할 수 있답니다!!!</h1>
</div>

<a href="/seller/profile/list">프로필 목록</a>
<br/><br/>
<a href="/seller/profile/create">프로필 등록</a>
</body>