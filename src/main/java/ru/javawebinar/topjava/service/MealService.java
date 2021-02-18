package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public void delete(Integer userId, int id) {
        checkNotFoundWithId(repository.delete(userId, id), id);
    }

    public Meal get(Integer userId, int id) {
        return checkNotFoundWithId(repository.get(userId, id), id);
    }

    public Collection<Meal> getAll(Integer userId) {
        return checkNotFound(repository.getAll(userId),"list of meal");
    }

    public void save(Integer userId, Meal meal) {
        checkNotFound(repository.save(userId, meal), "this saved meal");
    }

}