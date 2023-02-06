package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.meals;

public class InMemMealRepository implements MealRepository {
    private static MealRepository repository;

    private final AtomicInteger count = new AtomicInteger(0);
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    {
        meals.forEach(this::save);
    }

    public static synchronized MealRepository getRepository() {
        if (repository == null) {
            repository = new InMemMealRepository();
        }

        return repository;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public void save(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(count.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return;
        }

        mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public Meal show(int id) {
        return mealMap.get(id);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}
