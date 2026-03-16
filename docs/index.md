# InSeason Recipe API

A RESTful API built with Java Servlets that allows users to create and manage recipes and ingredients.

## Features

- Create, read, update, and delete recipes
- Nested ingredient resources
- UUID-based identifiers
- JSON persistence
- REST-style routing

## Example Request

curl -X POST http://localhost:9999/inseasonapp/api/recipes \
-H "Content-Type: application/json" \
-d "{\"title\":\"Pancakes\",\"instructions\":\"Mix and fry\",\"ingredients\":[]}"

## Documentation

- [API Reference](api.md)
- [Usage Examples](examples.md)
- [Architecture](architecture.md)