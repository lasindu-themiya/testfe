package com.example.frontend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/admin/deleteAdmin")
public class DeleteAdminServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/admin/delete/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String adminId = req.getParameter("id");

        if (adminId == null || adminId.trim().isEmpty()) {
            req.setAttribute("error", "Admin ID is required.");
            req.getRequestDispatcher("/deleteAdmin.jsp").forward(req, resp);
            return;
        }

        try {
            boolean isDeleted = deleteAdminFromBackend(adminId);

            if (isDeleted) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Admin+deleted+successfully");
            } else {
                req.setAttribute("error", "Admin not found or could not be deleted.");
                req.getRequestDispatcher("/deleteAdmin.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/deleteAdmin.jsp").forward(req, resp);
        }
    }

    private boolean deleteAdminFromBackend(String adminId) throws IOException {
        URL url = new URL(BACKEND_URL + adminId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        // Log backend response
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
