<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Student Dashboard</title>
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
            width: 300px;
        }

        .photo {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            margin-bottom: 10px;
        }

        .details {
            text-align: left;
            margin-top: 15px;
        }
    </style>
</head>
<body>
<div class="card">
    <img class="photo" src="<%= request.getAttribute("photo") != null ? request.getAttribute("photo") : "defaultPhoto.jpg" %>" alt="Student Photo">
    <h3><%= request.getAttribute("name") != null ? request.getAttribute("name") : "Loading..." %></h3>
    <div class="details">
        <p><b>ID:</b> <%= request.getAttribute("id") %></p>
        <p><b>Email:</b> <%= request.getAttribute("email") %></p>
        <p><b>Contact:</b> <%= request.getAttribute("contact") %></p>
        <p><b>Year:</b> <%= request.getAttribute("year") %></p>
        <p><b>Role:</b> <%= request.getAttribute("role") %></p>
    </div>
</div>
<br>
<a href="updateStudentProfile.jsp"> Update </a><br>
<a href="updatePassword.jsp"> Change Password  </a><br>
<a href="<%= request.getContextPath() %>/StudentServlet?action=viewProgressUpdates"> View Progress Updates </a><br>

<a href="<%= request.getContextPath() %>/TeamServlet?action=add"> createTeam </a><br>

<br><br>
<a href="viewEvents.jsp"> events </a><br>


<a href="/frontend_war/LogoutServlet"> Logout </a>
</body>
</html>
