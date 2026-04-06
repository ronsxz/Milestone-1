package m1.pkg1;

import java.sql.*;

public class Database {
    public static Connection getConnection() {
        try {
            // Palitan ang password at server name base sa SSMS setup mo
            String url = "jdbc:sqlserver://localhost:1433;databaseName=M1;encrypt=false;trustServerCertificate=true;";
            String user = "keneth_admin"; 
            String pass = "12345"; 
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }
}
