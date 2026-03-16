package com.nowhere;

import java.io.*;
import java.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@WebServlet("/api/recipes/*")
public class RecipeServlet extends HttpServlet {

    private List<Recipe> recipes;
    private ObjectMapper mapper;
    private String filePath;

    @Override
    public void init() throws ServletException {
	mapper = new ObjectMapper();
	//pretty print everything cause this is chaos
	mapper.findAndRegisterModules();
	mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);

	try {
	    filePath = getServletContext().getRealPath("/WEB-INF/recipes.json");
	    File file = new File(filePath);

	    if (file.exists() && file.length()>0) {
		recipes = mapper.readValue(
					   file,
					   new TypeReference<List<Recipe>>() {});
	    } else {
		recipes = new ArrayList<>();
		mapper.writeValue(file, recipes);
	    }
	} catch (IOException e) {
	    throw new ServletException(e);
	}
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	String path = request.getPathInfo();
	String method = request.getMethod(); //this gets get, post, delete, etc

	if (path == null || path.equals("/")) {
	    handleCollection(method, request, response);
	    return;
	}

	String[] parts = path.substring(1).split("/");

	try {
	    UUID recipeID = UUID.fromString(parts[0]);
	    Recipe recipe = recipes.stream()
		.filter(r -> r.getId().equals(recipeID))
		.findFirst()
		.orElse(null);
	    
	    if (recipe == null) {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
	    }
	    if (parts.length == 1) {
		handleSingleRecipe(method, recipe, request, response);
	    } else if (parts.length == 2 && parts[1].equals("ingredients")) {
		handleIngredients(method, recipe, request, response);
	    } else if (parts.length == 3 && parts[1].equals("ingredients")) {
		try {
		    UUID ingredientId = UUID.fromString(parts[2]);
		    handleSingleIngredient(method, recipe, ingredientId, request, response);
		} catch (IllegalArgumentException e) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid UUID");
		    //return 404 instead
		    return;
		}
	    } else {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    }
	} catch (IllegalArgumentException e) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid recipe UUID");
	}
    }
    
    private void handleCollection(String method, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	if (!method.equals("GET") && !method.equals("POST")) {
	    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
	
	if (method.equals("GET")) {
	    String searchTerm = request.getParameter("search");
	    List<Recipe> results = recipes;

	    if (searchTerm != null && !searchTerm.isBlank()) {
		String lowerSearch = searchTerm.toLowerCase();
		results = recipes.stream()
		    .filter(r -> r.getTitle().toLowerCase().contains(lowerSearch))
		    .toList();
	    }	    
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), results);
	    return;
	    
	} else if (method.equals("POST")) {
	    Recipe newRecipe = mapper.readValue(request.getInputStream(), Recipe.class);
	    newRecipe.setId(UUID.randomUUID());
	    if (newRecipe.getIngredients() == null) {
		newRecipe.setIngredients(new ArrayList<>());
	    } else {
		for (Ingredient i : newRecipe.getIngredients()) {
		    i.setId(UUID.randomUUID());
		}
	    }
	    recipes.add(newRecipe);
	    //save recipes to file
	    mapper.writeValue(new File(filePath), recipes);

	    response.setStatus(HttpServletResponse.SC_CREATED);
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), newRecipe);
	}
    }

    private void handleSingleRecipe(String method, Recipe recipe, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
   	if (method.equals("GET")) {
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), recipe);
	} else if (method.equals("PATCH")) {
	    Recipe updated = mapper.readValue(request.getInputStream(), Recipe.class);
	    if (updated.getTitle() != null) {
		recipe.setTitle(updated.getTitle());
	    } else if (updated.getInstructions() != null) {
		recipe.setInstructions(updated.getInstructions());
	    } else if (updated.getIngredients() != null) {
		recipe.setIngredients(updated.getIngredients());
	    }
	    mapper.writeValue(new File(filePath), recipes);
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), recipe);
	    
	} else if (method.equals("DELETE")) {
	    recipes.remove(recipe);
	    mapper.writeValue(new File(filePath), recipes);
	    
	} else {
	    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
    }

    private void handleIngredients(String method, Recipe recipe, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	if (method.equals("GET")) {
	    response.setContentType("application/json");	    	    
	    mapper.writeValue(response.getWriter(), recipe.getIngredients());
	} else if (method.equals("POST")) {
	    Ingredient newIngredient = mapper.readValue(request.getInputStream(), Ingredient.class);
	    newIngredient.setId(UUID.randomUUID());
	    recipe.getIngredients().add(newIngredient);
	    mapper.writeValue(new File(filePath), recipes);

	    response.setStatus(HttpServletResponse.SC_CREATED);
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), newIngredient);
	}
    }

    private void handleSingleIngredient(String method, Recipe recipe, UUID ingredientId, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	Ingredient ingredient = recipe.getIngredients().stream()
	    .filter(i -> i.getId().equals(ingredientId))
	    .findFirst()
	    .orElse(null);

	if (ingredient == null) {
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    return;
	}
	
	if (method.equals("GET")) {
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), ingredient);
	} else if (method.equals("DELETE")) {
	    recipe.getIngredients().remove(ingredient);
	    mapper.writeValue(new File(filePath), recipes);
	    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	} else if (method.equals("PATCH")) {
	    Map<String, Object> updates = mapper.readValue(request.getInputStream(), Map.class);
	    if (updates.containsKey("name")) {
		ingredient.setName((String) updates.get("name"));
	    }

	    if (updates.containsKey("quantity")) {
		ingredient.setQuantity((String) updates.get("quantity"));
	    }

	    mapper.writeValue(new File(filePath), recipes);
	    response.setContentType("application/json");
	    mapper.writeValue(response.getWriter(), ingredient);
	} else {
	    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}
    }
}
