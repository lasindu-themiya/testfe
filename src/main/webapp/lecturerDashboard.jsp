<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <title>Lecturer Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 400px;
        }

        .details {
            text-align: left;
            margin-top: 15px;
        }

        .test-links {
            margin-top: 30px;
            text-align: left;
        }

        .test-links h4 {
            margin-top: 0;
        }

        .test-links a {
            display: inline-block;
            margin-bottom: 5px;
            text-decoration: none;
            color: #007BFF;
        }

        .test-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="card">
    <h3><c:out value="${name}" default="Loading..." /></h3>
    <div class="details">
        <p><b>ID:</b> <c:out value="${id}" /></p>
        <p><b>Email:</b> <c:out value="${email}" /></p>
        <p><b>Department:</b> <c:out value="${department}" /></p>
        <p><b>Contact:</b> <c:out value="${contact}" /></p>
        <p><b>Course Assigned:</b> <c:out value="${courseAssign}" /></p>
    </div>

    <!-- Links to test forms/pages -->
    <div class="test-links">
        <h4>Test Links</h4>

        <!-- Announcement Links -->
        <h5>Announcements</h5>
        <a href="${pageContext.request.contextPath}/announcement/createAnnouncement.jsp">Create Announcement</a><br/>
        <a href="${pageContext.request.contextPath}/AnnouncementServlet?action=list">List Announcements</a><br/>
        <a href="${pageContext.request.contextPath}/AnnouncementServlet?action=update">Update Announcements</a><br/>

        <!-- Workshop Links -->
        <h5>Workshops</h5>
        <a href="${pageContext.request.contextPath}/workshop/createWorkshop.jsp">Create Workshop</a><br/>
        <a href="${pageContext.request.contextPath}/WorkshopServlet?action=list">List Workshops</a><br/>
        <a href="${pageContext.request.contextPath}/WorkshopServlet?action=update">Update Workshops</a><br/>

        <!-- Interview Links -->
        <h5>Interviews</h5>
        <a href="${pageContext.request.contextPath}/interview/createInterview.jsp">Create Interview</a><br/>
        <a href="${pageContext.request.contextPath}/InterviewServlet?action=list">List Interviews</a><br/>
        <a href="${pageContext.request.contextPath}/WorkshopServlet?action=update">Update Workshops</a><br/>

        <!-- Feedback Links -->
        <h5>Feedback</h5>
        <a href="${pageContext.request.contextPath}/feedback/createFeedback.jsp">Add Feedback</a><br/>
        <a href="${pageContext.request.contextPath}/FeedbackServlet?action=viewByBatch">View Batch Feedback</a><br/>
        <a href="${pageContext.request.contextPath}/FeedbackServlet?action=viewByStudent">View Student Feedback</a><br/>

        <!-- Profile/Password Update Links -->
        <h5>Lecturer Profile Management</h5>
        <a href="${pageContext.request.contextPath}/lecturer/updateProfile.jsp">Update Profile</a><br/>
        <a href="${pageContext.request.contextPath}/lecturer/updatePassword.jsp">Update Password</a><br/>

        <!-- Messages and Reports -->
        <h5>Lecturer Message and Report Management</h5>
        <a href="${pageContext.request.contextPath}/messages/ViewMessagesServlet">View Messages</a><br/>

        <h5>Student Portfolio View</h5>
        <a href="${pageContext.request.contextPath}/portfolio/lecturerViewPortfolio.jsp">View Portfolio</a><br/>
    </div>
</div>
</body>
</html>
