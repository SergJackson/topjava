package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service = new MealService(new InMemoryMealRepository());


    public Collection<Meal> getAll(Integer userId) {
        log.info("getAll");
        if (SecurityUtil.authUserId() != userId) {
            throw new NotFoundException("You aren't logged in!");
        }
        return service.getAll(userId);
    }

    public Meal get(Integer userId, int id) {
        log.info("get {}", id);
        if (SecurityUtil.authUserId() != userId) {
            throw new NotFoundException("You aren't logged in!");
        }
        return service.get(userId, id);
    }

    public void save(Integer userId, Meal meal) {
        log.info("save {}", meal);
        if (SecurityUtil.authUserId() != userId) {
            throw new NotFoundException("You aren't logged in!");
        }
        service.save(userId, meal);
    }

    public void delete(Integer userId, int id) {
        log.info("delete {}", id);
        if (SecurityUtil.authUserId() != userId) {
            throw new NotFoundException("You aren't logged in!");
        }
        service.delete(userId, id);
    }

}