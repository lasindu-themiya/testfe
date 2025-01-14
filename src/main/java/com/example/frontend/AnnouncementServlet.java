package com.example.frontend;

import com.example.frontend.dto.AnnouncementResponse;
import com.example.frontend.dto.AnnouncementRequest;
import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebServlet("/AnnouncementServlet")
public class AnnouncementServlet extends HttpServlet {

    // ===================== BACKEND ENDPOINTS =====================
    private static final String GET_BATCHES_URL         = "http://localhost:8081/api/batch/getAll";
    private static final String GET_STUDENTS_BY_BATCH   = "http://localhost:8081/api/student/getAll/";

    private static final String CREATE_INDIVIDUAL_URL   = "http://localhost:8081/api/lecturer/announcement/add/individual/";
    private static final String CREATE_BATCHWISE_URL    = "http://localhost:8081/api/lecturer/announcement/add/batchwise/";

    private static final String UPDATE_URL              = "http://localhost:8081/api/lecturer/announcement/update/";   // + {eventId}
    private static final String DELETE_URL              = "http://localhost:8081/api/lecturer/deleteAnnouncement/";    // + {eventId}
    private static final String VIEW_ALL_URL            = "http://localhost:8081/api/lecturer/announcement/view";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getBatches".equals(action)) {
            // Load all batches
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);
            request.getRequestDispatcher("announcement/createAnnouncement.jsp").forward(request, response);

        } else if ("loadStudents".equals(action)) {
            String batchId = request.getParameter("batchId");
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);

            List<Student> studentList = fetchAllStudents(batchId, request);
            request.setAttribute("studentList", studentList);

            request.getRequestDispatcher("announcement/createAnnouncement.jsp").forward(request, response);

        } else if ("list".equals(action)) {
            // Fetch & display announcements
            List<AnnouncementResponse> announcements = viewAllAnnouncements(request);
            request.setAttribute("announcementList", announcements);
            request.getRequestDispatcher("announcement/listAnnouncements.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            // e.g. ?action=edit&id=E004
            String eventId = request.getParameter("id");
            AnnouncementResponse ann = findAnnouncementByEventId(eventId, request);
            request.setAttribute("ann", ann);
            request.getRequestDispatcher("announcement/editAnnouncement.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            String announcementId = request.getParameter("id"); // Use announcement ID
            try {
                boolean success = deleteAnnouncement(announcementId, request);
                if (success) {
                    response.sendRedirect("AnnouncementServlet?action=list&msg=Deleted successfully");
                } else {
                    response.sendRedirect("AnnouncementServlet?action=list&msg=Failed to delete announcement");
                }
            } catch (Exception e) {
                response.sendRedirect("AnnouncementServlet?action=list&msg=Error occurred during deletion");
                e.printStackTrace();
            }
        }
        else {
            // Default: show list
            response.sendRedirect("AnnouncementServlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("create".equals(action)) {
            String batchId = request.getParameter("batchId");
            String studentId = request.getParameter("studentId");
            String content = request.getParameter("content");

            AnnouncementRequest body = new AnnouncementRequest(content);

            boolean success;
            if (studentId != null && !studentId.isEmpty()) {
                success = createIndividualAnnouncement(studentId, body, request);
            } else {
                success = createBatchwiseAnnouncement(batchId, body, request);
            }
            request.setAttribute("message", success ? "Announcement created!" : "Failed to create announcement.");
            request.getRequestDispatcher("announcement/createAnnouncement.jsp").forward(request, response);

        } else if ("update".equals(action)) {
            // e.g. hidden form input name="announcementId" => eventId
            String eventId = request.getParameter("announcementId");
            String content = request.getParameter("content");

            AnnouncementRequest body = new AnnouncementRequest(content);

            boolean success = updateAnnouncement(eventId, body, request);
            response.sendRedirect("AnnouncementServlet?action=list&msg=" + (success ? "Updated" : "Failed"));
        }
    }

    // ================== BATCH & STUDENT HELPERS ==================
    private List<Batch> fetchAllBatches(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_BATCHES_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                Batch[] batchArray = mapper.readValue(is, Batch[].class);
                return Arrays.asList(batchArray);
            }
        }
        throw new IOException("Failed to fetch batches. Code: " + responseCode);
    }

    private List<Student> fetchAllStudents(String batchId, HttpServletRequest req) throws IOException {
        String fullUrl = GET_STUDENTS_BY_BATCH + batchId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = conn.getInputStream()) {
                Student[] students = mapper.readValue(is, Student[].class);
                return Arrays.asList(students);
            }
        }
        throw new IOException("Failed to fetch students. Code: " + conn.getResponseCode());
    }

    // ================== CREATE / UPDATE / DELETE / VIEW ==================
    private boolean createIndividualAnnouncement(String studentId, AnnouncementRequest body, HttpServletRequest req)
            throws IOException {
        String fullUrl = CREATE_INDIVIDUAL_URL + studentId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }

        return (conn.getResponseCode() == 200);
    }

    private boolean createBatchwiseAnnouncement(String batchId, AnnouncementRequest body, HttpServletRequest req)
            throws IOException {
        String fullUrl = CREATE_BATCHWISE_URL + batchId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }

        return (conn.getResponseCode() == 200);
    }

    private boolean updateAnnouncement(String eventId, AnnouncementRequest body, HttpServletRequest req)
            throws IOException {
        // e.g. http://localhost:8081/api/lecturer/announcement/update/E004
        String fullUrl = UPDATE_URL + eventId;
        HttpURLConnection conn = (HttpURLConnection) new URL(fullUrl).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }

        return (conn.getResponseCode() == 200);
    }

    private boolean deleteAnnouncement(String announcementId, HttpServletRequest req) throws IOException {
        String deleteUrl = DELETE_URL + announcementId; // Use announcement ID
        HttpURLConnection conn = (HttpURLConnection) new URL(deleteUrl).openConnection();
        conn.setRequestMethod("DELETE");
        addSessionCookie(conn, req);

        conn.connect();
        int responseCode = conn.getResponseCode();

        if (responseCode == 200 || responseCode == 204) {
            return true;
        } else {
            debugError(conn);
            return false;
        }
    }

    private List<AnnouncementResponse> viewAllAnnouncements(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_ALL_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream is = conn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                AnnouncementResponse[] arr = mapper.readValue(is, AnnouncementResponse[].class);
                return Arrays.asList(arr);
            }
        }
        throw new IOException("Failed to fetch announcements. Code: " + conn.getResponseCode());
    }

    private AnnouncementResponse findAnnouncementByEventId(String eventId, HttpServletRequest req) throws IOException {
        // fetch all, find the one with eventId == param
        List<AnnouncementResponse> all = viewAllAnnouncements(req);
        for (AnnouncementResponse a : all) {
            if (eventId.equals(a.getEventId())) {
                return a;
            }
        }
        return null;
    }

    // ========== UTILITY ==========

    private void addSessionCookie(HttpURLConnection conn, HttpServletRequest req) {
        String backendSessionId = (String) req.getSession().getAttribute("backendSessionId");
        if (backendSessionId != null && !backendSessionId.isEmpty()) {
            conn.setRequestProperty("Cookie", backendSessionId);
        }
    }

    private void debugError(HttpURLConnection conn) throws IOException {
        try (InputStream err = conn.getErrorStream()) {
            if (err != null) {
                String error = new String(err.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Backend error: " + error);
            }
        }
    }
}
