package ru.otus.java.basic.http.server;

import ru.otus.java.basic.http.server.processors.AnotherHelloWorldRequestProcessor;
import ru.otus.java.basic.http.server.processors.HelloWorldRequestProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
