<!DOCTYPE html>
<html>
<head>
    <title>Add Batch</title>
</head>
<body>
<form action="/frontend_war/batch/addBatch" method="post">
    <label for="id">Batch ID:</label>
    <input type="text" id="id" name="id" required><br>

    <label for="name">Batch Name:</label>
    <input type="text" id="name" name="name" required><br>

    <label for="startDate">Start Date:</label>
    <input type="date" id="startDate" name="startDate" required><br>

    <label for="department">Department:</label>
    <input type="text" id="department" name="department" required><br>

    <label for="course">Course:</label>
    <input type="text" id="course" name="course" required><br>

    <button type="submit">Add Batch</button>
</form>
</body>
</html>
