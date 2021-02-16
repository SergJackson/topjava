<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">

</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<section>
    <h4><a href="?&action=add">Add Meal</a></h4>
    <table id="t01">
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
            <tr>
                <c:set var="color" value="black"/>
                <c:if test="${meal.excess}">
                    <c:set var="color" value="red"/>
                </c:if>

                <td class="${color}">${meal.dateTime.format( DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</td>
                <td class="${color}">${meal.description}</td>
                <td class="${color}">${meal.calories}</td>
                <td><a href="?id=${meal.id}&action=edit">Update</a></td>
                <td><a href="?id=${meal.id}&action=delete">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>