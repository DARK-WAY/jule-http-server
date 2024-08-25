package ru.otus.java.basic.http.server.lesson;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
