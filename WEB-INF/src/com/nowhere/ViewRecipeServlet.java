package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/view")
public class ViewRecipeServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	HttpSession session = request.getSession(false);

	List<Recipe> recipes = (List<Recipe>) session.getAttribute("recipes");

	int id = Integer.parseInt(request.getParameter("id"));
	Recipe r = recipes.get(id);

	response.setContentType("text/html");
	PrintWriter out = response.getWriter();

	out.println("<h2>" + r.getTitle() + "</h2>");
	out.println("<p>" + r.getInstructions() + "</p>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	doGet(request, response);
    }
}
