package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.exception.ExistStorageException;
import ru.javawebinar.topjava.exception.NotExistStorageException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.MealsUtil.createTo;

public abstract class AbstractStorage<SK> implements Storage {

    protected static final int CALORIES_PER_DAY = 2000;

    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public void update(Meal meal) {
        LOG.info("Update " + meal);
        SK searchKey = getSearchKeyIfExists(meal.getId());
        doUpdate(searchKey, meal);
    }

    public void save(Meal meal) {
        LOG.info("Save " + meal);
        SK searchKey = getSearchKeyIfNotExists(meal.getId());
        doSave(searchKey, meal);
    }

    public Meal get(String id) {
        LOG.info("Get " + id);
        SK searchKey = getSearchKeyIfExists(id);
        return getMeal(searchKey);
    }

    public void delete(String id) {
        LOG.info("Delete " + id);
        SK searchKey = getSearchKeyIfExists(id);
        doDelete(searchKey);
    }

    private SK getSearchKeyIfExists(String id) {
        SK searchKey = getSearchKey(id);
        if (!isExist(searchKey)) {
            LOG.warning("Meal " + id + " not exist");
            throw new NotExistStorageException(id);
        }
        return searchKey;
    }

    private SK getSearchKeyIfNotExists(String id) {
        SK searchKey = getSearchKey(id);
        if (isExist(searchKey)) {
            LOG.warning("Meal " + id + " already exist");
            throw new ExistStorageException(id);
        }
        return searchKey;
    }

    public List<MealTo> getAll() {
        LOG.info("getAll");
        List<Meal> meals = getAllMeal();
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }

    public void setAll(List<Meal> meals) {
        LOG.info("SetAll");
        setAllMeal(meals);
    }


    protected abstract List<Meal> getAllMeal();

    protected abstract void setAllMeal(List<Meal> meals);

    protected abstract SK getSearchKey(String id);

    protected abstract boolean isExist(SK searchKey);

    protected abstract void doSave(SK searchKey, Meal meal);

    protected abstract void doDelete(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Meal meal);

    protected abstract Meal getMeal(SK searchKey);

}
