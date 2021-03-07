package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JDBC;


@ActiveProfiles(profiles = JDBC)
public class MealServicePostgresJdbcTest extends MealServicePostgresTest {
}