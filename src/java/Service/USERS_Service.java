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
                user.setIsBanned(result.getBoolean("isBanned"));
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

    public List<USERS> getUsersByRoleID(int roleID) {
        List<USERS> userList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM USERS WHERE roleID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleID);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                USERS user = new USERS();
                user.setID(result.getInt("id"));
                user.setUserName(result.getString("userName"));
                user.setPassword(result.getString("password"));
                user.setEmail(result.getString("email"));
                user.setRoleID(result.getInt("roleID"));
                user.setIsBanned(result.getBoolean("isBanned"));
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
                user.setIsBanned(result.getBoolean("isBanned"));
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
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setInt(4, user.getRoleID());
            pstmt.executeUpdate();

            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setID(generatedKeys.getInt(1));
            }
            
            pstmt.close();
            conn.close();   

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateUser(USERS user) {
         try{
            Connection conn = DBConnection.getConnection();
            //check if user exists
            if (getUserById(user.getID()) == null) {
                throw new RuntimeException("User with ID " + user.getID() + " does not exist.");
            }

            //get the current user details
            USERS currentUser = getUserById(user.getID());

            
            String sql = "UPDATE USERS SET username = ?, password = ?, email = ?, roleID = ?, isBanned = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName() == null ? currentUser.getUserName() : user.getUserName());
            pstmt.setString(2, user.getPassword() == null ? currentUser.getPassword() : user.getPassword());
            pstmt.setString(3, user.getEmail() == null ? currentUser.getEmail() : user.getEmail());
            pstmt.setInt(4, user.getRoleID() == 0 ? currentUser.getRoleID() : user.getRoleID());
            pstmt.setBoolean(5, user.getIsBanned() == null ? currentUser.getIsBanned() : user.getIsBanned());
            pstmt.setInt(6, user.getID());
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
