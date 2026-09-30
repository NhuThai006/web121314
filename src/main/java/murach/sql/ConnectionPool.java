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
            // Hỗ trợ Render: Nếu có biến môi trường DB_URL thì dùng trực tiếp (không qua JNDI)
            String dbUrl = System.getenv("DB_URL");
            if (dbUrl != null && !dbUrl.trim().isEmpty()) {
                String dbUser = System.getenv("DB_USER");
                String dbPass = System.getenv("DB_PASS");
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
                return DriverManager.getConnection(dbUrl, dbUser, dbPass);
            }
            
            // Local fallback (JNDI)
            return dataSource.getConnection();
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
