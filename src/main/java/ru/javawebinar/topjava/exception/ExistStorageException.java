package ru.javawebinar.topjava.exception;

import java.sql.SQLException;

public class ExistStorageException extends StorageException {

    public ExistStorageException(String id, SQLException e) {
        super("Meal " + id + " already exist", id, e);
    }

    public ExistStorageException(String id) {
        super("Meal " + id + " already exist", id, null);
    }
}
