package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository mealRepository = new MemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");
        List<Meal> mealsList = mealRepository.listMeal();
        List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceeded(mealsList, LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealsWithExceeded);
        request.setAttribute("DateTimeFormat", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
