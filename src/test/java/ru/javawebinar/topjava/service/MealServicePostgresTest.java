package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;


@ActiveProfiles(profiles = POSTGRES_DB)
public abstract class MealServicePostgresTest extends MealServiceTest {
}