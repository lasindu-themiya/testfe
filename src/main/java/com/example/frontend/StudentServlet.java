package com.example.frontend;

import com.example.frontend.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private static final String BASE_URL = "http://localhost:8081/api/student";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "viewAnnouncements":
                handleViewAnnouncements(request, response);
                break;
            case "viewWorkshops":
                handleViewWorkshops(request, response);
                break;
            case "viewInterviews":
                handleViewInterviews(request, response);
                break;
            default:
                response.sendRedirect("studentDashboard.jsp");
                break;
        }
    }

    private void handleViewAnnouncements(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = BASE_URL + "/announcement/student";
        List<AnnouncementResponse> announcements = fetchFromBackend(url, request, new TypeReference<List<AnnouncementResponse>>() {});
        request.setAttribute("announcements", announcements);
        request.getRequestDispatcher("student/viewAnnouncements.jsp").forward(request, response);
    }

    private void handleViewWorkshops(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = BASE_URL + "/workshop/student";
        List<WorkshopResponse> workshops = fetchFromBackend(url, request, new TypeReference<List<WorkshopResponse>>() {});
        request.setAttribute("workshops", workshops);
        request.getRequestDispatcher("student/viewWorkshops.jsp").forward(request, response);
    }

    private void handleViewInterviews(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String url = BASE_URL + "/interview/student";
        List<InterviewResponse> interviews = fetchFromBackend(url, request, new TypeReference<List<InterviewResponse>>() {});
        request.setAttribute("interviews", interviews);
        request.getRequestDispatcher("student/viewInterviews.jsp").forward(request, response);
    }

    // Generic method for fetching from backend
    private <T> T fetchFromBackend(String url, HttpServletRequest request, TypeReference<T> typeReference) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Cookie", (String) request.getSession().getAttribute("backendSessionId"));

        if (conn.getResponseCode() == 200) {
            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(is, typeReference);
            }
        }
        throw new IOException("Failed to fetch data from backend. Response code: " + conn.getResponseCode());
    }
}
