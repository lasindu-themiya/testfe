<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Feedback</title>
</head>
<body>
<h1>Edit Feedback</h1>

<!-- Display Success or Error Messages -->
<c:if test="${not empty message}">
    <p style="color: ${message.startsWith('Failed') ? 'red' : 'green'};">${message}</p>
</c:if>

<c:if test="${not empty feedback}">
    <!-- Temporary Debugging Line: Display fid -->
    <p>Feedback ID (fid): ${feedback.fid}</p>

    <form action="FeedbackServlet" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="feedbackId" value="${feedback.fid}">

        <label>Content:</label><br/>
        <textarea name="content" required rows="5" cols="50">${feedback.content}</textarea><br/><br/>

        <label>Points:</label>
        <input type="number" name="points" value="${feedback.points}" required><br/><br/>

        <button type="submit">Update</button>
    </form>
</c:if>

<c:if test="${empty feedback}">
    <p>Feedback details are missing or invalid.</p>
    <a href="FeedbackServlet?action=viewByBatch">Return to Feedback List</a>
</c:if>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
