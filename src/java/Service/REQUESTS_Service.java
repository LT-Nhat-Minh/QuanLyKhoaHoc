/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Model.REQUESTS;
import Config.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author minhk
 */

public class REQUESTS_Service{
    public List<REQUESTS> getAllRequests() {
        List<REQUESTS> requestList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM REQUESTS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                REQUESTS request = new REQUESTS();
                request.setId(result.getInt("id"));
                request.setTitle(result.getString("title"));
                request.setDescription(result.getString("description"));
                request.setUserID(result.getInt("userID"));
                request.setStatus(result.getInt("status"));
                request.setCreatedAt(result.getString("createdAt"));
                request.setUpdatedAt(result.getString("updatedAt"));
                requestList.add(request);
            }

            result.close();
            pstmt.close();
            conn.close();

            return requestList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public REQUESTS getRequestById(int id) {
        REQUESTS request = new REQUESTS();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM REQUESTS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            while(result.next()) {
                request.setId(result.getInt("id"));
                request.setTitle(result.getString("title"));
                request.setDescription(result.getString("description"));
                request.setUserID(result.getInt("userID"));
                request.setStatus(result.getInt("status"));
                request.setCreatedAt(result.getString("createdAt"));
                request.setUpdatedAt(result.getString("updatedAt"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return request;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<REQUESTS> getRequestsByUserId(int userId) {
        List<REQUESTS> requestList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM REQUESTS WHERE userID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                REQUESTS request = new REQUESTS();
                request.setId(result.getInt("id"));
                request.setTitle(result.getString("title"));
                request.setDescription(result.getString("description"));
                request.setUserID(result.getInt("userID"));
                request.setStatus(result.getInt("status"));
                request.setCreatedAt(result.getString("createdAt"));
                request.setUpdatedAt(result.getString("updatedAt"));
                requestList.add(request);
            }

            result.close();
            pstmt.close();
            conn.close();

            return requestList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createRequest(REQUESTS request) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO REQUESTS (title, description, userID, status) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, request.getTitle());
            pstmt.setString(2, request.getDescription());
            pstmt.setInt(3, request.getUserID());
            pstmt.setInt(4, request.getStatus());
            pstmt.executeUpdate();

            // Lấy ID của bản ghi vừa được chèn
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                request.setId(generatedKeys.getInt(1));
            }

            // Truy vấn để lấy createdAt và updatedAt
            String selectSql = "SELECT * FROM REQUESTS WHERE id = ?";
            PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
            selectPstmt.setInt(1, request.getId());
            ResultSet result = selectPstmt.executeQuery();
            if (result.next()) {
                request.setCreatedAt(result.getString("createdAt"));
                request.setUpdatedAt(result.getString("updatedAt"));
            }
            
            generatedKeys.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }   

    public void updateRequest(REQUESTS request) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE REQUESTS SET title = ?, description = ?, userID = ?, status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, request.getTitle());
            pstmt.setString(2, request.getDescription());
            pstmt.setInt(3, request.getUserID());
            pstmt.setInt(4, request.getStatus());
            pstmt.setInt(5, request.getId());
            pstmt.executeUpdate();

            // Truy vấn để lấy createdAt và updatedAt
            String selectSql = "SELECT * FROM REQUESTS WHERE id = ?";
            PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
            selectPstmt.setInt(1, request.getId());
            ResultSet result = selectPstmt.executeQuery();
            if (result.next()) {
                request.setCreatedAt(result.getString("createdAt"));
                request.setUpdatedAt(result.getString("updatedAt"));
            }

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteRequest(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM REQUESTS WHERE id = ?";
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