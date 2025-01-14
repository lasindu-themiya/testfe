<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>View Messages</title>
</head>
<body>
<h1>Messages</h1>

<!-- Check if messages exist -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty messages}">
    <h2>Messages</h2>
    <table border="1">
        <thead>
        <tr>
            <th>Subject</th>
            <th>Content</th>
            <th>Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="message" items="${messages}">
            <tr>
                <td>${message.subject}</td>
                <td>${message.content}</td>
                <td>${message.date}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<!-- No messages -->
<c:if test="${empty messages}">
    <p>No messages to display.</p>
</c:if>

<a href="../lecturerDashboard.jsp">Back to Dashboard</a>
</body>
</html>
