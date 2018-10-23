package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-app-jdbc.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_USER_ID_1, USER_ID);
        assertMatch(meal, MEAL_USER_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getNotBelong() throws Exception {
        service.get(MEAL_USER_ID_1, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL_USER_ID_1, USER_ID);
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEALS_USER_DELETED);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotFound() throws Exception {
        service.delete(1, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deletedNotBelong() throws Exception {
        service.delete(MEAL_USER_ID_1, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(service.getBetweenDates(LocalDate.parse("2015-05-30"), LocalDate.parse("2015-05-30"), USER_ID), MEAL_USER_3, MEAL_USER_2, MEAL_USER_1);
    }

    @Test
    public void getBetweenDateTimes() {
        assertMatch(service.getBetweenDateTimes(LocalDateTime.of(2015, Month.MAY, 31, 10, 0),
                LocalDateTime.of(2015, Month.MAY, 31, 20, 0), USER_ID), MEAL_USER_6, MEAL_USER_5, MEAL_USER_4);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, MEALS_ADMIN);
    }

    @Test
    public void update() {
        Meal updated = new Meal(MEAL_USER_1);
        updated.setDescription("МиниЗавтрак");
        updated.setCalories(400);
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_USER_ID_1, USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotBelong() throws Exception{
        Meal updated = new Meal(MEAL_USER_1);
        updated.setDescription("МиниЗавтрак");
        updated.setCalories(400);
        service.update(updated, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, Month.JUNE, 29, 20, 0), "АдминУжин", 499);
        Meal created = service.create(newMeal, ADMIN_ID);
        newMeal.setId(created.getId());
        assertMatch(service.getAll(ADMIN_ID), MEAL_ADMIN_3, MEAL_ADMIN_2, MEAL_ADMIN_1, newMeal);
    }

    @Test
    public void createReturnValue() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, Month.JUNE, 29, 20, 0), "АдминУжин", 499);
        Meal created = service.create(newMeal, ADMIN_ID);
        newMeal.setId(created.getId());
        assertMatch(service.get(created.getId(),ADMIN_ID), newMeal);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createCheckIndex() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, Month.JUNE, 30, 20, 0), "Ужин", 500);
        Meal created = service.create(newMeal, ADMIN_ID);
    }
}