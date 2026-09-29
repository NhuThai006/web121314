<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Send Email - SQL Gateway</title>
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
            <h1>Send Email</h1>
        </div>
        <nav class="app-nav">
            <a href="index.jsp">SQL Gateway</a>
            <a href="email" class="active">Email</a>
            <a href="logout">Logout</a>
        </nav>
    </header>

    <main class="main-content">
        <section class="editor-section">
            <div class="section-header">
                <h2>Compose Email</h2>
                <p class="subtitle">Xin chào, <strong>${currentUser.fullName}</strong>
                    &lt;${currentUser.email}&gt;</p>
                <p class="subtitle help-hint">
                    <strong>To</strong>: người nhận chính ·
                    <strong>CC</strong>: gửi kèm (mọi người đều thấy) ·
                    <strong>BCC</strong>: gửi bí mật (người khác không thấy địa chỉ này)
                </p>
            </div>

            <c:if test="${message != null}">
                <p class="mail-message">${message}</p>
            </c:if>

            <form action="email" method="post" accept-charset="UTF-8" class="email-form">
                <div class="form-row">
                    <label for="from">From (Gmail hệ thống)</label>
                    <input type="email" id="from" name="from"
                           value="${from}" readonly>
                </div>

                <div class="form-row">
                    <label>Tài khoản đang đăng nhập</label>
                    <p class="user-card">
                        <strong>${currentUser.fullName}</strong><br>
                        ${currentUser.email}
                    </p>
                </div>

                <div class="form-row">
                    <label for="to">To</label>
                    <input type="text" id="to" name="to"
                           value="${to}" required
                           placeholder="email người nhận">
                </div>
                <div class="form-row">
                    <label for="cc">CC</label>
                    <input type="text" id="cc" name="cc"
                           value="${cc}"
                           placeholder="Optional, comma-separated">
                </div>
                <div class="form-row">
                    <label for="bcc">BCC</label>
                    <input type="text" id="bcc" name="bcc"
                           value="${bcc}"
                           placeholder="Optional, comma-separated">
                </div>
                <div class="form-row">
                    <label for="subject">Subject</label>
                    <input type="text" id="subject" name="subject"
                           value="${subject}" required>
                </div>
                <div class="form-row">
                    <label for="body">Body</label>
                    <textarea id="body" name="body" rows="8"
                              required>${body}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-execute">Send Email</button>
                    <button type="reset" class="btn-clear">Clear</button>
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
