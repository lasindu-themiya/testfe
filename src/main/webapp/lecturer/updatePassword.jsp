<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update Password</title>
</head>
<body>
<h1>Update Password</h1>
<form method="POST" action="<%= request.getContextPath() %>/LecturerProfileServlet?action=updatePassword">
    <label>Current Password:</label>
    <input type="password" name="currentPassword" /><br/>

    <label>New Password:</label>
    <input type="password" name="newPassword" /><br/>

    <input type="submit" value="Update Password"/>
</form>
<a href="../lecturerDashboard.jsp">Back to Dashboard</a>

</body>
</html>
