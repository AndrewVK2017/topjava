<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit Meal</title>
</head>
<body>

<h2>Edit Meal</h2>

<form action="edit" method="POST">
    <input type="hidden" name="id" value="${meal.id}">
    <table>
        <tr>
            <td align="right">Date/Time :</td>
            <td>
                <input type="datetime" name="dateTime" value="${DateTimeFormat.format(meal.dateTime)}">
            </td>
        </tr>
        <tr>
            <td align="right">Description :</td>
            <td>
                <input type="text" name="description" value="${meal.description}">
            </td>
        </tr>
        <tr>
            <td align="right">Calories :</td>
            <td>
                <input type="text" name="calories" value="${meal.calories}">
            </td>
        </tr>
        <tr>
            <td><input type="submit" align="center" value="Submit"/></td>
        </tr>
    </table>
</form>

</body>
</html>
