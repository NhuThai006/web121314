package murach.sql;

import java.sql.*;

public class SQLGatewayDAO {

    public static String executeSQL(String sqlStatement) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        String sqlResult = "";

        if (connection == null) {
            return "<p>Error: Unable to connect to the database.</p>";
        }

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            sqlStatement = sqlStatement.trim();

            if (sqlStatement.length() >= 6) {
                String sqlType = sqlStatement.substring(0, 6);

                if (sqlType.equalsIgnoreCase("select")) {
                    // SELECT query - return result as HTML table
                    resultSet = statement.executeQuery(sqlStatement);
                    sqlResult = SQLUtil.getHtmlTable(resultSet);
                } else {
                    // INSERT, UPDATE, DELETE, or DDL statement
                    int i = statement.executeUpdate(sqlStatement);
                    if (i == 0) {
                        // DDL statement (CREATE, DROP, ALTER, etc.)
                        sqlResult = "<p>The statement executed successfully.</p>";
                    } else {
                        // DML statement (INSERT, UPDATE, DELETE)
                        sqlResult = "<p>The statement executed successfully.<br>"
                                + i + " row(s) affected.</p>";
                    }
                }
            }
        } catch (SQLException e) {
            sqlResult = "<p>Error executing the SQL statement: <br>"
                    + e.getMessage() + "</p>";
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            pool.freeConnection(connection);
        }

        return sqlResult;
    }
}
