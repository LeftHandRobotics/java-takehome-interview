package com.lhr.chatroom;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;

public class Client implements AutoCloseable {
    private final Socket client;
    private final DataOutputStream os;
    private final BufferedReader is;
    BufferedReader reader;


    public Client(String host, int port) throws UnknownHostException, IOException {
        client = new Socket(host, port);
        os = new DataOutputStream(client.getOutputStream());
        is = new BufferedReader(new InputStreamReader(client.getInputStream()));
        reader =  new BufferedReader(new InputStreamReader(System.in));
    }

    public void sendReceive() {
        try {
            System.out.println("Connected");

            while (true) {
                System.out.print("> ");
                String msg = reader.readLine();
                os.writeBytes(msg + "\n");
                os.flush();

                // keep on reading from/to the socket till we receive the "Ok" from Server,
                // once we received that we break.
                String responseLine = is.readLine();

                if (responseLine != null && responseLine.equals("Ok")) {
                    return;
                }

                if (responseLine != null) {
                    System.out.println("< " + responseLine);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: hostname");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void close() throws IOException {
        is.close();
        os.close();
    }

    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
        int port = 8080;
        String host = "localhost";
        System.out.println("Connecting to server");

        try (Client client = new Client(host, port)) {
            client.sendReceive();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}