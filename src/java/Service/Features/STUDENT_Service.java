/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service.Features;

import Model.ANSWERS;
import Model.COURSES;
import Model.LESSONS;
import Model.QUIZZES;
import Service.ANSWERS_Service;
import Service.LESSONS_Service;
import Service.QUIZZES_Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author minhk
 */
public class STUDENT_Service {
    public List<Map<String, Object>> filteredQuizzesByAnswer(List<QUIZZES> quizList, int studentID) {
        List<Map<String, Object>> filteredQuizzes = new ArrayList<>();
        for (QUIZZES quiz : quizList) {
            ANSWERS_Service answerService = new ANSWERS_Service();
            ANSWERS answer = answerService.getAnswerByUserIdAndQuizId(studentID, quiz.getID());
            
            int score = 0;
            if (answer != null && answer.getAnswer()== quiz.getCorrectAnswer()) {
                score = 10;
            }
            if (answer != null) {
                Map<String, Object> quizData = Map.of(
                    "quiz", quiz,
                    "answer", answer.getAnswer(),
                    "score", score
                );
                filteredQuizzes.add(quizData);
            } else {
                Map<String, Object> quizData = Map.of(
                    "quiz", quiz,
                    "answer", "null",
                     "score", "null"
                );
                filteredQuizzes.add(quizData);
            }
        }
        return filteredQuizzes;
        
    }

    public List<Map<String, Object>> filteredLessonsByQuizzesAnswer(List<LESSONS> lessonList, int studentID) {
        List<Map<String, Object>> filteredLessons = new ArrayList<>();
        for (LESSONS lesson : lessonList) {
            
            QUIZZES_Service quizService = new QUIZZES_Service();
            List<QUIZZES> quizList = quizService.getQuizByLessonID(lesson.getID());
            if (quizList == null || quizList.isEmpty()) {
                System.out.println("No quizzes available for the lesson with ID: " + lesson.getID());
                continue; // Skip to the next lesson if no quizzes are available
            }
            List<Map<String, Object>> filteredQuizzes = this.filteredQuizzesByAnswer(quizList, studentID);
            //check if filteredQuizzes's answer key is all not null 
            boolean hasAnswer = true;
            int correctCount = 0;
            for (Map<String, Object> quizData : filteredQuizzes) {
                QUIZZES quiz = (QUIZZES) quizData.get("quiz");
                if ("null".equals(quizData.get("answer"))) {
                    hasAnswer = false;
                    break;
                }else {
                    int userAnswer = Integer.parseInt((quizData.get("answer")).toString());
                    if (userAnswer == quiz.getCorrectAnswer()) {
                        correctCount++;
                    }
    }
            }
            double score = quizList.isEmpty() ? 0.0 : ((double)(correctCount * 10) / (quizList.size()));
            score = Math.round((score * 100) / 100); // làm tròn 2 chữ số thập phân
            if (hasAnswer) {
                Map<String, Object> lessonData = Map.of(
                    "lesson", lesson,
                    "status", "completed",
                    "score", score
                );
                filteredLessons.add(lessonData);
            } else {
                Map<String, Object> lessonData = Map.of(
                    "lesson", lesson,
                    "status", "not completed",
                    "score", "null"

                );
                filteredLessons.add(lessonData);
            }
        }
        return filteredLessons;
    }

    public List<Map<String, Object>> filteredCoursesByLessons(List<COURSES> courseList, int studentID) {
        List<Map<String, Object>> filteredCourses = new ArrayList<>();
        for (COURSES course : courseList) {
            LESSONS_Service lessonService = new LESSONS_Service();
            List<LESSONS> lessonList = lessonService.getLessonByCourseID(course.getID());
            if (lessonList == null || lessonList.isEmpty()) {
                System.out.println("No lessons available with course ID: " + course.getID());
                continue; // Skip to the next lesson if no quizzes are available
            }
            List<Map<String, Object>> filteredLessons = this.filteredLessonsByQuizzesAnswer(lessonList, studentID);
            //check if filteredLessons's status is all completed
            boolean hasCompletedLesson = true;
            double totalLessonScore = 0;
            for (Map<String, Object> lessonData : filteredLessons) {
                if ("not completed".equals(lessonData.get("status"))) {
                    hasCompletedLesson = false;
                    break;
                }
               totalLessonScore += (double) lessonData.get("score"); 
            }
             double score = lessonList.isEmpty() ? 0.0 : (totalLessonScore / lessonList.size());
             score = Math.round(score * 100.0) / 100.0; // làm tròn 2 chữ số
            if (hasCompletedLesson) {
                Map<String, Object> courseData = Map.of(
                    "course", course,
                    "status", "completed",
                    "score", score
                    
                );
                filteredCourses.add(courseData);
            } else {
                Map<String, Object> courseData = Map.of(
                    "course", course,
                    "status", "not completed",
                    "score", "null"
                );
                filteredCourses.add(courseData);
            }
        }
        return filteredCourses;
    }
}
