package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@WebServlet("/api/seasonalrecipe/*")
public class SeasonalRecipeServlet extends HttpServlet {
    private ObjectMapper mapper;
    private List<Recipe> recipes;
    private String filePath;

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

	filePath = getServletContext().getRealPath("/WEB-INF/recipes.json");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	String path = request.getPathInfo();
	String method = request.getMethod();

	if (!method.equals("GET")) {
	    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only GET is supported");
	    return;
	}
	
	try {
	    File file = new File(filePath);
	    if (file.exists()) {
		recipes = Arrays.asList(mapper.readValue(file, Recipe[].class));
		recipes = new ArrayList<>(recipes);
	    } else {
		recipes = new ArrayList<>();
	    }
	} catch (Exception e) {
	    recipes = new ArrayList<>();
	}
	
	response.setContentType("application/json");

	if (path == null || path.equals("/")) {
	    mapper.writeValue(response.getWriter(), recipes);
	    return;
	} else {
	    String key = path.substring(1).toUpperCase();
	    if (SEASONS.containsKey(key)) {
		handleSeason(method, key, request, response);
	    } else {
		handleMonth(method, key, request, response);
	    }
	}
    }

    private void handleSeason(String method, String key, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	List<NYSeasonalProduce> months = SEASONS.get(key);

	if (months == null) {
	    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not a valid season");
	    return;
	}

	Set<String> combinedProduce = new HashSet<>();
	for (NYSeasonalProduce month : months) {
	    combinedProduce.addAll(month.getProduce());
	}

	Set<Recipe> results = new HashSet<>();

	for (Recipe r : recipes) {
	    for (Ingredient i : r.getIngredients()) {
		String ingredientName = i.getName().toLowerCase();

		for (String p : combinedProduce) {
		    if (ingredientName.contains(p.toLowerCase())) {
			results.add(r);
			break;
		    }
		}
	    }
	}
	mapper.writeValue(response.getWriter(), new ArrayList<>(results));
    }

    private void handleMonth(String method, String key, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	NYSeasonalProduce monthEnum;

	try {
	    monthEnum = NYSeasonalProduce.valueOf(key);
	} catch (IllegalArgumentException e) {
	    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not a valid month");
	    return;
	}
	Set<String> seasonalProduce = monthEnum.getProduce();
	Set<Recipe> results = new HashSet<>();

	for (Recipe r : recipes) {
	    for (Ingredient i : r.getIngredients()) {
		String ingredientName = i.getName().toLowerCase();

		for (String p : seasonalProduce) {
		    if (ingredientName.contains(p.toLowerCase())) {
			results.add(r);
			break;
		    }
		}
	    }
	}

	mapper.writeValue(response.getWriter(), new ArrayList<>(results));
    }
}
		    
