<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Events</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 0;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 600px;
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
        .form-group {
            margin: 20px 0;
            text-align: center;
        }
        button {
            background-color: #007BFF;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        button:hover {
            background-color: #0056b3;
        }
        .section {
            margin-bottom: 30px;
        }
        .section h2 {
            color: #444;
            margin-bottom: 10px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>View Events</h1>

    <div class="section">
        <h2>Announcements</h2>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewAnnouncementsByStudent">
                <button type="submit">View Your Announcements</button>
            </form>
        </div>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewAnnouncementsByBatch">
                <button type="submit">View Batch Announcements</button>
            </form>
        </div>
    </div>

    <div class="section">
        <h2>Workshops</h2>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewWorkshopsByStudent">
                <button type="submit">View Your Workshops</button>
            </form>
        </div>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewWorkshopsByBatch">
                <button type="submit">View Batch Workshops</button>
            </form>
        </div>
    </div>

    <div class="section">
        <h2>Interviews</h2>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewInterviewsByStudent">
                <button type="submit">View Your Interviews</button>
            </form>
        </div>
        <div class="form-group">
            <form action="<%= request.getContextPath() %>/StudentServlet" method="GET">
                <input type="hidden" name="action" value="viewInterviewsByBatch">
                <button type="submit">View Batch Interviews</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
