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

@WebServlet("/student/deleteStudent")
public class DeleteStudentServlet extends HttpServlet {

    private static final String GET_ALL_BATCHES_URL = "http://localhost:8081/api/batch/getAll";
    private static final String GET_ALL_STUDENTS_URL = "http://localhost:8081/api/student/getAll/";
    private static final String DELETE_STUDENT_URL = "http://localhost:8081/api/student/delete/";

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

            req.getRequestDispatcher("/deleteStudent.jsp").forward(req, resp);
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
        String studentId = req.getParameter("id");

        if (studentId == null || studentId.trim().isEmpty()) {
            req.setAttribute("error", "Student ID is required.");
            req.getRequestDispatcher("/deleteStudent.jsp").forward(req, resp);
            return;
        }

        try {
            boolean isDeleted = deleteStudentFromBackend(studentId);

            if (isDeleted) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Student+deleted+successfully");
            } else {
                req.setAttribute("error", "Student not found or could not be deleted.");
                req.getRequestDispatcher("/deleteStudent.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/deleteStudent.jsp").forward(req, resp);
        }
    }

    private boolean deleteStudentFromBackend(String studentId) throws IOException {
        URL url = new URL(DELETE_STUDENT_URL + studentId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        return connection.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT;
    }
}
