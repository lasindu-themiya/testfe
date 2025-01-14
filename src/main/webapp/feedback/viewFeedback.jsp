<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Feedback</title>
    <script>
        function loadStudentsForBatch() {
            const batchId = document.getElementById('batchId').value;
            if (batchId) {
                window.location.href = '${pageContext.request.contextPath}/FeedbackServlet?action=loadStudentsView&batchId=' + batchId;
            }
        }
    </script>
</head>
<body>
<h1>Feedback List</h1>

<!-- Display Success or Error Messages -->
<c:if test="${not empty message}">
    <p style="color: ${message.startsWith('Failed') ? 'red' : 'green'};">${message}</p>
</c:if>

<%
    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
    List<com.example.frontend.dto.FeedbackDTO> feedbackList =
            (List<com.example.frontend.dto.FeedbackDTO>) request.getAttribute("feedbackList");
    String selectedBatchId = (String) request.getAttribute("selectedBatchId");
    String selectedStudentId = (String) request.getAttribute("selectedStudentId");
%>

<!-- View Feedback by Batch -->
<form action="${pageContext.request.contextPath}/FeedbackServlet" method="get">
    <label for="batchId">View Feedback by Batch:</label>
    <select id="batchId" name="batchId" onchange="loadStudentsForBatch()">
        <option value="">-- Select Batch --</option>
        <c:forEach var="b" items="${batchList}">
            <option value="${b.id}" <c:if test="${b.id == selectedBatchId}">selected</c:if>>
                    ${b.name}
            </option>
        </c:forEach>
    </select>
    <input type="hidden" name="action" value="viewByBatch">
    <button type="submit">View Feedback</button>
</form>
<br>

<!-- View Feedback by Student -->
<form action="${pageContext.request.contextPath}/FeedbackServlet" method="get">
    <input type="hidden" name="batchId" value="${selectedBatchId != null ? selectedBatchId : ''}">
    <label for="studentId">View Feedback by Student:</label>
    <select id="studentId" name="studentId">
        <option value="">-- Select a Student --</option>
        <c:forEach var="s" items="${studentList}">
            <option value="${s.id}" <c:if test="${s.id == selectedStudentId}">selected</c:if>>
                    ${s.name}
            </option>
        </c:forEach>
    </select>
    <input type="hidden" name="action" value="viewByStudent">
    <button type="submit">View Feedback</button>
</form>
<br>

<!-- Feedback Table -->
<table border="1">
    <tr>
        <th>Feedback ID</th>
        <th>Content</th>
        <th>Points</th>
        <th>Date</th>
        <th>Student</th>
        <th>Batch</th>
        <th>Actions</th>
    </tr>
    <c:if test="${empty feedbackList}">
        <tr>
            <td colspan="7">No feedback available</td>
        </tr>
    </c:if>
    <c:forEach var="fb" items="${feedbackList}">
        <tr>
            <td>${fb.fid}</td>
            <td>${fb.content}</td>
            <td>${fb.points}</td>
            <td>${fb.date}</td>
            <td>${fb.studentId}</td>
            <td>${fb.batchId}</td>
            <td>
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=edit&id=${fb.fid}">Edit</a>
                |
                <a href="${pageContext.request.contextPath}/FeedbackServlet?action=delete&id=${fb.fid}"
                   onclick="return confirm('Are you sure you want to delete this feedback?');">
                    Delete
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
