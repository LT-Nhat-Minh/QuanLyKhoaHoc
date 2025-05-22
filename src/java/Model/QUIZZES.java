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
    private String title;    
    private String question;

    public QUIZZES(){}

    public QUIZZES(String title, String question){
        this.title = title;
        this.question = question;
    }
    
    public QUIZZES(int id, String title, String question){
        this.id = id;
        this.title = title;
        this.question = question;
    }
    
    public int getID(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getQuestion(){
        return question;
    } 
    

    public void setID( int id){
        this.id = id;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setQuestion(String question){
        this.question = question;
    } 

    
   
    
}
