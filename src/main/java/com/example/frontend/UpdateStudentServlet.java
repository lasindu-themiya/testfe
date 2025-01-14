package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@WebServlet("/student/updateStudent")
public class UpdateStudentServlet extends HttpServlet {

    private static final String GET_ALL_BATCHES_URL = "http://localhost:8081/api/batch/getAll";
    private static final String GET_ALL_STUDENTS_URL = "http://localhost:8081/api/student/getAll/";
    private static final String UPDATE_STUDENT_URL = "http://localhost:8081/api/student/update/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Fetch all batches
            List<Batch> batches = fetchAllBatches();
            req.setAttribute("batches", batches);

            // If batchId is provided, fetch students for that batch
            String batchId = req.getParameter("batchId");
            if (batchId != null && !batchId.isEmpty()) {
                List<Student> students = fetchAllStudents(batchId);
                req.setAttribute("students", students);
            }

            req.getRequestDispatcher("/updateStudent.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data: " + e.getMessage());
        }
    }

    private List<Batch> fetchAllBatches() throws IOException {
        URL url = new URL(GET_ALL_BATCHES_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = connection.getInputStream()) {
            return Arrays.asList(mapper.readValue(is, Batch[].class));
        }
    }

    private List<Student> fetchAllStudents(String batchId) throws IOException {
        URL url = new URL(GET_ALL_STUDENTS_URL + batchId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = connection.getInputStream()) {
            return Arrays.asList(mapper.readValue(is, Student[].class));
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String studentId = req.getParameter("id");

            // Build Student object from form data
            Student student = new Student();
            student.setName(req.getParameter("name"));
            student.setEmail(req.getParameter("email"));
            student.setPassword(req.getParameter("password"));
            student.setYear(Integer.parseInt(req.getParameter("year")));
            student.setContact(req.getParameter("contact"));
            student.setPhoto(req.getParameter("photo"));
            student.setRole(req.getParameter("role"));

            // Convert Student object to JSON payload
            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(student);

            // Send update request to the backend
            boolean success = updateStudentInBackend(studentId, payload);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Student+updated+successfully");
            } else {
                req.setAttribute("error", "Failed to update student.");
                req.getRequestDispatcher("/updateStudent.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/updateStudent.jsp").forward(req, resp);
        }
    }

    private boolean updateStudentInBackend(String studentId, String payload) throws IOException {
        URL url = new URL(UPDATE_STUDENT_URL + studentId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes("utf-8"));
        }

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
