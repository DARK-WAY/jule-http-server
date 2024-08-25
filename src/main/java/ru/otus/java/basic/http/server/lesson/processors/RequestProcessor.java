package ru.otus.java.basic.http.server.lesson.processors;

import ru.otus.java.basic.http.server.lesson.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

public interface RequestProcessor {
    void execute(HttpRequest request, OutputStream out) throws IOException;
}