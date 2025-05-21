/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Utils.jwt;
import Service.USERS_Service;
import Model.USERS;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author minhk
 */
@WebServlet(name = "api_USERS_Controller", urlPatterns = {"/api/users"})
public class api_USERS_Controller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet api_USERS_Controller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet api_USERS_Controller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // check validate token        
        if (jwt.validateToken(request)) {
            System.out.println("Token is valid!");

            // check roleID
            int roleID = (int) request.getAttribute("roleID");
            if (roleID == 3) { // 1 student, 2 teacher, 3 admin

                // Check if get user by id, the request will have a query id
                String idParam = request.getParameter("id");
                USERS_Service userService = new USERS_Service();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                if (idParam != null) {
                    // Get user by id
                    try {
                        int id = Integer.parseInt(idParam);
                        USERS user = userService.getUserById(id);
                        if (user != null) {
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write(new Gson().toJson(user));
                        } else {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"error\": \"User not found\"}");
                        }
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid user id\"}");
                    }
                } else {
                    // Get all users
                    System.out.println("Get all users");
                    List<USERS> userList = userService.getAllUsers();
                    System.out.println(userList);
                    if (userList != null) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getWriter().write(new Gson().toJson(userList));
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("{\"error\": \"No users found\"}");
                    }
                }

            } else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("{\"message\": \"You do not have permission to access this resource\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"Invalid token\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

    setCorsHeaders(response);
    response.setContentType("application/json;charset=UTF-8");

    try {
        String contentType = request.getContentType();
        USERS user = new USERS();

        if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
            user.setUserName(request.getParameter("userName"));
            user.setEmail(request.getParameter("email"));
            user.setPassword(request.getParameter("password"));

            String roleStr = request.getParameter("roleID");
            try {
                user.setRoleID(Integer.parseInt(roleStr));
            } catch (NumberFormatException e) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid roleID format");
                return;
            }

        } else {
            sendError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported content type");
            return;
        }

        if (user.getUserName() == null || user.getEmail() == null || user.getPassword() == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required fields");
            return;
        }

        USERS_Service userService = new USERS_Service();
        userService.createUser(user);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("{\"message\": \"User created successfully\"}");

    } catch (Exception e) {
        handleError(response, e); // hoặc log lỗi tại đây
        e.printStackTrace(); // In lỗi chi tiết trong console
    }
}
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void handleError(HttpServletResponse response, Exception e) throws IOException {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
}


  private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
    response.setStatus(statusCode);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"error\": \"" + message + "\"}");
}
  
  private void setCorsHeaders(HttpServletResponse response) {
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
}



}
