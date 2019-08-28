package com.lhr.chatroom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server implements AutoCloseable {
    private final ServerSocket server;

    public Server(String host, int port, int backlogConnectionQueueLength) throws UnknownHostException, IOException {
        server = new ServerSocket(port, backlogConnectionQueueLength, InetAddress.getByName(host));
        System.out.println(Thread.currentThread() + " Created Server");
    }

    public void start() {
        System.out.println("Started");

        while (true) {
            acceptAndHandleClient(server);
        }
    }

    private void acceptAndHandleClient(ServerSocket server) {
        try (Socket clientSocket = server.accept()) {
            handleNewClient(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewClient(Socket clientSocket) throws IOException {
        System.out.println(Thread.currentThread() + " Received Connection from " + clientSocket);
        BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintStream os = new PrintStream(clientSocket.getOutputStream());
        // echo that data back to the client, except for QUIT.

        String line = null;

        while ((line = is.readLine()) != null) {
            if (line.equalsIgnoreCase("QUIT"))
                break;
            else {
                os.println(line);
                os.flush();
            }
        }

        os.println("Ok");
        os.flush();
        is.close();
        os.close();
    }

    public void close() throws IOException {
        server.close();
    }

    public static void main(String[] args) {
        try (Server server = new Server("localhost", 8080, 50)) {
            server.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}