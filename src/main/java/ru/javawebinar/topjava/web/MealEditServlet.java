package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MemoryMealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealEditServlet extends HttpServlet {
    private static final Logger log = getLogger(MealEditServlet.class);
    private MealRepository mealRepository = new MemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("edite to meal");
        request.setAttribute("meal", mealRepository.getMeal(Integer.parseInt(request.getParameter("mealId"))));
        request.setAttribute("DateTimeFormat", DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
        request.getRequestDispatcher("/editmeal.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("update to meal");
        request.setCharacterEncoding("UTF-8");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        mealRepository.updateMeal(new Meal(Integer.parseInt(request.getParameter("id")),
                LocalDateTime.parse(request.getParameter("dateTime"), timeFormatter),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories"))));
        log.debug("update mael end");
        response.sendRedirect("/topjava/meals");
    }
}
