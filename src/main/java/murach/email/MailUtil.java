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

    // Email gửi đi phải là email đã xác thực trên Brevo
    private static final String SMTP_USER = "duongduyvinh206@gmail.com";
    // KHÔNG commit mật khẩu lên GitHub — điền App Password local khi chạy
    private static final String SMTP_PASS = "";

    public static String getSmtpUser() {
        return SMTP_USER;
    }

    // Thay thế bằng Brevo API Key cho hosting trên render.com
    private static final String BREVO_API_KEY = "YOUR_BREVO_API_KEY_HERE";
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    private static String getBrevoApiKey() {
        String envKey = System.getenv("BREVO_API_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey;
        }
        return BREVO_API_KEY;
    }

    public static void sendMail(String replyTo, String to, String cc, String bcc,
            String subject, String body) throws MessagingException {

        // --- GIỮ LẠI CODE SMTP CŨ NHƯNG KHÔNG SỬ DỤNG ---
        /*
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
        */

        // --- SỬ DỤNG BREVO API ---
        try {
            String jsonPayload = buildBrevoJsonPayload(replyTo, to, cc, bcc, subject, body);

            java.net.URL url = new java.net.URL(BREVO_API_URL);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", getBrevoApiKey());
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);

            try (java.io.OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode < 200 || responseCode >= 300) {
                String errorResponse = "";
                java.io.InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    try (java.util.Scanner scanner = new java.util.Scanner(errorStream, "utf-8")) {
                        errorResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    }
                }
                throw new MessagingException("Failed to send email via Brevo API. HTTP code: " + responseCode + " - " + errorResponse);
            }
        } catch (Exception e) {
            if (e instanceof MessagingException) {
                throw (MessagingException) e;
            }
            throw new MessagingException("Error sending email via Brevo API: " + e.getMessage(), e);
        }
    }

    private static String buildBrevoJsonPayload(String replyTo, String to, String cc, String bcc, String subject, String body) {
        String escapedSubject = subject.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        String escapedBody = body.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sender\":{\"email\":\"").append(SMTP_USER).append("\",\"name\":\"SQL Gateway System\"},");
        
        if (to != null && !to.trim().isEmpty()) {
            sb.append("\"to\":[");
            String[] toArray = to.split(",");
            for (int i = 0; i < toArray.length; i++) {
                sb.append("{\"email\":\"").append(toArray[i].trim()).append("\"}");
                if (i < toArray.length - 1) sb.append(",");
            }
            sb.append("],");
        }

        if (cc != null && !cc.trim().isEmpty()) {
            sb.append("\"cc\":[");
            String[] ccArray = cc.split(",");
            for (int i = 0; i < ccArray.length; i++) {
                sb.append("{\"email\":\"").append(ccArray[i].trim()).append("\"}");
                if (i < ccArray.length - 1) sb.append(",");
            }
            sb.append("],");
        }

        if (bcc != null && !bcc.trim().isEmpty()) {
            sb.append("\"bcc\":[");
            String[] bccArray = bcc.split(",");
            for (int i = 0; i < bccArray.length; i++) {
                sb.append("{\"email\":\"").append(bccArray[i].trim()).append("\"}");
                if (i < bccArray.length - 1) sb.append(",");
            }
            sb.append("],");
        }
        
        if (replyTo != null && !replyTo.trim().isEmpty() && !replyTo.trim().equalsIgnoreCase(SMTP_USER)) {
            sb.append("\"replyTo\":{\"email\":\"").append(replyTo.trim()).append("\"},");
        }

        sb.append("\"subject\":\"").append(escapedSubject).append("\",");
        sb.append("\"textContent\":\"").append(escapedBody).append("\"");
        sb.append("}");

        return sb.toString();
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
