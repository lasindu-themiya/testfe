<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Interviews</title>
</head>
<body>
<h1>Interviews</h1>
<c:if test="${not empty interviews}">
    <ul>
        <c:forEach var="interview" items="${interviews}">
            <li>${interview.companyName} - ${interview.position} (Date: ${interview.date})</li>
        </c:forEach>
    </ul>
</c:if>
<a href="studentDashboard.jsp">Back to Dashboard</a>
</body>
</html>
