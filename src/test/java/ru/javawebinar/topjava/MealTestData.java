package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_USER_ID_1 = START_SEQ + 2;

    public static final Meal MEAL_USER_1 = new Meal(MEAL_USER_ID_1, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_USER_2 = new Meal(START_SEQ + 3, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_USER_3 = new Meal(START_SEQ + 4, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_USER_4 = new Meal(START_SEQ + 5, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_USER_5 = new Meal(START_SEQ + 6, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_USER_6 = new Meal(START_SEQ + 7, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 520);
    public static final Meal MEAL_ADMIN_1 = new Meal(START_SEQ + 8, LocalDateTime.of(2015, Month.JUNE, 30, 10, 0), "АдминЗавтрак", 1000);
    public static final Meal MEAL_ADMIN_2 = new Meal(START_SEQ + 9, LocalDateTime.of(2015, Month.JUNE, 30, 13, 0), "АдминОбед", 500);
    public static final Meal MEAL_ADMIN_3 = new Meal(START_SEQ + 10, LocalDateTime.of(2015, Month.JUNE, 30, 20, 0), "АдминУжин", 499);

    public static final List<Meal> MEALS_USER = Stream.of(
            MEAL_USER_2, MEAL_USER_3, MEAL_USER_4, MEAL_USER_5, MEAL_USER_6)
            .sorted(Comparator.comparing(Meal::getDateTime).reversed())
            .collect(Collectors.toList());

    public static final List<Meal> MEALS_ADMIN = Stream.of(
            MEAL_ADMIN_1, MEAL_ADMIN_2, MEAL_ADMIN_3)
            .sorted(Comparator.comparing(Meal::getDateTime).reversed())
            .collect(Collectors.toList());

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

}
