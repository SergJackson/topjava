package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 15, 0), "Обед", 1700),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 14, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 1500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("--- Cycle ---");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(16, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("--- Stream ---");
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(16, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> list = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        Integer sumColories = 0;
        meals.sort(Comparator.comparing(UserMeal::getDateTime));
        for (UserMeal meal : meals) {
            sumColories = meal.getDateTime().truncatedTo(ChronoUnit.DAYS).equals(currentDate) ? sumColories : 0;
            sumColories = sumColories + meal.getCalories();
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                list.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), sumColories >= caloriesPerDay));
            }
            currentDate = meal.getDateTime().truncatedTo(ChronoUnit.DAYS);
        }
        return list;

    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final AtomicReferenceArray<LocalDateTime> currentDate = new AtomicReferenceArray<>(new LocalDateTime[]{LocalDateTime.of(1970, 1, 1, 0, 0, 0)});
        final AtomicReferenceArray<Integer> sumColories = new AtomicReferenceArray<>(new Integer[]{0});
        List<UserMealWithExcess> userMealWithExcess = meals
                .stream()
                .sorted()
                .collect(
                        (Supplier<ArrayList<UserMealWithExcess>>) ArrayList::new,
                        (list, item) -> {
                            sumColories.set(0, item.getDateTime().truncatedTo(ChronoUnit.DAYS).equals(currentDate.get(0)) ? sumColories.get(0) : 0);
                            sumColories.set(0, sumColories.get(0) + item.getCalories());
                            list.add(new UserMealWithExcess(item.getDateTime(), item.getDescription(), item.getCalories(), sumColories.get(0) >= caloriesPerDay));
                            currentDate.set(0, item.getDateTime().truncatedTo(ChronoUnit.DAYS));
                        },
                        ArrayList::addAll)
                .stream()
                //.reduce((a1, a2)-> ((UserMealWithExcess)a1). + a2)
                .filter((a) -> isBetweenHalfOpen(a.getDateTime().toLocalTime(), startTime, endTime))
                .collect(Collectors.toList()
                );

        return userMealWithExcess;
    }
}
