import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static final int PORT = 5000;
    public static final String IP = "127.0.0.1";

    BufferedReader bufferedReader;
    private String name;

    public static void main(String[] args) {
        Main client = new Main();
        client.runClient();
    }

    //starting client
    public void runClient() {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter name: ");
        name = reader.nextLine().toUpperCase();

        try {
            Socket socket = new Socket(IP, PORT); // connecting to IP by PORT
            System.out.println("Connected to " + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Receiver());
            t.start();

            while (true) {
                System.out.print(">> ");
                String msg = reader.nextLine();
                if (!msg.equals("q")) {
                    printWriter.println(name + ":" + msg);
                    printWriter.flush();
                } else {
                    printWriter.println(name + " has left chat");
                    printWriter.flush();
                    printWriter.close();
//                    reader.close();
                    socket.close();
                }
            }
        } catch (IOException e) {
//            System.out.println("Cant send msg");
//            e.getMessage();
        }
        reader.close();

    }

    class Receiver implements Runnable {
        @Override
        public void run() {
            String message;
            try {
                while ((message = bufferedReader.readLine()) != null) {
                    String substring[] = message.split(":");
                    if (!substring[0].equals(name)) {
                        System.out.println(message);
                        System.out.println(">> ");
                    }

                }

            } catch (Exception e) {
                System.out.println("Connection closed");
//                e.printStackTrace();
//                System.out.println("the msg is: "+e.getMessage());
            }
        }
    }
}
