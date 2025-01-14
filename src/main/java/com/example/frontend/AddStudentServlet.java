package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@WebServlet("/student/addStudent")
public class AddStudentServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/student/add/";

    private static final String GET_ALL_BATCHES_URL = "http://localhost:8081/api/batch/getAll";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Batch> batches = fetchAllBatches();
            req.setAttribute("batches", batches);
            req.getRequestDispatcher("/addStudent.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching batches: " + e.getMessage());
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



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String batchId = req.getParameter("batchId");

        // Retrieve Admin ID from session
        String adminId = (String) req.getSession().getAttribute("userAId");
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");

        System.out.println("Admin ID from session: " + adminId); // Debugging
        System.out.println("Backend Session ID: " + backendSessionId); // Debugging

        if (adminId == null || adminId.isEmpty()) {
            req.setAttribute("error", "Unauthorized: Admin must be logged in to add a Student.");
            req.getRequestDispatcher("/addStudent.jsp").forward(req, resp);
            return;
        }

        try {
            Student student = new Student();
            student.setName(req.getParameter("name"));
            student.setEmail(req.getParameter("email"));
            student.setPassword(req.getParameter("password"));
            student.setYear(Integer.parseInt(req.getParameter("year")));
            student.setContact(req.getParameter("contact"));
            student.setPhoto(req.getParameter("photo"));

            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(student);

            boolean success = addStudentToBackend(batchId, payload,backendSessionId);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Student+added+successfully");
            } else {
                req.setAttribute("error", "Failed to add student.");
                req.getRequestDispatcher("/addStudent.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/addStudent.jsp").forward(req, resp);
        }
    }

    private boolean addStudentToBackend(String batchId, String payload, String backendSessionId) throws IOException {
        URL url = new URL(BACKEND_URL + batchId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Attach backend session cookie
        if (backendSessionId != null) {
            connection.setRequestProperty("Cookie", backendSessionId);
        }

        try (OutputStream os = connection.getOutputStream()) {
            os.write(payload.getBytes("utf-8"));
        }

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
