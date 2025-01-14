<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Update Admin</title>
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
        .form-container {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 400px;
        }
        .form-container h2 {
            margin-bottom: 20px;
        }
        .form-container label {
            font-weight: bold;
            display: block;
            margin-top: 10px;
        }
        .form-container input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .form-container button {
            margin-top: 20px;
            padding: 10px 20px;
            border: none;
            background-color: #4CAF50;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }
        .form-container .error {
            color: red;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Update Admin</h2>
    <form action="/frontend_war/admin/updateAdmin" method="post">
        <label for="id">Admin ID</label>
        <input type="text" id="id" name="id" value="" required>

        <label for="userName">Username</label>
        <input type="text" id="userName" name="userName" value="" required>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="" required>

        <label for="password">Password</label>
        <input type="password" id="password" name="password" placeholder="Enter new password" required>

        <label for="status">Status</label>
        <input type="text" id="status" name="status" value="" required>

        <button type="submit">Update Admin</button>

        <% if (request.getAttribute("error") != null) { %>
        <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
    </form>
</div>
</body>
</html>
