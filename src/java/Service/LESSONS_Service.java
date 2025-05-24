package Service;
import Config.DBConnection;
import Model.LESSONS;
import Model.COURSES;
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
                lesson.setCourseID(result.getInt("courseID"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
                lessonList.add(lesson);
            }

            result.close();
            pstmt.close();
            conn.close();

            return lessonList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public LESSONS getLessonById(int id) {
        LESSONS lesson = new LESSONS();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM LESSONS WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet result = pstmt.executeQuery();

            if (result.next()) {
                lesson.setID(result.getInt("id"));
                lesson.setCourseID(result.getInt("courseID"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
            }

            result.close();
            pstmt.close();
            conn.close();

            return lesson;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<LESSONS> getLessonByCourseID(int courseID) {
        List<LESSONS> lessonByCourseIDList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM LESSONS WHERE courseID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseID);
            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                LESSONS lesson = new LESSONS();
                lesson.setID(result.getInt("id"));
                lesson.setCourseID(result.getInt("courseID"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
                lessonByCourseIDList.add(lesson);
            }

            result.close();
            pstmt.close();
            conn.close();

            return lessonByCourseIDList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public List<LESSONS> getLessonByCoursesList(List<COURSES> coursesList) {
        List<LESSONS> lessonByCoursesList = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM LESSONS WHERE courseID IN (";
            for (int i = 0; i < coursesList.size(); i++) {
                sql += "?";
                if (i < coursesList.size() - 1) {
                    sql += ", ";
                }
            }
            sql += ")";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int i = 0; i < coursesList.size(); i++) {
                pstmt.setInt(i + 1, coursesList.get(i).getID());
            }

            ResultSet result = pstmt.executeQuery();

            while (result.next()) {
                LESSONS lesson = new LESSONS();
                lesson.setID(result.getInt("id"));
                lesson.setCourseID(result.getInt("courseID"));
                lesson.setTitle(result.getString("title"));
                lesson.setContent(result.getString("content"));
                lesson.setVideoURL(result.getString("videoURL"));
                lessonByCoursesList.add(lesson);
            }

            result.close();
            pstmt.close();
            conn.close();

            return lessonByCoursesList;
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void createLesson(LESSONS lesson) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO LESSONS (courseID, title, content, videoURL) VALUES (?,?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1,lesson.getCourseID());
            pstmt.setString(2, lesson.getTitle());
            pstmt.setString(3, lesson.getContent());
            pstmt.setString(4, lesson.getVideoURL());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                lesson.setID(generatedKeys.getInt(1));
            }

            generatedKeys.close();
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error service: " + e.getMessage());
        }
    }

    public void updateLesson(LESSONS lesson) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE LESSONS SET courseID = ?, title = ?, content = ?, videoURL = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, lesson.getCourseID());
            pstmt.setString(2, lesson.getTitle());
            pstmt.setString(3, lesson.getContent());
            pstmt.setString(4, lesson.getVideoURL());
            pstmt.setInt(5, lesson.getID());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();

        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
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
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    
    

}
