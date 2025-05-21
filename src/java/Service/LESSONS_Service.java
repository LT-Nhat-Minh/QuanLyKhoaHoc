package Service;

import Config.DBConnection;
import Model.LESSONS;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LESSONS_Service {

    public List<LESSONS> getAllLessons() {
        List<LESSONS> lessonList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM LESSONS";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                LESSONS lesson = new LESSONS();
                lesson.setID(result.getInt("id"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
                lessonList.add(lesson);
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return lessonList;
    }

    public LESSONS getLessonById(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM LESSONS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                LESSONS lesson = new LESSONS();
                lesson.setID(result.getInt("id"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
                return lesson;
            }

            result.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        return null;
    }

    public void createLesson(LESSONS lesson) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO LESSONS (title, description, videoURL) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lesson.getTitle());
            pstmt.setString(2, lesson.getContent());
            pstmt.setString(3, lesson.getVideoURL());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateLesson(LESSONS lesson) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE LESSONS SET title = ?, description = ?, videoURL = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, lesson.getTitle());
            pstmt.setString(2, lesson.getContent());
            pstmt.setString(3, lesson.getVideoURL());
            pstmt.setInt(4, lesson.getID());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void deleteLesson(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM LESSONS WHERE id = ?";
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
