package Service;

import Config.DBConnection;
import Model.COURSES;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class COURSES_Service {

    public List<COURSES> getAllCourses() {
        List<COURSES> courseList = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM COURSES";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                COURSES course = new COURSES();
                course.setID(result.getInt("id"));
                course.setTitle(result.getString("title"));
                course.setDescription(result.getString("description"));
                course.setPrice(result.getDouble("price"));
                course.setCreatedUserByID(result.getInt("createdByUserID"));
                courseList.add(course);
            }

            result.close();
            pstmt.close();
            conn.close();

            return courseList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public COURSES getCourseById(int id) {
        COURSES course = new COURSES();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM COURSES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                course.setID(result.getInt("id"));
                course.setTitle(result.getString("title"));
                course.setDescription(result.getString("description"));
                course.setPrice(result.getDouble("price"));
                course.setCreatedUserByID(result.getInt("createdByUserID"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return course;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createCourse(COURSES course) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO COURSES (title, description, price, createdByUserID) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setDouble(3, course.getPrice());
            pstmt.setInt(4, course.getCreatedUserByID());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                course.setID(generatedKeys.getInt(1));
            }

            generatedKeys.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void updateCourse(COURSES course) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE COURSES SET title = ?, description = ?, price = ? , createdByUserID = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setDouble(3, course.getPrice());
            pstmt.setInt(4, course.getCreatedUserByID());
            pstmt.setInt(5, course.getID());
            pstmt.executeUpdate();
            
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteCourse(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM COURSES WHERE id = ?";
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
