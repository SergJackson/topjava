package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {

    private List<Meal> storage = new ArrayList<>();

    @Override
    protected List<Meal> getAllMeal() {
        return storage;
    }

    @Override
    protected void setAllMeal(List<Meal> meals) {
        storage.addAll(meals);
    }

    @Override
    protected Integer getSearchKey(String id) {
        for (Meal meal : storage) {
            if (meal.getId().equals(id)) {
                return storage.indexOf(meal);
            }
        }
        return -1;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return searchKey > -1;
    }

    @Override
    protected void doSave(Integer searchKey, Meal meal) {
        storage.add(meal);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected void doUpdate(Integer searchKey, Meal meal) {
        storage.set(searchKey, meal);
    }

    @Override
    protected Meal getMeal(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}
