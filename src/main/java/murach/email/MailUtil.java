package murach.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Gửi mail thật qua Gmail SMTP (đăng nhập bằng App Password).
 * From luôn là SMTP_USER — Gmail không cho giả From tùy ý.
 */
public class MailUtil {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    // Gmail đã "đăng nhập" để hệ thống gửi mail (như bài Email List)
    private static final String SMTP_USER = "nuta006.st@gmail.com";
    // Tạo tại: Google Account → Security → 2-Step Verification → App passwords
    // KHÔNG commit mật khẩu lên GitHub — điền App Password local khi chạy
    private static final String SMTP_PASS = "";

    public static String getSmtpUser() {
        return SMTP_USER;
    }

    public static void sendMail(String replyTo, String to, String cc, String bcc,
            String subject, String body) throws MessagingException {

        if (SMTP_PASS == null || SMTP_PASS.trim().isEmpty()
                || "REPLACE_WITH_GMAIL_APP_PASSWORD".equals(SMTP_PASS)) {
            throw new MessagingException(
                    "Chưa cấu hình Gmail App Password trong MailUtil.SMTP_PASS");
        }

        // 1 - get a mail session (Gmail SMTP + login)
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        // 2 - create a message
        MimeMessage message = new MimeMessage(session);
        message.setSubject(subject, "UTF-8");
        message.setText(body, "UTF-8");

        // 3 - address the message
        message.setFrom(new InternetAddress(SMTP_USER));

        if (replyTo != null && !replyTo.trim().isEmpty()
                && !replyTo.trim().equalsIgnoreCase(SMTP_USER)) {
            message.setReplyTo(InternetAddress.parse(replyTo.trim()));
        }

        if (to != null && !to.trim().isEmpty()) {
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to.trim()));
        }
        if (cc != null && !cc.trim().isEmpty()) {
            message.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(cc.trim()));
        }
        if (bcc != null && !bcc.trim().isEmpty()) {
            message.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(bcc.trim()));
        }

        // 4 - send the message
        Transport.send(message);
    }

    /** Welcome mail kiểu Murach Email List */
    public static void sendWelcomeMail(User user) throws MessagingException {
        String subject = "Welcome to our Email List";
        String body = "Dear " + user.getFirstName() + ",\n\n"
                + "Thanks for joining our email list. "
                + "We'll send you notifications of new releases.\n\n"
                + "Have a great day!\n";

        sendMail(SMTP_USER, user.getEmail(), null, null, subject, body);
    }

    public static void sendOtpMail(String toEmail, String fullName, String otp)
            throws MessagingException {
        String subject = "Your OTP code - SQL Gateway";
        String body = "Dear " + fullName + ",\n\n"
                + "Your OTP code is: " + otp + "\n"
                + "This code expires in 5 minutes.\n\n"
                + "Have a great day!\n";

        sendMail(SMTP_USER, toEmail, null, null, subject, body);
    }
}
