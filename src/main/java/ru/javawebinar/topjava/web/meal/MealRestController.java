package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final MealService service;
    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.debug("getAll");
        return getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.debug("get meal with id={}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.debug("Create new meal \"{}\"", meal.getDescription());
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        log.debug("update meal with id={}", meal.getId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        log.debug("delete meal with id={}", id);
        service.delete(id, authUserId());
    }

    public List<MealTo> getFiltered(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        int userId = authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", fromDate, toDate, fromTime, toTime, userId);
        List<Meal> mealsByDate = service.getFiltered(fromDate, toDate, userId);
        return getFilteredTos(mealsByDate, authUserCaloriesPerDay(), fromTime, toTime);
    }
}