package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Lecturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
@WebServlet("/batch/addBatch")
public class AddBatchServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/batch/add";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/addBatch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String adminId = (String) req.getSession().getAttribute("userAId");
            String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");

            System.out.println("Admin ID from session: " + adminId); // Debugging
            System.out.println("Backend Session ID: " + backendSessionId); // Debugging

            if (adminId == null || adminId.isEmpty()) {
                req.setAttribute("error", "Unauthorized: Admin must be logged in to add a batch.");
                req.getRequestDispatcher("/addBatch.jsp").forward(req, resp);
                return;
            }

            Batch batch = new Batch();
            batch.setId(req.getParameter("id"));
            batch.setName(req.getParameter("name"));
            batch.setStartDate(req.getParameter("startDate"));
            batch.setDepartment(req.getParameter("department"));
            batch.setCourse(req.getParameter("course"));

            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(batch);

            boolean success = sendBatchToBackend(payload,backendSessionId);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Batch+added+successfully");
            } else {
                req.setAttribute("error", "Failed to add batch.");
                req.getRequestDispatcher("/addBatch.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/addBatch.jsp").forward(req, resp);
        }
    }

    private boolean sendBatchToBackend(String payload,  String backendSessionId) throws IOException {
        URL url = new URL(BACKEND_URL);
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

