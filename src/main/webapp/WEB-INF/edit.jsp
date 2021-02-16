<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>Meals</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <jsp:useBean id="action" type="java.lang.String" scope="request"/>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<section>
    <form method="post" action="" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="id" value="${meal.id}">
        <input type="hidden" name="action" value="${action}">
        <dl>
            <dt>DateTime:</dt>
            <dd><input type="datetime-local" name="dateTime" size=50 value="${meal.dateTime}"></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" name="description" size=30 value="${meal.description}"></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input type="text" name="calories" size=30 value="${meal.calories}"></dd>
        </dl>
        <button type="submit">Save</button>
        <button type="reset" onclick="window.history.back()">Cancel</button>
    </form>
</section>
</body>
</html>