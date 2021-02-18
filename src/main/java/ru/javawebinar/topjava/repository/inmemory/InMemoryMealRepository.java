package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder());

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(1, meal));
    }

    @Override
    public Meal save(Integer userId, Meal meal) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            meals = new ConcurrentHashMap<>();
            repository.put(userId, meals);
        }
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(Integer userId, int id) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(Integer userId, int id) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (repository.containsKey(userId)) {
            return meals.get(id);
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        if (repository.containsKey(userId)) {
            return repository
                    .get(userId)
                    .values()
                    .stream()
                    .sorted(MEAL_COMPARATOR)
                    .collect(Collectors.toList());
        }
        return new ConcurrentHashMap<Integer, Meal>().values();
    }

}

