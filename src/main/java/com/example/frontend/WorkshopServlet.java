package com.example.frontend;

import com.example.frontend.dto.Batch;
import com.example.frontend.dto.Student;
import com.example.frontend.dto.WorkshopDTO;
import com.example.frontend.dto.WorkshopRequest;
import com.example.frontend.dto.WorkshopResponse;
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

@WebServlet("/WorkshopServlet")
public class WorkshopServlet extends HttpServlet {

    private static final String GET_BATCHES_URL       = "http://localhost:8081/api/batch/getAll";
    private static final String GET_STUDENTS_BY_BATCH = "http://localhost:8081/api/student/getAll/";

    private static final String CREATE_INDIVIDUAL_URL = "http://localhost:8081/api/lecturer/workshop/add/individual/";
    private static final String CREATE_BATCHWISE_URL  = "http://localhost:8081/api/lecturer/workshop/add/batchwise/";

    private static final String UPDATE_URL            = "http://localhost:8081/api/lecturer/workshop/update/";
    private static final String DELETE_URL            = "http://localhost:8081/api/lecturer/deleteWorkshop/";
    private static final String VIEW_ALL_URL          = "http://localhost:8081/api/lecturer/workshop/view";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("getBatches".equals(action)) {
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);
            request.getRequestDispatcher("workshop/createWorkshop.jsp").forward(request, response);

        } else if ("loadStudents".equals(action)) {
            String batchId = request.getParameter("batchId");
            List<Batch> batchList = fetchAllBatches(request);
            request.setAttribute("batchList", batchList);

            List<Student> studentList = fetchAllStudents(batchId, request);
            request.setAttribute("studentList", studentList);
            request.getRequestDispatcher("workshop/createWorkshop.jsp").forward(request, response);

        } else if ("list".equals(action)) {
            List<WorkshopResponse> workshopList = viewAllWorkshops(request);
            request.setAttribute("workshopList", workshopList);
            request.getRequestDispatcher("workshop/listWorkshops.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            String eventId = request.getParameter("id");
            WorkshopResponse wk = findWorkshopByEventId(eventId, request);
            request.setAttribute("wk", wk);
            request.getRequestDispatcher("workshop/editWorkshop.jsp").forward(request, response);

        } else if ("delete".equals(action)) {
            String eventId = request.getParameter("id");
            boolean success = deleteWorkshop(eventId, request);
            response.sendRedirect("WorkshopServlet?action=list&msg=" + (success ? "Deleted" : "Failed"));

        } else {
            response.sendRedirect("WorkshopServlet?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("create".equals(action)) {
            String batchId   = request.getParameter("batchId");
            String studentId = request.getParameter("studentId");
            String type      = request.getParameter("type");
            String contact   = request.getParameter("contact");

            WorkshopRequest body = new WorkshopRequest(type, contact);

            boolean success;
            if (studentId != null && !studentId.isEmpty()) {
                success = createIndividualWorkshop(studentId, body, request);
            } else {
                success = createBatchwiseWorkshop(batchId, body, request);
            }

            request.setAttribute("message", success ? "Workshop created successfully!" : "Failed to create workshop.");
            request.getRequestDispatcher("workshop/createWorkshop.jsp").forward(request, response);

        } else if ("update".equals(action)) {
            String eventId = request.getParameter("workshopId");
            String type    = request.getParameter("type");
            String contact = request.getParameter("contact");

            WorkshopRequest body = new WorkshopRequest(type, contact);
            boolean success = updateWorkshop(eventId, body, request);
            response.sendRedirect("WorkshopServlet?action=list&msg=" + (success ? "Updated" : "Failed"));
        }
    }

    // ========== BATCH & STUDENT UTILS ==========

    private List<Batch> fetchAllBatches(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_BATCHES_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = conn.getInputStream()) {
                Batch[] batchArray = mapper.readValue(is, Batch[].class);
                return Arrays.asList(batchArray);
            }
        }
        throw new IOException("Failed to fetch batches, code: " + conn.getResponseCode());
    }

    private List<Student> fetchAllStudents(String batchId, HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(GET_STUDENTS_BY_BATCH + batchId).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ObjectMapper mapper = new ObjectMapper();
            try (InputStream is = conn.getInputStream()) {
                Student[] studentArr = mapper.readValue(is, Student[].class);
                return Arrays.asList(studentArr);
            }
        }
        throw new IOException("Failed to fetch students for batch " + batchId);
    }

    // ========== CREATE / UPDATE / DELETE / VIEW ==========

    private boolean createIndividualWorkshop(String studentId, WorkshopRequest body, HttpServletRequest req)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_INDIVIDUAL_URL + studentId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        new ObjectMapper().writeValue(conn.getOutputStream(), body);
        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
    }

    private boolean createBatchwiseWorkshop(String batchId, WorkshopRequest body, HttpServletRequest req)
            throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(CREATE_BATCHWISE_URL + batchId).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        new ObjectMapper().writeValue(conn.getOutputStream(), body);
        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
    }

    private boolean updateWorkshop(String eventId, WorkshopRequest body, HttpServletRequest req) throws IOException {
        // e.g. http://localhost:8081/api/lecturer/workshop/update/E007
        HttpURLConnection conn = (HttpURLConnection) new URL(UPDATE_URL + eventId).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        addSessionCookie(conn, req);

        conn.setDoOutput(true);
        new ObjectMapper().writeValue(conn.getOutputStream(), body);
        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK);
    }

    private boolean deleteWorkshop(String eventId, HttpServletRequest req) throws IOException {
        // e.g. http://localhost:8081/api/lecturer/deleteWorkshop/E007
        HttpURLConnection conn = (HttpURLConnection) new URL(DELETE_URL + eventId).openConnection();
        conn.setRequestMethod("DELETE");
        addSessionCookie(conn, req);

        conn.connect();
        return (conn.getResponseCode() == HttpURLConnection.HTTP_OK
                || conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    private List<WorkshopResponse> viewAllWorkshops(HttpServletRequest req) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(VIEW_ALL_URL).openConnection();
        conn.setRequestMethod("GET");
        addSessionCookie(conn, req);

        conn.connect();
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            WorkshopResponse[] arr = new ObjectMapper().readValue(conn.getInputStream(), WorkshopResponse[].class);
            return Arrays.asList(arr);
        }
        throw new IOException("Failed to fetch workshops. Code: " + conn.getResponseCode());
    }

    private WorkshopResponse findWorkshopByEventId(String eventId, HttpServletRequest req) throws IOException {
        List<WorkshopResponse> all = viewAllWorkshops(req);
        for (WorkshopResponse w : all) {
            if (w.getEventId().equals(eventId)) {
                return w;
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
                String errorMsg = new String(err.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("Backend error: " + errorMsg);
            }
        }
    }
}
