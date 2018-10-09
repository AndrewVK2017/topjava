<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Add Meal</title>
</head>
<body>
<h2>Add Meal</h2>

<form action="add" method="POST">
    <table>
        <tr>
            <td align="right">Date/Time :</td>
            <td>
                <input type="datetime" name="dateTime">
            </td>
        </tr>
        <tr>
            <td align="right">Description :</td>
            <td>
                <input type="text" name="description">
            </td>
        </tr>
        <tr>
            <td align="right">Calories :</td>
            <td>
                <input type="text" name="calories">
            </td>
        </tr>
        <tr>
            <td><input type="submit" align="center" value="Submit"/></td>
        </tr>
    </table>
</form>
</body>
</html>
