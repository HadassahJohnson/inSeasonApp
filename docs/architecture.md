# Architecture

## Technologies

- Java Servlets
- Jackson JSON library
- File-based persistence

## Data Storage

Recipes are stored in:

WEB-INF/recipes.json

The file is loaded when the servlet initializes.

## Routing

The servlet parses the request path and routes requests to handler methods such as:

handleCollection()
handleSingleRecipe()
handleIngredients()
handleSingleIngredient()