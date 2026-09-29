package murach.email;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmailServlet extends HttpServlet {

    private boolean requireLogin(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return false;
        }
        return true;
    }

    private void prepareForm(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("from", MailUtil.getSmtpUser());
        request.setAttribute("currentUser", user);
        request.setAttribute("replyTo", user.getEmail());

        HttpSession session = request.getSession();
        Object flash = session.getAttribute("message");
        if (flash != null && request.getAttribute("message") == null) {
            request.setAttribute("message", flash);
            session.removeAttribute("message");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireLogin(request, response)) {
            return;
        }

        prepareForm(request);
        getServletContext()
                .getRequestDispatcher("/email.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        if (!requireLogin(request, response)) {
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        String from = user.getEmail();
        String to = request.getParameter("to");
        String cc = request.getParameter("cc");
        String bcc = request.getParameter("bcc");
        String subject = request.getParameter("subject");
        String body = request.getParameter("body");

        String message;
        try {
            MailUtil.sendMail(from, to, cc, bcc, subject, body);
            message = "Email sent successfully.";
        } catch (javax.mail.MessagingException e) {
            log(e.toString());
            message = "Error sending email: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.setAttribute("to", to);
        request.setAttribute("cc", cc);
        request.setAttribute("bcc", bcc);
        request.setAttribute("subject", subject);
        request.setAttribute("body", body);
        prepareForm(request);

        getServletContext()
                .getRequestDispatcher("/email.jsp")
                .forward(request, response);
    }
}
