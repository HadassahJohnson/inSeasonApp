# Example Requests

## Create Recipe

curl -X POST http://localhost:9999/inseasonapp/api/recipes \
-H "Content-Type: application/json" \
-d "{\"title\":\"Tomato Soup\",\"instructions\":\"Simmer ingredients\",\"ingredients\":[]}"

## Get All Recipes

curl http://localhost:9999/inseasonapp/api/recipes