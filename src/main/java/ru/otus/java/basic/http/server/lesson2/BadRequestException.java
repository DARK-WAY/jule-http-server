package ru.otus.java.basic.http.server.lesson2;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
