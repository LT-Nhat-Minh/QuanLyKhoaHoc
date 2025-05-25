package Controller.Features;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Model.USERS;
import Service.USERS_Service;
import Utils.jwt;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author minhk
 */
@WebServlet(urlPatterns = {"/api/admin/*"})
public class api_ADMIN_Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        if (jwt.validateToken(request)){
            int userRoleID = (int) request.getAttribute("roleID");
            if (userRoleID == 3) { // 1 student, 2 teacher, 3 admin
            String path = request.getPathInfo();
            switch (path) {
                case "/users":
                    // Handle user-related admin actions
                    response.getWriter().write("{\"message\": \"Admin access to users\"}");
                    break;
                case "/teachers":
                    USERS_Service userService = new USERS_Service();
                    List<USERS> teachers = userService.getUsersByRoleID(2);

                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write(new Gson().toJson(teachers));
                    break;
                case "/quizzes":
                    // Handle quiz-related admin actions
                    response.getWriter().write("{\"message\": \"Admin access to quizzes\"}");
                    break;
                case "/lessons":
                    // Handle lesson-related admin actions
                    response.getWriter().write("{\"message\": \"Admin access to lessons\"}");
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"message\": \"Resource not found\"}");
            }
            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You are not authorized to access this resource\"}");
            }
        
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
