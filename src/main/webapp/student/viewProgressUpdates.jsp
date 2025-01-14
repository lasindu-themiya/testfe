<%@ page import="com.example.frontend.dto.ProgressUpdateResponse" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Progress Updates</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 0;
            background-color: #f4f4f9;
        }
        .container {
            max-width: 800px;
            margin: auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        ul {
            list-style: none;
            padding: 0;
        }
        li {
            background: #e6e6e6;
            margin: 10px 0;
            padding: 15px;
            border-radius: 5px;
        }
        .content {
            font-size: 16px;
        }
        .date {
            font-size: 14px;
            color: #666;
        }
        a {
            display: block;
            margin-top: 20px;
            text-align: center;
            text-decoration: none;
            color: #007BFF;
        }
        a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Progress Updates</h1>
    <ul>
        <%
            List<ProgressUpdateResponse> progressUpdates = (List<ProgressUpdateResponse>) request.getAttribute("progressUpdates");
            if (progressUpdates != null && !progressUpdates.isEmpty()) {
                for (ProgressUpdateResponse update : progressUpdates) {
        %>
        <li>
            <div class="content"><%= update.getContent() %></div>
            <div class="date"><b>Date:</b> <%= update.getDate() %></div>
        </li>
        <%
            }
        } else {
        %>
        <li>No progress updates to show.</li>
        <% } %>
    </ul>
    <a href="studentDashboard.jsp">Back to Dashboard</a>
</div>
</body>
</html>
