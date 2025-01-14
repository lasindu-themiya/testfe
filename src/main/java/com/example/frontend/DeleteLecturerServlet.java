package com.example.frontend;

import com.example.frontend.dto.Lecturer;
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

@WebServlet("/lecturer/deleteLecturer")
public class DeleteLecturerServlet extends HttpServlet {

    private static final String GET_ALL_LECTURERS_URL = "http://localhost:8081/api/lecturer/getAll";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Lecturer> lecturers = fetchAllLecturers();
            req.setAttribute("lecturers", lecturers);
            req.getRequestDispatcher("/deleteLecturer.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching lecturers: " + e.getMessage());
        }
    }

    private List<Lecturer> fetchAllLecturers() throws IOException {
        URL url = new URL(GET_ALL_LECTURERS_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = connection.getInputStream()) {
            return Arrays.asList(mapper.readValue(is, Lecturer[].class));
        }
    }

    private static final String BACKEND_URL = "http://localhost:8081/api/lecturer/delete/";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lecturerId = req.getParameter("id");

        if (lecturerId == null || lecturerId.trim().isEmpty()) {
            req.setAttribute("error", "Lecturer ID is required.");
            req.getRequestDispatcher("/deleteLecturer.jsp").forward(req, resp);
            return;
        }

        try {
            boolean isDeleted = deleteLecturerFromBackend(lecturerId);

            if (isDeleted) {
                resp.sendRedirect("/frontend_war/adminDashboard.jsp?message=Lecturer+deleted+successfully");
            } else {
                req.setAttribute("error", "Lecturer not found or could not be deleted.");
                req.getRequestDispatcher("/deleteLecturer.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "An error occurred: " + e.getMessage());
            req.getRequestDispatcher("/deleteLecturer.jsp").forward(req, resp);
        }
    }

    private boolean deleteLecturerFromBackend(String lecturerId) throws IOException {
        URL url = new URL(BACKEND_URL + lecturerId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
    }
}
