import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {

    public static void main(String args[]) {
        final int port = 52000;

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Web Server Starting on port " + port);

            while (true) {
                Socket s = server.accept();
                System.out.println("Client connected");
                InetAddress clientAddress = s.getInetAddress();
                String clientIP = clientAddress.getHostAddress();
                System.out.println(clientIP);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public class HttpServerSession extends Thread {
        private Socket s;

    }

}
