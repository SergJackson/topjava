package ru.javawebinar.topjava.repository;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

@Repository
public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Integer userId, Meal meal);

    // false if meal do not belong to userId
    boolean delete(Integer userId, int id);

    // null if meal do not belong to userId
    Meal get(Integer userId, int id);

    // ORDERED dateTime desc
    Collection<Meal> getAll(Integer userId);
}
