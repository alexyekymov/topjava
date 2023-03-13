package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createSameDateTime() {
        assertThrows(DataAccessException.class, () -> service.create(getNewWithSameDateTime(), USER_ID));
    }

    @Test
    public void createForNotExistingUser() {
        assertThrows(DataAccessException.class, () -> service.create(getNew(), 1));
    }

    @Test
    public void update() {
        Meal updated = getNew();
        updated.setId(meal1.getId());
        updated.setDateTime(LocalDateTime.of(2023, Month.MARCH, 13, 10, 0));
        updated.setDescription("Test Meal");
        updated.setCalories(999);
        service.update(updated, USER_ID);
        assertMatch(service.get(updated.getId(), USER_ID), updated);
    }

    @Test
    public void updateSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, GUEST_ID));
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), USER_ID));
    }

    @Test
    public void deleteSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.delete(meal1.getId(), GUEST_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(meal1.getId(), USER_ID);
        assertMatch(meal, meal1);
    }

    @Test
    public void getSomeoneElses() {
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), GUEST_ID));
    }

    @Test
    public void getBetweenInclusive() {
        assertMatch(service.getBetweenInclusive(START_END_DATE, START_END_DATE, USER_ID), getList());
    }

    @Test
    public void getBetweenInclusiveEmptyResult() {
        assertMatch(service.getBetweenInclusive(START_END_DATE, START_END_DATE, GUEST_ID), Collections.emptyList());
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(USER_ID), getListOfAll());
    }

    @Test
    public void getAllEmptyResult() {
        assertMatch(service.getAll(GUEST_ID), Collections.emptyList());
    }
}