package com.project.usermanager.web;

import com.project.usermanager.dao.UserDAO;
import com.project.usermanager.entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/new":
                showNewForm(request, response);
                break;
            case "/insert":
                insertUser(request, response);
                break;
            case "/delete":
                deleteUser(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/update":
                updateUser(request, response);
                break;
            default:
                try {
                    listUser(request, response);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-form.jsp");
        requestDispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String age = request.getParameter("age");
        User user = new User(name, surname, Integer.parseInt(age));
        userDAO.userAdd(user);

        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        userDAO.deleteUser(id);
        response.sendRedirect("list");

    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        User tempUser;
        tempUser = userDAO.getUserById(id);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-from.jsp");
        request.setAttribute("user", tempUser);
        requestDispatcher.forward(request, response);

    }

    public void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        int age = Integer.parseInt(request.getParameter("age"));
        User user = new User(id, name, surname, age);
        userDAO.updateUser(user);
        response.sendRedirect("list");

    }

    private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {

        try {
            List<User> listUsers = userDAO.getAllUsers();
            request.setAttribute("listUser", listUsers);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-list.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
