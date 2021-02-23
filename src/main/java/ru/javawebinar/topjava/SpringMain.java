package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            System.out.println("Проверяем Spring MealRestController");
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            System.out.println(" Добавляем еду");
            mealRestController.create(new Meal(LocalDateTime.now().minusHours(9), "Завтрак", 870));
            mealRestController.create(new Meal(LocalDateTime.now().minusHours(6), "Обед", 1100));
            mealRestController.create(new Meal(LocalDateTime.now().minusHours(1), "Ужин", 650));
            System.out.println(" Выводем весь список еды текущего пользователя");
            mealRestController.getAll().forEach(System.out::println);

            System.out.println(" Удаляем один прием пищи");
            int id = mealRestController
                    .getFiltered(
                            LocalDate.parse("2005-01-01"),
                            LocalTime.parse("01:00:00"),
                            LocalDate.now(),
                            LocalTime.now().minusHours(12))
                    .stream()
                    .findFirst()
                    .get()
                    .getId();
            System.out.println(" ...Выбран ID " + id);
            mealRestController.delete(id);

            System.out.println(" Обновляем случайный прием пищи");
            id = mealRestController.getAll().stream().findAny().get().getId();
            System.out.println(" ...Выбран ID " + id);
            Meal mealOld = mealRestController.get(id);
            Meal mealNew = new Meal(mealOld.getDateTime(), "Полдник", 250);
            mealNew.setId(id);
            mealRestController.update(mealNew, id);

            System.out.println(" Выводим общий итог изменений");
            mealRestController.getAll().forEach(System.out::println);

        }
    }
}
