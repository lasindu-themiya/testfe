<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Workshops</title>
</head>
<body>
<h1>Workshops</h1>
<c:if test="${not empty workshops}">
    <ul>
        <c:forEach var="workshop" items="${workshops}">
            <li>${workshop.name} (Date: ${workshop.date})</li>
        </c:forEach>
    </ul>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
