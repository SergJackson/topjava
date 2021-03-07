package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;


@ActiveProfiles(profiles = HSQL_DB)
public abstract class MealServiceHsqldbTest extends MealServiceTest {
}