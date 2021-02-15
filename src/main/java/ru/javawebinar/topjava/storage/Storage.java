package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface Storage {
    void clear();

    void update(Meal meal);

    void save(Meal meal);

    Meal get(String id);

    void delete(String id);

    List<MealTo> getAll();

    void setAll(List<Meal> meals);

    int size();
}
