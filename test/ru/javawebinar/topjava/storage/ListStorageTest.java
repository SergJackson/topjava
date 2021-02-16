package ru.javawebinar.topjava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.TestMeals;
import ru.javawebinar.topjava.exception.NotExistStorageException;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class ListStorageTest {
    private static final int COUNT_MEAL_TEST = 7;
    private Storage storage = new ListStorage();

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.setAll(new TestMeals().getMeals());
    }

    @Test
    public void setAllMeal() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
        storage.clear();
        assertEquals(0, storage.size());
        storage.setAll(new TestMeals().getMeals());
        assertEquals(COUNT_MEAL_TEST, storage.size());
    }

    @Test
    public void doSave() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
        Meal meal = new Meal(LocalDateTime.now(),"Lunth", 800);
        storage.save(meal);
        assertEquals(COUNT_MEAL_TEST + 1, storage.size());
        assertEquals(meal, storage.get(meal.getId()));
    }

    @Test
    public void doDelete() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
        storage.delete(storage.getAll().get(0).getId());
        assertEquals(COUNT_MEAL_TEST - 1, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void doDeleteIsNotExist() {
        storage.delete("0000000-000000-000000000000");
    }

    @Test
    public void doUpdate() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
        Meal meal = storage.get(storage.getAll().get(1).getId());
        meal.setCalories(meal.getCalories() + 100);
        storage.update(meal);
        assertEquals(COUNT_MEAL_TEST, storage.size());
        assertEquals(meal.getCalories(), storage.get(meal.getId()).getCalories());
    }

    @Test
    public void clear() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void size() {
        assertEquals(COUNT_MEAL_TEST, storage.size());
    }
}