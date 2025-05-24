/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class QUIZZES {
    protected int id;
    private int lessonID;
    private String title;    
    private String question;
    private int correctAnswer;

    public QUIZZES(){}

    public QUIZZES(int lessonID, String title, String question,int correctAnswer){
        this.lessonID = lessonID;
        this.title = title;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
    
    public QUIZZES(int id,int lessonID, String title, String question, int correctAnswer){
        this.id = id;
        this.lessonID = lessonID;
        this.title = title;
        this.question = question;
        this.correctAnswer = correctAnswer;

    }


    public int getID(){
        return id;
    }
    public int getLessonID(){
        return lessonID;
    }
    public String getTitle(){
        return title;
    }
    
    public String getQuestion(){
        return question;
    } 
    
    public int getCorrectAnswer(){
        return correctAnswer;
    }
    public void setID( int id){
        this.id = id;
    }
    
    public void setLessonID( int lessonID){
        this.lessonID = lessonID;
    }
    
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setQuestion(String question){
        this.question = question;
    } 

    public void setCorrectAnswer(int correctAnswer){
        this.correctAnswer = correctAnswer;
    }
   
    
}
