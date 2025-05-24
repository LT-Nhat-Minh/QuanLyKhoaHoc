package Model;

/**
 *
 * @author 22521
 */
public class ANSWERS {
    private int userId;
    private int quizId;
    private int answer;//(1,2,3,4)
    private String updatedAt;
    
    public ANSWERS() {}
    
    public ANSWERS(int userId, int quizId, int answer, String updatedAt) {
        this.userId = userId;
        this.quizId = quizId;
        this.answer = answer;
        this.updatedAt = updatedAt;
    }
    

    public int getUserId() {
        return userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public int getAnswer() {
        return answer;
    }

    public String updatedAt(){
        return updatedAt;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}