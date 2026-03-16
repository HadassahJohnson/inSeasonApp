package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

//need to store all fruit and vegetables in an enum array maybe with associated months? So if apples are good in the fall then apples would be associated with 10, 11, 12

@WebServlet("/produce")
public class ProduceServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	response.setContentType("text/html; charset=UTF-8");
	PrintWriter out = response.getWriter();
	Set<String> produce = new HashSet<>();

	String month = request.getParameter("month");
	out.println("""
		    <!DOCTYPE html>
		    <html>
		    <head><title>Produce In Season</title></head>
		    <body>
		    <h2>Produce in Season for the month of %s</h2>
		    <ul>
		    """.formatted(month));
		    switch(month) {
		    case "January" -> produce = NYSeasonalProduce.JANUARY.getProduce();
		    case "February" -> produce = NYSeasonalProduce.FEBRUARY.getProduce();
		    case "March" -> produce = NYSeasonalProduce.MARCH.getProduce();
		    case "April" -> produce = NYSeasonalProduce.APRIL.getProduce();
		    case "May" -> produce = NYSeasonalProduce.MAY.getProduce();
		    case "June" -> produce = NYSeasonalProduce.JUNE.getProduce();
		    case "July" ->  produce = NYSeasonalProduce.JULY.getProduce();
		    case "August" ->  produce = NYSeasonalProduce.AUGUST.getProduce();
		    case "September" ->  produce = NYSeasonalProduce.SEPTEMBER.getProduce();
		    case "October" ->  produce = NYSeasonalProduce.OCTOBER.getProduce();
		    case "November" -> produce = NYSeasonalProduce.NOVEMBER.getProduce();
		    case "December" -> produce = NYSeasonalProduce.DECEMBER.getProduce();
		    }
		    
		    for (String fruitable : produce) {
			out.println("<li>" + fruitable + "</li>");
		    }

		    //for every fruitable in produce print a list item with the name of the fruitable
	out.println("""
		    </ul>
		    </body>
		    </html>
		    """);
		    out.close();
		    
    }
}
