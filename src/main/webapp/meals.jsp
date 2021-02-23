<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>

    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .card-body {
            width: 50%;
            border-radius: 5px;

        }

        .card-head {
            width: 100%;
            background-color: antiquewhite;
            border-radius: 5px;
            border: 1px;
        }

        .card-footer {
            width: 100%;
            text-align: right;
            background-color: azure;
            border-radius: 5px;
        }

        dl {
            margin: 8px 0;
            padding: 0;
        }

        dt {
            display: inline-block;
            width: 170px;
        }

        dd {
            display: inline-block;
            margin-left: 8px;
            vertical-align: top;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals: userId = ${userId}</h2>
    <div class="card-body">
        <form id="filter" name="filter" method="get">
            <div class="card-head">

                <dl>
                    <dt>От даты (включая)</dt>
                    <dd><input type="date" name="startDate" id="startDate" value="${startDate}" autocomplete="off"/>
                    </dd>
                </dl>
                <dl>
                    <dt>До даты (включая)</dt>
                    <dd><input type="date" name="endDate" id="endDate" value="${endDate}" autocomplete="off"></dd>
                </dl>
                <dl>
                    <dt>От времени (включая)</dt>
                    <dd><input type="time" name="startTime" id="startTime" value="${startTime}" autocomplete="off"></dd>
                </dl>
                <dl>
                    <dt>До времени (исключая)</dt>
                    <dd><input type="time" name="endTime" id="endTime" value="${endTime}" autocomplete="off"></dd>
                </dl>
            </div>
            <div class="card-footer">
                <button type="reset">Отменить</button>
                <button type="submit">Отфильтровать</button>
            </div>
        </form>
    </div>
    <hr/>
    <a href="meals?&action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>