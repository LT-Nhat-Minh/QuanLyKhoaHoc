/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author minhk
 */
public class DBConnection {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://DESKTOP-Minh118:1433;;"
                + "databaseName=qlkh_beta16;"
                + "encrypt=true;"
                + "trustServerCertificate=true";
        String user = "sa";
        String pass = "123456";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, pass);
    }
    
    
//     public static Connection getConnection() throws Exception {
//        String url = "jdbc:sqlserver://WIN-VSV1A4EOCDD\\SQLEXPRESS01:1433;;"
//                + "databaseName=qlkhonline;"
//                + "encrypt=true;"
//                + "trustServerCertificate=true";
//        String user = "sa";
//        String pass = "123456";
//        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        System.out.println("Test: ConnDB");
//        return DriverManager.getConnection(url, user, pass);
//    }
}
