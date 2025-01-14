<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <title>Toggle Admin Status</title>
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
            text-align: center;
        }
        .form-container h2 {
            margin-bottom: 20px;
        }
        .form-container label {
            font-weight: bold;
            display: block;
            margin-top: 10px;
        }
        .form-container input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .form-container button {
            margin-top: 15px;
            padding: 10px 20px;
            border: none;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }
        .form-container .activate {
            background-color: #4CAF50;
        }
        .form-container .block {
            background-color: #ff4d4d;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>Toggle Admin Status</h2>

    <form action="/frontend_war/admin/toggleStatus" method="post">
        <label for="adminId">Admin ID</label>
        <input type="text" id="adminId" name="id" placeholder="Enter Admin ID" required>

        <button class="activate" type="submit" name="status" value="ACTIVE">Activate Admin</button>
        <button class="block" type="submit" name="status" value="BLOCKED">Block Admin</button>
    </form>
</div>
</body>
</html>
