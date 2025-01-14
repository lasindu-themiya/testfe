package com.example.frontend;

import com.example.frontend.dto.Batch;
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

@WebServlet("/batch/updateBatch")
public class UpdateBatchServlet extends HttpServlet {

    private static final String GET_ALL_BATCHES_URL = "http://localhost:8081/api/batch/getAll";
    private static final String BACKEND_URL = "http://localhost:8081/api/batch/update/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Fetch all batches from the backend
            List<Batch> batches = fetchAllBatches();
            System.out.println("Batches fetched: " + batches); // Debugging

            // Set the batches list in the request attribute
            req.setAttribute("batches", batches);

            // Forward to the JSP
            req.getRequestDispatcher("/updateBatch.jsp").forward(req, resp);
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
        try {
            String batchId = req.getParameter("id");
            Batch batch = new Batch();
            batch.setName(req.getParameter("name"));
            batch.setStartDate(req.getParameter("startDate"));
            batch.setDepartment(req.getParameter("department"));
            batch.setCourse(req.getParameter("course"));

            ObjectMapper mapper = new ObjectMapper();
            String payload = mapper.writeValueAsString(batch);

            boolean success = updateBatchInBackend(batchId, payload);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Batch+updated+successfully");
            } else {
                req.setAttribute("error", "Failed to update batch.");
                req.getRequestDispatcher("/updateBatch.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/updateBatch.jsp").forward(req, resp);
        }
    }

    private boolean updateBatchInBackend(String batchId, String payload) throws IOException {
        URL url = new URL(BACKEND_URL + batchId);
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
