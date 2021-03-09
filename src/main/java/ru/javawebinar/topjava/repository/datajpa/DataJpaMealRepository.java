package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.id(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository
                .findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll(userId);
    }

    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
    }

    /*
        public interface BetweenHalfOpen {
            List<Meal> getHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
        }

        @Component
        @Profile("hsqldb")
        public class DevDatasourceConfig implements BetweenHalfOpen {
            @Override
            public List<Meal> getHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
                return crudRepository.getBetweenHalfOpen(userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
            }
        }

        @Component
        @Profile("postgres")
        public class ProductionDatasourceConfig implements BetweenHalfOpen {
            @Override
            public List<Meal> getHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
                return crudRepository.getBetweenHalfOpen(userId, startDateTime, endDateTime);
            }
        }

        @Override
        public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
            return betweenHalfOpen.getHalfOpen(startDateTime, endDateTime, userId);
        }
    */
}