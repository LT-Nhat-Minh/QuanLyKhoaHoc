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
                courseList.add(course);
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return courseList;
    }

    public COURSES getCourseById(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM COURSES WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                COURSES course = new COURSES();
                course.setID(result.getInt("id"));
                course.setTitle(result.getString("title"));
                course.setDescription(result.getString("description"));
                course.setPrice(result.getDouble("price"));
                return course;
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public void createCourse(COURSES course) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO COURSES (title, description, price) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setDouble(3, course.getPrice());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateCourse(COURSES course) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE COURSES SET title = ?, description = ?, price = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setDouble(3, course.getPrice());
            pstmt.setInt(4, course.getID());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
            System.out.println("Error: " + e.getMessage());
        }
    }
}
