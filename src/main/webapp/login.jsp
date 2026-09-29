<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - SQL Gateway</title>
    <link rel="stylesheet" href="styles/main.css">
</head>
<body>

<div class="app-container">
    <header class="app-header">
        <div class="logo">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
                <rect width="32" height="32" rx="8" fill="#007B88"/>
                <path d="M8 12h16M8 16h16M8 20h12" stroke="white" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <h1>Login</h1>
        </div>
        <nav class="app-nav">
            <a href="index.jsp">SQL Gateway</a>
            <a href="login" class="active">Login</a>
            <a href="register">Register</a>
        </nav>
    </header>

    <main class="main-content">
        <section class="editor-section">
            <div class="section-header">
                <h2>Sign in</h2>
                <p class="subtitle">Đăng nhập để gửi email (To / CC / BCC)</p>
            </div>

            <c:if test="${message != null}">
                <p class="mail-message">${message}</p>
            </c:if>

            <form action="login" method="post" class="email-form">
                <div class="form-row">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="${email}" required>
                </div>
                <div class="form-row">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-execute">Login</button>
                    <a href="register" class="btn-clear" style="text-decoration:none;display:inline-flex;align-items:center;">Register</a>
                </div>
            </form>
        </section>
    </main>

    <footer class="app-footer">
        <p>&copy; 2026 SQL Gateway &mdash; Murach's Java Servlets and JSP</p>
    </footer>
</div>

</body>
</html>
