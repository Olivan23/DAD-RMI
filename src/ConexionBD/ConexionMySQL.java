package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/votacion"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "Bye211221"; 

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
