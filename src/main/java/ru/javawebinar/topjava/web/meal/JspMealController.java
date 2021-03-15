package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    @GetMapping("/meals")
    public String getMeals(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @GetMapping("/meals/delete")
    public String deleteMeal(HttpServletRequest request, Model model) {
        int id = getId(request);
        super.delete(id);
        String uri = request.getRequestURI();
        String path = request.getContextPath();
        return "redirect:" +  "/meals";
    }

    @GetMapping("/meals/save")
    public String saveMeal(HttpServletRequest request, Model model) {
        int userId = SecurityUtil.authUserId();
        String mealId = request.getParameter("id");
        final Meal meal = mealId == null ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                super.service.get(getId(request), userId);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals")
    public String setMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(meal, getId(request));
        } else {
            super.create(meal);
        }
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}

