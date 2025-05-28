package Service;
import Config.DBConnection;
import Model.COURSES_USERS_BANNED;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author minhk
 */
public class COURSES_USERS_BANNED_Service {

    public List<Integer> getAllBannedUsersFromCourse(int courseID) {
        List<Integer> bannedUserIDs = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT bannedUserID FROM COURSES_USERS_BANNED WHERE courseID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseID);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                bannedUserIDs.add(result.getInt("bannedUserID"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return bannedUserIDs;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public COURSES_USERS_BANNED getBannedUserFromCourse(int courseID, int userID) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM COURSES_USERS_BANNED WHERE courseID = ? AND bannedUserID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseID);
            pstmt.setInt(2, userID);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                COURSES_USERS_BANNED bannedUser = new COURSES_USERS_BANNED();
                bannedUser.setCourseID(result.getInt("courseID"));
                bannedUser.setBannedUserID(result.getInt("bannedUserID"));
                result.close();
                pstmt.close();
                conn.close();
                return bannedUser;
            } else {
                result.close();
                pstmt.close();
                conn.close();
                return null; // No banned user found
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    //create
    public COURSES_USERS_BANNED banUserFromCourse(int courseID, int userID) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO COURSES_USERS_BANNED (courseID, bannedUserID) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseID);
            pstmt.setInt(2, userID);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return new COURSES_USERS_BANNED(courseID, userID);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    //delete
    public void removeBannedUserFromCourse(int courseID, int userID) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM COURSES_USERS_BANNED WHERE courseID = ? AND bannedUserID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseID);
            pstmt.setInt(2, userID);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}
