package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.TestMeals;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.ListStorage;
import ru.javawebinar.topjava.storage.Storage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private Storage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new ListStorage();
        storage.setAll(new TestMeals().getMeals());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");
        String dateTime = request.getParameter("dateTime");

        if (calories.isEmpty()) {
            throw new IllegalArgumentException("Calories cannot empty!");
        }
        int intCalories = 0;
        try {
            intCalories = Integer.valueOf(calories);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Calories must be a number!");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        if (!dateTime.isEmpty()) {
            try {
                localDateTime = LocalDateTime.parse(dateTime);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("DateTime must consist a date and time!");
            }
        }
        if (action.equals("add")) {
            storage.save(new Meal(LocalDateTime.parse(dateTime), description, Integer.valueOf(calories)));
        } else {
            Meal meal = storage.get(id);
            meal.setDateTime(localDateTime);
            meal.setDescription(description);
            meal.setCalories(intCalories);
            storage.update(meal);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String action = request.getParameter("action");
        log.debug("redirect to meals");
        if (action == null) {
            request.setAttribute("meals", storage.getAll());
            request.getRequestDispatcher("/WEB-INF/meals.jsp").forward(request, response);
            return;
        }

        Meal meal;
        switch (action) {
            case "delete":
                storage.delete(id);
                response.sendRedirect("meals");
                return;
            case "edit":
                meal = storage.get(id);
                break;
            case "add":
                meal = new Meal(LocalDateTime.now(), "(новый прием пищи)", 0);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/WEB-INF/edit.jsp").forward(request, response);
    }
}
