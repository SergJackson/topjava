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
        return null;
    }

    @Override
    protected boolean isExist(Integer searchKey) {
        return false;
    }

    @Override
    protected void doSave(Integer searchKey, Meal meal) {

    }

    @Override
    protected void doDelete(Integer searchKey) {

    }

    @Override
    protected void doUpdate(Integer searchKey, Meal meal) {

    }

    @Override
    protected Meal getMeal(Integer searchKey) {
        return null;
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
