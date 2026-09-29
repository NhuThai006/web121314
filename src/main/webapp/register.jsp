<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - SQL Gateway</title>
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
            <h1>Register</h1>
        </div>
        <nav class="app-nav">
            <a href="index.jsp">SQL Gateway</a>
            <a href="login">Login</a>
            <a href="register" class="active">Register</a>
        </nav>
    </header>

    <main class="main-content">
        <section class="editor-section">
            <div class="section-header">
                <h2>Create account</h2>
                <p class="subtitle">Đăng ký → nhận OTP qua email → xác nhận mới tạo tài khoản</p>
            </div>

            <c:if test="${message != null}">
                <p class="mail-message">${message}</p>
            </c:if>

            <form action="register" method="post" accept-charset="UTF-8" class="email-form">
                <div class="form-row">
                    <label for="firstName">First name</label>
                    <input type="text" id="firstName" name="firstName"
                           value="${firstName}" required>
                </div>
                <div class="form-row">
                    <label for="lastName">Last name</label>
                    <input type="text" id="lastName" name="lastName"
                           value="${lastName}" required>
                </div>
                <div class="form-row">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           value="${email}" required>
                </div>
                <div class="form-row">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="form-row">
                    <label for="confirm">Confirm password</label>
                    <input type="password" id="confirm" name="confirm" required>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-execute">Register</button>
                    <a href="login" class="btn-clear" style="text-decoration:none;display:inline-flex;align-items:center;">Login</a>
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
