/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class LESSONS {
    protected int id;
    private int courseID;
    private String title;    
    private String content;
    private String videoURL;
    
    public LESSONS(){}

    public LESSONS(int courseID, String title, String content, String videoURL){
        this.courseID = courseID;
        this.title = title;
        this.content = content;
        this.videoURL = videoURL;
    }

    public LESSONS(int id,int courseID, String title, String content, String videoURL){
        this.id = id;
        this.courseID = courseID;
        this.title = title;
        this.content = content;
        this.videoURL = videoURL;
    }
    
    public int getID(){
        return id;
    }
    public int getCourseID(){
        return courseID;
    }
    public String getTitle(){
        return title;
    }
    
    public String getContent(){
        return content;
    } 
    
    public String getVideoURL(){
        return videoURL;
    }
    
    public void setID( int id){
        this.id = id;
    }
    
    public void setCourseID( int courseID){
        this.courseID = courseID;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setContent(String content){
        this.content = content;
    } 
    
    public void setVideoURL(String videoURL){
        this.videoURL = videoURL;
    }
    
    
}
