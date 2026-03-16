# InSeason Recipe API

## Overview

InSeason is a simple RESTful web API built with Java Servlets that allows users to store and manage recipes and their ingredients. Recipes are persisted in a JSON file and can be created, viewed, updated, and deleted through HTTP requests.

Each recipe contains:

* A unique **UUID**
* A **title**
* **instructions**
* A list of **ingredients**

Each ingredient also has its own UUID, name, and quantity.

The API stores all recipes in `recipes.json` located in the `/WEB-INF` directory so that data persists between server restarts.

---

## Technology Stack

* Java
* Jakarta Servlets
* Jackson (JSON serialization/deserialization)
* JSON file storage
* curl for testing endpoints

---

## Base URL

```
http://localhost:9999/inseasonapp/api/recipes
```

---

## Data Format

### Recipe

```json
{
  "id": "uuid",
  "title": "Recipe Title",
  "instructions": "Cooking instructions",
  "ingredients": [
    {
      "id": "uuid",
      "name": "Ingredient name",
      "quantity": "Amount"
    }
  ]
}
```

### Ingredient

```json
{
  "id": "uuid",
  "name": "Ingredient name",
  "quantity": "Amount"
}
```

IDs are generated automatically by the server using UUIDs.

---

# API Endpoints

## Get All Recipes

```
GET /api/recipes
```

Returns a list of all recipes.

Example:

```
curl http://localhost:9999/inseasonapp/api/recipes
```

---

## Get a Single Recipe

```
GET /api/recipes/{recipeId}
```

Example:

```
curl http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}
```

---

## Create a Recipe

```
POST /api/recipes
```

Example:

```
curl -X POST http://localhost:9999/inseasonapp/api/recipes -H "Content-Type: application/json" -d "{\"title\":\"Pancakes\",\"instructions\":\"Mix and fry\",\"ingredients\":[]}"
```

The server automatically generates UUIDs for the recipe and its ingredients.

---

## Update a Recipe

```
PATCH /api/recipes/{recipeId}
```

Example:

```
curl -X PATCH http://localhost:9999/inseasonapp/api/recipes/{recipeUUID} -H "Content-Type: application/json" -d "{\"title\":\"Updated Pancakes\"}"
```

You can update:

* title
* instructions
* ingredients

---

## Delete a Recipe

```
DELETE /api/recipes/{recipeId}
```

Example:

```
curl -X DELETE http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}
```

---

# Ingredient Endpoints

Ingredients are nested under recipes.

## Get Ingredients for a Recipe

```
GET /api/recipes/{recipeId}/ingredients
```

Example:

```
curl http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}/ingredients
```

---

## Add an Ingredient

```
POST /api/recipes/{recipeId}/ingredients
```

Example:

```
curl -X POST http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}/ingredients -H "Content-Type: application/json" -d "{\"name\":\"Flour\",\"quantity\":\"1 cup\"}"
```

---

## Get a Single Ingredient

```
GET /api/recipes/{recipeId}/ingredients/{ingredientId}
```

---

## Update an Ingredient

```
PATCH /api/recipes/{recipeId}/ingredients/{ingredientId}
```

Example:

```
curl -X PATCH http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}/ingredients/{ingredientUUID} -H "Content-Type: application/json" -d "{\"quantity\":\"2 cups\"}"
```

---

## Delete an Ingredient

```
DELETE /api/recipes/{recipeId}/ingredients/{ingredientId}
```

Example:

```
curl -X DELETE http://localhost:9999/inseasonapp/api/recipes/{recipeUUID}/ingredients/{ingredientUUID}
```

---

# Storage

Recipes are stored in:

```
WEB-INF/recipes.json
```

If the file does not exist when the server starts, it will be created automatically.

---

# Running the Application

1. Deploy the servlet application to a servlet container (e.g., Tomcat).
2. Start the server.
3. Use `curl`, Postman, or a browser to interact with the API.

Example server URL:

```
http://localhost:9999/inseasonapp/
```

---

# Features

* RESTful API design
* UUID-based resource identifiers
* JSON persistence
* Nested ingredient resources
* CRUD operations for recipes and ingredients
* Search functionality for recipes by title
