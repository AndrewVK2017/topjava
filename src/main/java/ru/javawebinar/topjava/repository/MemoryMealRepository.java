package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealRepository implements MealRepository {

    private static List<Meal> meals = new CopyOnWriteArrayList<>();

    private static AtomicInteger count = new AtomicInteger(0);

    static {
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Meal getMeal(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst().get();
    }

    @Override
    public List<Meal> listMeal() {
        return this.meals;
    }

    @Override
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    @Override
    public void deleteMeal(int id) {
        meals.removeIf(meal -> meal.getId() == id);
    }

    @Override
    public void updateMeal(Meal meal) {
        for (Meal m : meals) {
            if (m.getId() == meal.getId()) meals.set(meals.indexOf(m), meal);
        }
    }

    @Override
    public int generateId() {
        return count.incrementAndGet();
    }
}
