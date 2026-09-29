package murach.email;

import java.io.Serializable;
import java.util.Random;

public class PendingRegistration implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String otp;
    private long expireAt;
    private boolean mailSent;

    public static String generateOtp() {
        int code = 100000 + new Random().nextInt(900000);
        return String.valueOf(code);
    }

    public PendingRegistration(String email, String password,
            String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.otp = generateOtp();
        this.expireAt = System.currentTimeMillis() + 5 * 60 * 1000L; // 5 phút
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }

    public boolean matchesOtp(String input) {
        return otp != null && otp.equals(input != null ? input.trim() : "");
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOtp() {
        return otp;
    }

    public boolean isMailSent() {
        return mailSent;
    }

    public void setMailSent(boolean mailSent) {
        this.mailSent = mailSent;
    }

    public String getFullName() {
        return (firstName + " " + lastName).trim();
    }
}
