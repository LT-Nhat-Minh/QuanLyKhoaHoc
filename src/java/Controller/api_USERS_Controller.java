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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
