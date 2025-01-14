package com.example.frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/admin/toggleStatus")
public class ToggleAdminStatusServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/admin/toggleStatus/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String adminId = req.getParameter("id");
        String newStatus = req.getParameter("status");

        if (adminId == null || newStatus == null) {
            req.setAttribute("error", "Admin ID and status are required.");
            req.getRequestDispatcher("/toggleAdminStatus.jsp").forward(req, resp);
            return;
        }

        try {
            boolean isToggled = toggleAdminStatusOnBackend(adminId, newStatus);

            if (isToggled) {
                req.setAttribute("message", "Admin status updated successfully.");
            } else {
                req.setAttribute("error", "Failed to update admin status.");
            }
            req.getRequestDispatcher("/toggleAdminStatus.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/toggleAdminStatus.jsp").forward(req, resp);
        }
    }

    private boolean toggleAdminStatusOnBackend(String adminId, String newStatus) throws IOException {
        URL url = new URL(BACKEND_URL + adminId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Prepare request body
        String requestBody = "{\"status\":\"" + newStatus + "\"}";
        try (OutputStream os = connection.getOutputStream()) {
            os.write(requestBody.getBytes());
        }

        // Log backend response
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
