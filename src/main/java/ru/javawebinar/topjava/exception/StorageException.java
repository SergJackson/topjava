package ru.javawebinar.topjava.exception;

public class StorageException extends RuntimeException {
    private final String id;

    public StorageException(String message, String id) {
        super(message);
        this.id = id;
    }

    public StorageException(Exception e) {
        this(e.getMessage(), null, e);
    }

    public StorageException(String message, String id, Exception e) {
        super(message, e);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}