package ru.otus.java.basic.http.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private HttpServer server;
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Dispatcher dispatcher;

    public ClientHandler(HttpServer server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.dispatcher = new Dispatcher();

        System.out.println("Клиент присоединился ");
        new Thread(() -> {
            try {
                if (!socket.isClosed()) {
                    byte[] buffer = new byte[8192];
                    int n = in.read(buffer);
                    String rawRequest = new String(buffer, 0, n);
                    HttpRequest request = new HttpRequest(rawRequest);
                    request.printInfo(true);
                    dispatcher.execute(request, out);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                disconnect();
            }
        }).start();
    }

    public void disconnect() {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
