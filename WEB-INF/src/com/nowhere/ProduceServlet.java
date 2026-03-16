package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebServlet("/api/produce/*")
public class ProduceServlet extends HttpServlet {
    private ObjectMapper mapper;

    private static final Map<String, List<NYSeasonalProduce>> SEASONS = Map.of(
        "SPRING", List.of(NYSeasonalProduce.MARCH, NYSeasonalProduce.APRIL, NYSeasonalProduce.MAY),
        "SUMMER", List.of(NYSeasonalProduce.JUNE, NYSeasonalProduce.JULY, NYSeasonalProduce.AUGUST),
        "FALL",   List.of(NYSeasonalProduce.SEPTEMBER, NYSeasonalProduce.OCTOBER, NYSeasonalProduce.NOVEMBER),
        "WINTER", List.of(NYSeasonalProduce.DECEMBER, NYSeasonalProduce.JANUARY, NYSeasonalProduce.FEBRUARY)
    );
    
    @Override
    public void init() throws ServletException {
	mapper = new ObjectMapper();
	mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	String path = request.getPathInfo();
	String method = request.getMethod();

	if(!method.equals("GET")) {
	    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only GET is supported");
	    return;
	}

	if (path == null || path.equals("/")) {
	    handleAllProduce(method, request, response);
	} else {
	    String key = path.substring(1).toUpperCase();
	    if (SEASONS.containsKey(key)) {
		handleSeason(method, key, request, response);
	    } else {
		handleMonth(method, key, request, response);
	    }
	}
    }
	    

    private void handleAllProduce(String method, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	Map<String, Set<String>> allProduce = new HashMap<>();
	for (NYSeasonalProduce monthEnum : NYSeasonalProduce.values()) {
	    Set<String> produce = new HashSet<>(monthEnum.getProduce());
	    produce.remove("No Produce Available");
	    allProduce.put(monthEnum.name(), produce);
	}
	
	response.setContentType("application/json");
	mapper.writeValue(response.getWriter(), allProduce);
    }

    private void handleMonth(String method, String month, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	try {
	    NYSeasonalProduce monthEnum = NYSeasonalProduce.valueOf(month);
	    Map<String, Object> result = new HashMap<>();

	    
	    result.put("month", monthEnum.name());
	    result.put("produce", monthEnum.getProduce());

	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), result);
	} catch (IllegalArgumentException e) {
	    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid month: + month");
			       }
	}

    private void handleSeason(String method, String season, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	List<NYSeasonalProduce> months = SEASONS.get(season);
	Set<String> seasonProduce = new HashSet<>();

	for (NYSeasonalProduce monthEnum : months) {
	    Set<String> produce = new HashSet<>(monthEnum.getProduce());
	    produce.remove("No Produce Available");
	    seasonProduce.addAll(produce);
	}

	Map<String, Object> result = Map.of("season", season, "produce", seasonProduce);

	response.setContentType("application/json");
	mapper.writeValue(response.getWriter(), result);
    }
}
