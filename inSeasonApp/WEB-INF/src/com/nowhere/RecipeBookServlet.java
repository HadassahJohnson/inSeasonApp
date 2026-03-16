package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/recipebook")
public class RecipeBookServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	response.setContentType("text/html; charset=UTF-8");
	PrintWriter out = response.getWriter();

	out.println("""
		    <!DOCTYPE html>
		    <html>
		    <head><title>My Recipe Book</title></head>
		    <body>
		    <h1>This is a list of the input recipes</h1>
		    <p>Recipes:</p>
		    <ul>
		    """);
		    HttpSession session = request.getSession(false);
		    List<Recipe> recipes = new ArrayList<>();
		    
		    if(session!=null) {
			recipes = (List<Recipe>)session.getAttribute("recipes");
			if (recipes != null) {
			    for (int i = 0; i < recipes.size(); i++) {
				out.println("<li>");
				out.println("<a href='view?id=" + i + "'>" + recipes.get(i).getTitle() + "</a>");
				out.println("</li>");
			    }
			}
		    }
		    out.println("""
		    </ul>
		    </body>
		    </html>
		    """);
		    out.close();
		    System.out.println("hello world, to Tomcat!");
		    }
    }
		    
