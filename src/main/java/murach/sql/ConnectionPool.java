package murach.sql;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionPool {
    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    private ConnectionPool() {
        try {
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:comp/env/jdbc/murach");
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    public Connection getConnection() {
        try {
            // Đọc từ biến môi trường
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPass = System.getenv("DB_PASS");

            // Nếu không có biến môi trường thì dùng thông tin database Render của bạn cung cấp
            if (dbUrl == null || dbUrl.trim().isEmpty()) {
                dbUrl = "jdbc:postgresql://dpg-dauhbmg93c1s73e8m7v0-a.singapore-postgres.render.com:5432/murach_db_1wu5";
                dbUser = "murach_db_1wu5_user";
                dbPass = "6j2TmqpoDuO3ZBL2n4ELiIJqI8IkzmTs";
            }

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            return DriverManager.getConnection(dbUrl, dbUser, dbPass);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void freeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
