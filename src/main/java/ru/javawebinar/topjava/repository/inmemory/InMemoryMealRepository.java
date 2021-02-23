package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()).thenComparingInt(Meal::getId);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(1, meal));
        MealsUtil.meals_2.forEach(meal -> this.save(2, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        repository.computeIfAbsent(userId, id -> {
            Map<Integer, Meal> meals = new ConcurrentHashMap<>();
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                meals.put(meal.getId(), meal);
                return meals;
            } else {
                meals.computeIfPresent(meal.getId(), (idx, oldMeal) -> meal);
                return meals;
            }
        });
        repository.computeIfPresent(userId, (id, meals) -> {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                meals.put(meal.getId(), meal);
            } else {
                meals.computeIfPresent(meal.getId(), (idx, oldMeal) -> meal);
            }
            return meals;
        });
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.values().stream()
                    .sorted(MEAL_COMPARATOR)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}

