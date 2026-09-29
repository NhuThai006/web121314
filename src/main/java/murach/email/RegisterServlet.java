package murach.email;

import java.io.*;
import javax.mail.MessagingException;
import javax.servlet.*;
import javax.servlet.http.*;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        getServletContext()
                .getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        String message;
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()
                || firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty()) {
            message = "Vui lòng điền đầy đủ thông tin.";
        } else if (!password.equals(confirm)) {
            message = "Xác nhận mật khẩu không khớp.";
        } else if (AccountDAO.emailExists(email)) {
            message = "Email này đã được đăng ký. Vui lòng đăng nhập.";
        } else {
            PendingRegistration pending = new PendingRegistration(
                    email.trim(), password, firstName.trim(), lastName.trim());

            String mailStatus = sendOtpEmail(pending);
            pending.setMailSent(mailStatus == null);

            HttpSession session = request.getSession();
            session.setAttribute("pendingRegistration", pending);

            if (mailStatus == null) {
                session.setAttribute("otpMessage",
                        "Đã gửi mã OTP tới " + pending.getEmail()
                                + ". Vui lòng kiểm tra email và nhập OTP.");
            } else {
                // SMTP localhost thường chưa chạy → vẫn cho nhập OTP (hiện mã để test)
                session.setAttribute("otpMessage",
                        "Không gửi được email (" + mailStatus
                                + "). Dùng mã OTP tạm thời bên dưới để xác nhận.");
                session.setAttribute("showOtpFallback", true);
            }

            response.sendRedirect("verify-otp");
            return;
        }

        request.setAttribute("message", message);
        request.setAttribute("email", email);
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);

        getServletContext()
                .getRequestDispatcher("/register.jsp")
                .forward(request, response);
    }

    /** @return null nếu gửi OK, ngược lại là thông báo lỗi */
    private String sendOtpEmail(PendingRegistration pending) {
        try {
            MailUtil.sendOtpMail(
                    pending.getEmail(),
                    pending.getFullName(),
                    pending.getOtp());
            return null;
        } catch (MessagingException e) {
            log(e.toString());
            return e.getMessage();
        }
    }
}
