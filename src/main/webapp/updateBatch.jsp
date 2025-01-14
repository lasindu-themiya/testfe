<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Batch</title>
    <script>
        // Define the batches as a JavaScript variable embedded in the JSP
        const batches = <%= request.getAttribute("batches") != null
        ? new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(request.getAttribute("batches"))
        : "[]" %>;

        // Populate the combo box and details when the page loads
        window.onload = function () {
            populateBatchDropdown();
        };

        function populateBatchDropdown() {
            const batchDropdown = document.getElementById('batchId');

            // Populate the dropdown
            batches.forEach(batch => {
                const option = document.createElement('option');
                option.value = batch.id;
                option.textContent = batch.name;
                batchDropdown.appendChild(option);
            });
        }

        function populateBatchDetails() {
            const selectedId = document.getElementById('batchId').value;
            const selectedBatch = batches.find(batch => batch.id === selectedId);

            if (selectedBatch) {
                document.getElementById('name').value = selectedBatch.name;
                document.getElementById('startDate').value = selectedBatch.startDate;
                document.getElementById('department').value = selectedBatch.department;
                document.getElementById('course').value = selectedBatch.course;
            } else {
                // Clear fields if no batch is selected
                document.getElementById('name').value = '';
                document.getElementById('startDate').value = '';
                document.getElementById('department').value = '';
                document.getElementById('course').value = '';
            }
        }
    </script>
</head>
<body>
<form action="/frontend_war/batch/updateBatch" method="post">
    <label for="batchId">Select Batch:</label>
    <select id="batchId" name="id" onchange="populateBatchDetails()" required>
        <option value="">-- Select Batch --</option>
    </select><br>

    <label for="name">Batch Name:</label>
    <input type="text" id="name" name="name" required><br>

    <label for="startDate">Start Date:</label>
    <input type="date" id="startDate" name="startDate" required><br>

    <label for="department">Department:</label>
    <input type="text" id="department" name="department" required><br>

    <label for="course">Course:</label>
    <input type="text" id="course" name="course" required><br>

    <button type="submit">Update Batch</button>
</form>
</body>
</html>
