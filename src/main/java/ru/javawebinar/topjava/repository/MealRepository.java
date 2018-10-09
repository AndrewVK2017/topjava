package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;


public interface MealRepository {
    List<Meal> listMeal();

    void addMeal(Meal meal);

    void deleteMeal(int id);

    void updateMeal(Meal meal);

    Meal getMeal(int id);

    int generateId();
}
