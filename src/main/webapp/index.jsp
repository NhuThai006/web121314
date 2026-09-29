<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sqlStatement == null}">
    <c:set var="sqlStatement" value="SELECT * FROM Users" scope="session" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SQL Gateway - PostgreSQL</title>
    <link rel="stylesheet" href="styles/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500&display=swap" rel="stylesheet">
</head>
<body>

<div class="app-container">
    <header class="app-header">
        <div class="logo">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
                <rect width="32" height="32" rx="8" fill="url(#grad1)"/>
                <path d="M8 12h16M8 16h16M8 20h12" stroke="white" stroke-width="2" stroke-linecap="round"/>
                <defs>
                    <linearGradient id="grad1" x1="0" y1="0" x2="32" y2="32">
                        <stop offset="0%" stop-color="#6366f1"/>
                        <stop offset="100%" stop-color="#8b5cf6"/>
                    </linearGradient>
                </defs>
            </svg>
            <h1>SQL Gateway</h1>
        </div>
        <nav class="app-nav">
            <a href="index.jsp" class="active">SQL Gateway</a>
            <a href="email">Email</a>
            <a href="login">Login</a>
            <a href="register">Register</a>
        </nav>
        <span class="badge">PostgreSQL</span>
    </header>

    <main class="main-content">
        <section class="editor-section">
            <div class="section-header">
                <h2>SQL Editor</h2>
                <p class="subtitle">Enter an SQL statement and click Execute</p>
            </div>
            <form action="sqlGateway" method="post" class="sql-form">
                <div class="textarea-wrapper">
                    <textarea id="sqlStatement" name="sqlStatement"
                              placeholder="Enter your SQL query here...">${sqlStatement}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-execute" id="btnExecute">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                            <path d="M4 2L14 8L4 14V2Z" fill="currentColor"/>
                        </svg>
                        Execute
                    </button>
                    <button type="reset" class="btn-clear" id="btnClear">
                        Clear
                    </button>
                </div>
            </form>
        </section>

        <section class="result-section">
            <div class="section-header">
                <h2>Result</h2>
            </div>
            <div class="result-container">
                <c:choose>
                    <c:when test="${sqlResult != null && sqlResult != ''}">
                        ${sqlResult}
                    </c:when>
                    <c:otherwise>
                        <p class="empty-state">Execute a query to see results here.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </main>

    <footer class="app-footer">
        <p>&copy; 2026 SQL Gateway &mdash; Murach's Java Servlets and JSP</p>
    </footer>
</div>

</body>
</html>