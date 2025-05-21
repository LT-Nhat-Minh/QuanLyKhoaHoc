/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.time.LocalDate;

/**
 *
 * @author 22521
 */
public class ENROLLMENTS{
    private LocalDate enrollmentDate;
    private boolean progress;
    public ENROLLMENTS(LocalDate enrollmentDate, boolean progress) {
        this.enrollmentDate = enrollmentDate;
        this.progress = progress;
    }
     public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public boolean getProgress(){
        return progress;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public void setProgress( boolean progress){
        this.progress = progress;
        
    }
}
