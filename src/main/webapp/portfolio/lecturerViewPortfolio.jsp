<%@ page import="java.util.List" %>
<%@ page import="com.example.frontend.dto.Student" %>
<%@ page import="com.example.frontend.dto.Batch" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>View Portfolio</title>
    <script>
        // If batchList is empty, redirect to the servlet to load them
        window.onload = function() {
            var batchListSize = <%= (request.getAttribute("batchList") == null) ? 0
                                     : ((List)request.getAttribute("batchList")).size() %>;
            if (batchListSize === 0) {
                window.location.href = '<%= request.getContextPath() %>/PortfolioViewServlet?action=getBatches';
            }
        };

        function loadStudentsForBatch() {
            const batchId = document.getElementById('batchId').value;
            if (batchId) {
                window.location.href = '<%= request.getContextPath() %>/PortfolioViewServlet?action=loadStudents&batchId=' + batchId;
            }
        }

        function viewPortfolio() {
            const studentId = document.getElementById('studentId').value;
            if (studentId) {
                window.location.href = 'PortfolioViewServlet?action=viewPortfolio&studentId=' + studentId;
            }
        }
    </script>
</head>
<body>
<h1>View Portfolio</h1>

<!-- We assume the servlet sets "batchList" and optionally "studentList" -->
<%
    List<Batch> batchList = (List<Batch>) request.getAttribute("batchList");
    List<Student> studentList = (List<Student>) request.getAttribute("studentList");
%>
<!-- Batch Dropdown -->
<form>
    <c:choose>
        <c:when test="${not empty batchList}">
            <select id="batchId" name="batchId" onchange="loadStudentsForBatch()">
            <option value="">-- Select Batch --</option>
            <c:forEach var="batch" items="${batchList}">
                <option value="${batch.id}">${batch.name}</option>
            </c:forEach>
            </select>

        </c:when>
        <c:otherwise>
            <p>No batches available. Please try again later.</p>
        </c:otherwise>
    </c:choose>
    <br/><br/>

    <select id="studentId" name="studentId" onchange="viewPortfolio()">
        <option value="">-- Select Student --</option>
        <c:forEach var="student" items="${studentList}">
            <option value="${student.id}">${student.name}</option>
        </c:forEach>
    </select>


</form>

<br/>
<c:if test="${not empty portfolioList}">
    <h2>Portfolios</h2>
    <c:forEach var="portfolio" items="${portfolioList}">
        <div style="border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;">
            <p><b>Name:</b> ${portfolio.name}</p>
            <p><b>About:</b> ${portfolio.about}</p>
            <p><b>Projects:</b> ${portfolio.projects}</p>
            <p><b>Photo:</b> <img src="${portfolio.photo}" alt="Portfolio Photo" style="max-width: 200px;" /></p>
            <p><b>Bio:</b> ${portfolio.bio}</p>
        </div>
    </c:forEach>
</c:if>

<c:if test="${empty portfolioList && not empty studentList}">
    <p>No portfolios found for the selected student.</p>
</c:if>

<a href="lecturerDashboard.jsp">Back to Dashboard</a>
</body>
</html>
