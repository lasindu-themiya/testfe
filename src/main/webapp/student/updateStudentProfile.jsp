<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
  <title>Update Student Profile</title>
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
  </style>
</head>
<body>
<div class="form-container">
  <h2>Update Profile</h2>
  <form action="/frontend_war/student/updateProfile" method="post">
    <label for="name">Name</label>
    <input type="text" id="name" name="name" placeholder="Enter your name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>" required>

    <label for="email">Email</label>
    <input type="email" id="email" name="email" placeholder="Enter your email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>" required>

    <label for="contact">Contact</label>
    <input type="text" id="contact" name="contact" placeholder="Enter your contact number" value="<%= request.getAttribute("contact") != null ? request.getAttribute("contact") : "" %>" required>

    <label for="photo">Photo URL</label>
    <input type="text" id="photo" name="photo" placeholder="Enter photo URL" value="<%= request.getAttribute("photo") != null ? request.getAttribute("photo") : "" %>">

    <button type="submit">Update Profile</button>
  </form>
</div>
</body>
</html>
