/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Config.DBConnection;
import java.util.List;
import Model.USERS;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author minhk
 */
public class USERS_Service {
    public List<USERS> getAllUsers() {
        List<USERS> userList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                USERS user = new USERS();
                user.setID(result.getInt("id"));
                user.setUserName(result.getString("userName"));
                user.setPassword(result.getString("password"));
                user.setEmail(result.getString("email"));
                user.setRoleID(result.getInt("roleID"));
                userList.add(user);
            }

            result.close();
            pstmt.close();
            conn.close();

            return userList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public USERS getUserById(int id) {
        USERS user = new USERS();
        try{
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                user.setID(result.getInt("id"));
                user.setUserName(result.getString("userName"));
                user.setPassword(result.getString("password"));
                user.setEmail(result.getString("email"));
                user.setRoleID(result.getInt("roleID"));
            }

            result.close();
            pstmt.close();
            conn.close();
        
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }     
    }

    public void createUser(USERS user) {
        try{
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO USERS (username, password, email, roleID) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getRoleID());
            pstmt.executeUpdate();
            
            pstmt.close();
            conn.close();   

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateUser(USERS user) {
         try{
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE USERS SET username = ?, password = ?, email = ?, roleID = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getRoleID());
            pstmt.setInt(5, user.getID());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteUser(int id) {
        try{
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM USERS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
