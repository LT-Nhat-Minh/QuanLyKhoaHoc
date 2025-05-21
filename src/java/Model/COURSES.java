/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author 22521
 */
public class COURSES {
    protected int id;
    private String title;    
    private String description;
    private double price;
    
    public COURSES(int id, String title, String description, double price){
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
    }
    
    public int getCourseID(){
        return id;
    }
    
    public String getCourseTitle(){
        return title;
    }
    
    public String getCourseDescription(){
        return description;
    } 
    
    public double getPrice(){
        return price;
    }
    
    public void setCourseID( int id){
        this.id = id;
    }
    
    public void setCourseTitle(String title){
        this.title = title;
    }
    
    public void setCourseDescription(String description){
        this.description = description;
    } 
    
    public void setPrice(double price){
        this.price = price;
    }
    
    
}
