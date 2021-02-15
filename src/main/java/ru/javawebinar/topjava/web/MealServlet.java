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
            case "view":
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
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/view.jsp" : "/WEB-INF/edit.jsp")
        ).forward(request, response);

    }
}
