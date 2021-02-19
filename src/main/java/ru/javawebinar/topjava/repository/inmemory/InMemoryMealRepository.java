package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Comparator<Meal> MEAL_COMPARATOR = Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder());

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    ReentrantLock reentrantLock = new ReentrantLock();

    {
        MealsUtil.meals.forEach(meal -> this.save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Meal m;
        reentrantLock.lock();
        try {
            Map<Integer, Meal> meals = repository.get(userId);
            if (meals == null) {
                meals = new ConcurrentHashMap<>();
                repository.put(userId, meals);
            }
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                meals.put(meal.getId(), meal);
                m = meal;
            } else {
                // handle case: update, but not present in storage
                m = meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
        } finally {
            reentrantLock.unlock();
        }
        return m;
    }

    @Override
    public boolean delete(int userId, int id) {
        Boolean result = false;
        reentrantLock.lock();
        try {
            Map<Integer, Meal> meals = repository.get(userId);
            if (meals != null) {
                result = meals.remove(id) != null;
            }
        } finally {
            reentrantLock.unlock();
        }
        return result;
    }

    @Override
    public Meal get(int userId, int id) {
        //if (repository.containsKey(userId)) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        //if (repository.containsKey(userId)) {
        return repository
                .get(userId)
                .values()
                .stream()
                .sorted(MEAL_COMPARATOR)
                .collect(Collectors.toList());
        //}
        //return new ArrayList<>();
    }

}

