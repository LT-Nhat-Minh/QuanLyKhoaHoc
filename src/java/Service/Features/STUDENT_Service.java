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
            if (answer != null) {
                Map<String, Object> quizData = Map.of(
                    "quiz", quiz,
                    "answer", answer.getAnswer()
                );
                filteredQuizzes.add(quizData);
            } else {
                Map<String, Object> quizData = Map.of(
                    "quiz", quiz,
                    "answer", "null"
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
            List<Map<String, Object>> filteredQuizzes = this.filteredQuizzesByAnswer(quizList, studentID);
            //check if filteredQuizzes's answer key is all not null 
            boolean hasAnswer = true;
            for (Map<String, Object> quizData : filteredQuizzes) {
                if ("null".equals(quizData.get("answer"))) {
                    hasAnswer = false;
                    break;
                }
            }
            if (hasAnswer) {
                Map<String, Object> lessonData = Map.of(
                    "lesson", lesson,
                    "status", "completed"
                );
                filteredLessons.add(lessonData);
            } else {
                Map<String, Object> lessonData = Map.of(
                    "lesson", lesson,
                    "status", "not completed"
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
            List<Map<String, Object>> filteredLessons = this.filteredLessonsByQuizzesAnswer(lessonList, studentID);
            //check if filteredLessons's status is all completed
            boolean hasCompletedLesson = true;
            for (Map<String, Object> lessonData : filteredLessons) {
                if ("not completed".equals(lessonData.get("status"))) {
                    hasCompletedLesson = false;
                    break;
                }
            }
            if (hasCompletedLesson) {
                Map<String, Object> courseData = Map.of(
                    "course", course,
                    "status", "completed"
                );
                filteredCourses.add(courseData);
            } else {
                Map<String, Object> courseData = Map.of(
                    "course", course,
                    "status", "not completed"
                );
                filteredCourses.add(courseData);
            }
        }
        return filteredCourses;
    }
}
