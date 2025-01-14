package com.example.frontend;

import com.example.frontend.dto.AdminUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/admin/updateAdmin")
public class UpdateAdminServlet extends HttpServlet {

    private static final String BACKEND_URL = "http://localhost:8081/api/admin/update/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Populate AdminUpdateDTO from the form data
            AdminUpdateDTO adminUpdateDTO = new AdminUpdateDTO();
            adminUpdateDTO.setId(req.getParameter("id"));
            adminUpdateDTO.setUserName(req.getParameter("userName"));
            adminUpdateDTO.setEmail(req.getParameter("email"));
            adminUpdateDTO.setPassword(req.getParameter("password"));
            adminUpdateDTO.setStatus(req.getParameter("status"));

            // Call backend API to update admin
            boolean success = updateAdminInBackend(adminUpdateDTO);

            if (success) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Admin+updated+successfully");
            } else {
                req.setAttribute("error", "Failed to update admin.");
                req.getRequestDispatcher("/updateAdmin.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/updateAdmin.jsp").forward(req, resp);
        }
    }

    private boolean updateAdminInBackend(AdminUpdateDTO adminUpdateDTO) throws IOException {
        URL url = new URL(BACKEND_URL + adminUpdateDTO.getId());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Send JSON data to backend
        ObjectMapper mapper = new ObjectMapper();
        try (OutputStream os = connection.getOutputStream()) {
            os.write(mapper.writeValueAsBytes(adminUpdateDTO));
        }

        // Log backend response
        int responseCode = connection.getResponseCode();
        System.out.println("Backend Response Code: " + responseCode);

        return responseCode == HttpURLConnection.HTTP_OK;
    }
}
