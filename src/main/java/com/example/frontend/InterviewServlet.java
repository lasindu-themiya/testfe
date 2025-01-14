package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.example.frontend.dto.InterviewRequest;
import com.example.frontend.dto.InterviewResponse;
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

@WebServlet("/InterviewServlet")
public class InterviewServlet extends HttpServlet {

    private static final String GET_BATCHES_URL       = "http://localhost:8081/api/batch/getAll";
    private static final String GET_STUDENTS_BY_BATCH = "http://localhost:8081/api/student/getAll/";

    private static final String CREATE_INDIVIDUAL_URL = "http://localhost:8081/api/lecturer/interview/add/individual/";
    private static final String CREATE_BATCHWISE_URL  = "http://localhost:8081/api/lecturer/interview/add/batchwise/";
    private static final String UPDATE_URL            = "http://localhost:8081/api/lecturer/interview/update/";
    private static final String DELETE_URL            = "http://localhost:8081/api/lecturer/deleteInterview/";
    private static final String VIEW_ALL_URL          = "http://localhost:8081/api/lecturer/interview/view";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getBatches".equals(action)) {
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);
            request.getRequestDispatcher("interview/createInterview.jsp").forward(request, response);

        } else if ("loadStudents".equals(action)) {
            String batchId = request.getParameter("batchId");
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);

            List<Student> studentList = fetchAllStudents(batchId, request);
            request.setAttribute("studentList", studentList);
            request.getRequestDispatcher("interview/createInterview.jsp").forward(request, response);

        } else if ("list".equals(action)) {
            List<InterviewResponse> interviews = viewAllInterviews(request);
            request.setAttribute("interviewList", interviews);
            request.getRequestDispatcher("interview/listInterviews.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String eventId = request.getParameter("id");  // the event ID
            InterviewResponse itv = findInterviewByEventId(eventId, request);
            request.setAttribute("itv", itv);
            request.getRequestDispatcher("interview/editInterview.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            String eventId = request.getParameter("id");
            boolean success = deleteInterview(eventId, request);
            response.sendRedirect("InterviewServlet?action=list&msg=" + (success ? "Deleted" : "Failed"));

        } else {
            response.sendRedirect("InterviewServlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("create".equals(action)) {
            String batchId   = request.getParameter("batchId");
            String studentId = request.getParameter("studentId");
            String companyName = request.getParameter("companyName");
            String position = request.getParameter("position");
            String mode = request.getParameter("mode");

            InterviewRequest body = new InterviewRequest(companyName, position, mode);

            boolean success;
            if (studentId != null && !studentId.isEmpty()) {
                success = createIndividualInterview(studentId, body, request);
            } else {
                success = createBatchwiseInterview(batchId, body, request);
            }
            request.setAttribute("message", success ? "Interview created!" : "Failed to create interview.");
            request.getRequestDispatcher("interview/createInterview.jsp").forward(request, response);

        } else if ("update".equals(action)) {
            String eventId = request.getParameter("interviewId"); // hidden form
            String companyName = request.getParameter("companyName");
            String position    = request.getParameter("position");
            String mode        = request.getParameter("mode");

            InterviewRequest body = new InterviewRequest(companyName, position, mode);
            boolean success = updateInterview(eventId, body, request);
            response.sendRedirect("InterviewServlet?action=list&msg=" + (success ? "Updated" : "Failed"));
        }
    }

    // ========== BATCH & STUDENT ==========

    private List<Batch> fetchAllBatches(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_BATCHES_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            try (InputStream is = conn.getInputStream()) {
                Batch[] batchArray = new ObjectMapper().readValue(is, Batch[].class);
                return Arrays.asList(batchArray);
            }
        }
        throw new IOException("Failed to fetch batches. Code: " + conn.getResponseCode());
    }

    private List<Student> fetchAllStudents(String batchId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_STUDENTS_BY_BATCH + batchId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            try (InputStream is = conn.getInputStream()) {
                Student[] studentArray = new ObjectMapper().readValue(is, Student[].class);
                return Arrays.asList(studentArray);
            }
        }
        throw new IOException("Failed to fetch students. Code: " + conn.getResponseCode());
    }

    // ========== CREATE / UPDATE / DELETE / VIEW ==========

    private boolean createIndividualInterview(String studentId, InterviewRequest body, HttpServletRequest req)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_INDIVIDUAL_URL + studentId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }
        return (conn.getResponseCode() == 200);
    }

    private boolean createBatchwiseInterview(String batchId, InterviewRequest body, HttpServletRequest req)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_BATCHWISE_URL + batchId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }
        return (conn.getResponseCode() == 200);
    }

    private boolean updateInterview(String eventId, InterviewRequest body, HttpServletRequest req) throws IOException {
        // e.g. UPDATE_URL + E004
        HttpURLConnection conn = (HttpURLConnection) new URL(UPDATE_URL + eventId).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            new ObjectMapper().writeValue(os, body);
        }
        return (conn.getResponseCode() == 200);
    }

    private boolean deleteInterview(String eventId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(DELETE_URL + eventId).openConnection();
        conn.setRequestMethod("DELETE");
        addSessionCookie(conn, req);

        conn.connect();
        return (conn.getResponseCode() == 200 || conn.getResponseCode() == 204);
    }

    private List<InterviewResponse> viewAllInterviews(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_ALL_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == 200) {
            InterviewResponse[] arr = new ObjectMapper().readValue(conn.getInputStream(), InterviewResponse[].class);
            return Arrays.asList(arr);
        }
        throw new IOException("Failed to fetch interviews. Code: " + conn.getResponseCode());
    }

    private InterviewResponse findInterviewByEventId(String eventId, HttpServletRequest req) throws IOException {
        List<InterviewResponse> all = viewAllInterviews(req);
        for (InterviewResponse i : all) {
            if (i.getEventId().equals(eventId)) {
                return i;
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
