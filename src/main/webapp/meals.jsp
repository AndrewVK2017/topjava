<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>

<table border=1>
    <thead>
    <tr>
        <th>Meal Id</th>
        <th>Date/Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <tr style="color:${meal.exceed ? 'red' : 'green'}">
            <td><c:out value="${meal.id}"/></td>
            <td><c:out value="${DateTimeFormat.format(meal.dateTime)}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <td><a href="meals/edit?mealId=${meal.id}">Update</a></td>
            <td><a href="meals/delete?mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<h3><a href="meals/add">Add Meal</a></h3>
</body>
</html>
