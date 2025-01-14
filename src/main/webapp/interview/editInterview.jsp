<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Interview</title>
</head>
<body>
<h1>Edit Interview</h1>
<form method="POST" action="<%= request.getContextPath() %>/InterviewServlet?action=update">
    <input type="hidden" name="interviewId" value="${itv.eventId}" />

    <label>Company Name:</label>
    <input type="text" name="companyName" value="${itv.companyName}"/><br/>

    <label>Position:</label>
    <input type="text" name="position" value="${itv.position}"/><br/>

    <label>Mode (Online/Offline):</label>
    <input type="text" name="mode" value="${itv.mode}"/><br/>

    <input type="submit" value="Update Interview"/>
</form>
<p><a href="listInterviews.jsp">Back to Interviews</a></p>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
