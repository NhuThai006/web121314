package murach.email;

import java.io.*;
import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.http.*;

public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null
                || session.getAttribute("pendingRegistration") == null) {
            response.sendRedirect("register");
            return;
        }

        forwardPage(request, response, session);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null
                || session.getAttribute("pendingRegistration") == null) {
            response.sendRedirect("register");
            return;
        }

        PendingRegistration pending =
                (PendingRegistration) session.getAttribute("pendingRegistration");
        String otpInput = request.getParameter("otp");

        if (pending.isExpired()) {
            session.removeAttribute("pendingRegistration");
            session.removeAttribute("showOtpFallback");
            request.setAttribute("message",
                    "Mã OTP đã hết hạn. Vui lòng đăng ký lại.");
            getServletContext()
                    .getRequestDispatcher("/register.jsp")
                    .forward(request, response);
            return;
        }

        if (!pending.matchesOtp(otpInput)) {
            session.setAttribute("otpMessage", "Mã OTP không đúng. Thử lại.");
            response.sendRedirect("verify-otp");
            return;
        }

        boolean created = AccountDAO.register(
                pending.getEmail(),
                pending.getPassword(),
                pending.getFirstName(),
                pending.getLastName());

        if (!created) {
            session.setAttribute("otpMessage",
                    "Không tạo được tài khoản. Email có thể đã tồn tại.");
            response.sendRedirect("verify-otp");
            return;
        }

        User user = AccountDAO.login(pending.getEmail(), pending.getPassword());
        session.removeAttribute("pendingRegistration");
        session.removeAttribute("showOtpFallback");
        session.removeAttribute("otpMessage");
        session.setAttribute("user", user);

        String welcomeStatus = sendWelcome(user);
        session.setAttribute("message",
                "Xác nhận OTP thành công. " + welcomeStatus);

        response.sendRedirect("email");
    }

    private String sendWelcome(User user) {
        try {
            MailUtil.sendWelcomeMail(user);
            return "Đã gửi welcome email tới " + user.getEmail() + ".";
        } catch (MessagingException e) {
            log(e.toString());
            return "Welcome email gửi thất bại: " + e.getMessage();
        }
    }

    private void forwardPage(HttpServletRequest request,
            HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {

        Object msg = session.getAttribute("otpMessage");
        if (msg != null) {
            request.setAttribute("message", msg);
            session.removeAttribute("otpMessage");
        }

        PendingRegistration pending =
                (PendingRegistration) session.getAttribute("pendingRegistration");
        request.setAttribute("pendingEmail", pending.getEmail());



        getServletContext()
                .getRequestDispatcher("/verify-otp.jsp")
                .forward(request, response);
    }
}
