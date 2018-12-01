package com.adurlak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Main {

    ArrayList serverList;
    PrintWriter printWriter;

    public static void main(String[] args) {
        Main server = new Main();
        server.startChatApp();
    }

    // start serwera
    public void startChatApp() {
        serverList = new ArrayList();
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            while (true) {
                Socket socket = serverSocket.accept(); // akceptuje polaczenia przychodzace na porcie 5000
                System.out.println("Starting chat: " + serverSocket); // wyswietli sie informacja o serwerze
                printWriter = new PrintWriter(socket.getOutputStream());
                serverList.add(printWriter); // dodajemy kazdego klienta
                Thread t = new Thread((new ServerClient(socket)));
                t.start();
            }

        } catch (IOException e) {
            System.out.println("Server's start failed: " + e.getMessage());

        }
    }

    //odczytujemy i rozsylamy dane od klientow
    class ServerClient implements Runnable {

        Socket socket;
        BufferedReader bufferedReader;

        public ServerClient(Socket socketClient) {
            try {
                System.out.println("Connected");
                socket = socketClient;
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.out.println("Client couldnt connect to server: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            String string;
            PrintWriter printWriter = null;

            try {
                while ((string = bufferedReader.readLine()) != null) {
                    System.out.println("Reply >> " +string);

                    Iterator it = serverList.iterator();
                    while (it.hasNext()) {
                        printWriter = (PrintWriter) it.next();
                        printWriter.println(string);
                        printWriter.flush();
                    }
                }
            } catch (Exception e) {

            }
        }
    }
}
