<%@ page import="com.example.servletstudy.domain.member.MemberRepository" %>
<%@ page import="com.example.servletstudy.domain.member.Member" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Logger log = LoggerFactory.getLogger("jsp/members/save.jsp");
    log.info("JspClass.jsp_service_method");

    MemberRepository memberRepository = MemberRepository.getInstance();

    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));
    Member savedMember = memberRepository.save(new Member(username, age));
    log.info("savedMember = {}", savedMember);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
Success
<ul>
    <li>id=<%=savedMember.getId()%></li>
    <li>username=<%=savedMember.getUsername()%></li>
    <li>age=<%=savedMember.getAge()%></li>
</ul>
<a href="/index.html">Home</a>
</body>
</html>
