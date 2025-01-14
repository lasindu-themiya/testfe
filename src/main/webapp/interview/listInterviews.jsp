<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>All Interviews</title>
</head>
<body>
<h1>All Interviews</h1>

<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>

<table border="1">
    <tr>
        <th>Event ID</th>
        <th>Name</th>
        <th>Company</th>
        <th>Position</th>
        <th>Mode</th>
        <th>Date</th>
        <th>Time</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <!-- 'interviewList' is a List<InterviewResponse> set by the InterviewServlet -->
    <c:forEach var="itv" items="${interviewList}">
        <tr>
            <td>${itv.eventId}</td>
            <td>${itv.name}</td>
            <td>${itv.companyName}</td>
            <td>${itv.position}</td>
            <td>${itv.mode}</td>
            <td>${itv.date}</td>
            <td>${itv.time}</td>
            <td>${itv.status}</td>
            <td>
                <a href="${pageContext.request.contextPath}/InterviewServlet?action=edit&id=${itv.eventId}">Edit</a>
                |
                <a href="${pageContext.request.contextPath}/InterviewServlet?action=delete&id=${itv.iId}"
                   onclick="return confirm('Are you sure?');">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

<p><a href="createInterview.jsp">Create New Interview</a></p>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
