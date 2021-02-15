package ru.javawebinar.topjava.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String id) {
        super("Meal " + id + " not exist", id);
    }
}
