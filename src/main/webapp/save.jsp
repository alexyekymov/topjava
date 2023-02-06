<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${param.get("action") == "edit" ? "Edit meal" : "Add new Meal"}</title>
</head>
<body>

<form action="meals" method="post">
    <input type="hidden" name="id" id="id" value="${param.get("id")}">

    <label for="dateTime">Date: </label>
    <input type="datetime-local" name="dateTime" id="dateTime"
           value="${meal.dateTime ne null ? meal.dateTime : ''}">

    <label for="description">Description: </label>
    <input type="text" name="description" id="description"
           value="${meal.description ne null ? meal.description : ''}">

    <label for="calories">Calories: </label>
    <input type="number" name="calories" id="calories"
           value="${meal.calories ne null ? meal.calories : ''}">

    <button type="submit">Save!</button>
</form>
</body>
</html>
