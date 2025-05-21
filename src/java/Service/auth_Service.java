/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Config.DBConnection;
import Model.USERS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author minhk
 */
public class auth_Service {
    public String login(String email, String password) {
        try{
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS WHERE email = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet result = pstmt.executeQuery();
            
            if(result.next()){
                int id = result.getInt("id");
                String userName = result.getString("userName");
                int roleID = result.getInt("roleID");
                
                // Generate JWT token
                String token = new USERS(id, userName, password, email, roleID).generateToken();
                System.out.println("JWT Token: " + token);
                return token;
            } else {
                throw new Exception("Invalid username or password");
            }


        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public USERS register(String userName, String email, String password) {
        PreparedStatement pstmt = null;
        try{
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO USERS (userName, email, password) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            int rowsAffected = pstmt.executeUpdate();

            //check if user is created
            if(rowsAffected > 0){
                // Get last inserted id
                String sql2 = "SELECT TOP 1 * FROM USERS ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql2);
                ResultSet result = pstmt.executeQuery();
                if (result.next()) {
                    USERS user = new USERS();
                    user.setID(result.getInt("id"));
                    user.setUserName(result.getString("userName"));
                    user.setEmail(result.getString("email"));
                    user.setPassword(result.getString("password"));
                    user.setRoleID(result.getInt("roleID"));
                    
                    return user;
                } else {
                    throw new Exception("User registration failed");
                }
            } else {
                throw new Exception("User registration failed");
            }


        } catch(Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}
