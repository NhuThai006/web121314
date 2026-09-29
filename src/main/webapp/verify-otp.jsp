<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận OTP - SQL Gateway</title>
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
            <h1>Xác nhận OTP</h1>
        </div>
        <nav class="app-nav">
            <a href="register">Register</a>
            <a href="login">Login</a>
        </nav>
    </header>

    <main class="main-content">
        <section class="editor-section">
            <div class="section-header">
                <h2>Nhập mã OTP</h2>
                <p class="subtitle">
                    Mã đã gửi tới email: <strong>${pendingEmail}</strong>
                    (hiệu lực 5 phút)
                </p>
            </div>

            <c:if test="${message != null}">
                <p class="mail-message">${message}</p>
            </c:if>

            <c:if test="${otpFallback != null}">
                <p class="user-card">
                    SMTP localhost chưa chạy nên không gửi được mail.<br>
                    Mã OTP để test: <strong>${otpFallback}</strong>
                </p>
            </c:if>

            <form action="verify-otp" method="post" class="email-form">
                <div class="form-row">
                    <label for="otp">Mã OTP (6 số)</label>
                    <input type="text" id="otp" name="otp"
                           maxlength="6" pattern="[0-9]{6}"
                           placeholder="Nhập OTP từ email" required>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-execute">Xác nhận</button>
                    <a href="register" class="btn-clear"
                       style="text-decoration:none;display:inline-flex;align-items:center;">Đăng ký lại</a>
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
