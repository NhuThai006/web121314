package murach.email;

import murach.sql.ConnectionPool;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMailDAO {

    public static class UserMail {
        private final String email;
        private final String fullName;

        public UserMail(String email, String fullName) {
            this.email = email;
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }
    }

    public static List<UserMail> getAllUsers() {
        List<UserMail> users = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();

        if (connection == null) {
            return users;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Email, FirstName, LastName FROM Account ORDER BY AccountID";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                String email = rs.getString("Email");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                String fullName = (firstName + " " + lastName).trim();
                users.add(new UserMail(email, fullName));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            pool.freeConnection(connection);
        }

        return users;
    }
}
