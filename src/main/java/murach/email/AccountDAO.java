package murach.email;

import murach.sql.ConnectionPool;
import java.sql.*;

public class AccountDAO {

    public static boolean emailExists(String email) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        if (connection == null) {
            return false;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT AccountID FROM Account WHERE Email = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            close(rs, ps, connection, pool);
        }
    }

    public static boolean register(String email, String password,
            String firstName, String lastName) {
        if (emailExists(email)) {
            return false;
        }

        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        if (connection == null) {
            return false;
        }

        PreparedStatement ps = null;
        try {
            String sql = "INSERT INTO Account (Email, Password, FirstName, LastName) "
                    + "VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email.trim());
            ps.setString(2, password);
            ps.setString(3, firstName.trim());
            ps.setString(4, lastName.trim());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            close(null, ps, connection, pool);
        }
    }

    public static User login(String email, String password) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        if (connection == null) {
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT AccountID, Email, FirstName, LastName "
                    + "FROM Account WHERE Email = ? AND Password = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1, email.trim());
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("AccountID"),
                        rs.getString("Email"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"));
            }
            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            close(rs, ps, connection, pool);
        }
    }

    private static void close(ResultSet rs, PreparedStatement ps,
            Connection connection, ConnectionPool pool) {
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
}
