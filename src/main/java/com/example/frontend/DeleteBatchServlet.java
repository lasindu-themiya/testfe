package com.example.frontend;


import com.example.frontend.dto.Batch;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@WebServlet("/batch/deleteBatch")
public class DeleteBatchServlet extends HttpServlet {

    private static final String GET_ALL_BATCHES_URL = "http://localhost:8081/api/batch/getAll";
    private static final String BACKEND_URL = "http://localhost:8081/api/batch/delete/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Batch> batches = fetchAllBatches();
            req.setAttribute("batches", batches);
            req.getRequestDispatcher("/deleteBatch.jsp").forward(req, resp);
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
        String batchId = req.getParameter("id");

        if (batchId == null || batchId.trim().isEmpty()) {
            req.setAttribute("error", "Batch ID is required.");
            req.getRequestDispatcher("/deleteBatch.jsp").forward(req, resp);
            return;
        }

        try {
            boolean isDeleted = deleteBatchFromBackend(batchId);

            if (isDeleted) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Batch+deleted+successfully");
            } else {
                req.setAttribute("error", "Batch not found or could not be deleted.");
                req.getRequestDispatcher("/deleteBatch.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/deleteBatch.jsp").forward(req, resp);
        }
    }

    private boolean deleteBatchFromBackend(String batchId) throws IOException {
        URL url = new URL(BACKEND_URL + batchId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}

