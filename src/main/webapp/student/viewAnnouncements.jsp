<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Announcements</title>
</head>
<body>
<h1>Announcements</h1>
<c:if test="${not empty announcements}">
    <ul>
        <c:forEach var="announcement" items="${announcements}">
            <li>${announcement.content} (Date: ${announcement.date})</li>
        </c:forEach>
    </ul>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
