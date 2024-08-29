import java.net.*;
import java.io.*;
import java.util.*;

public class HttpServer {

    public static void main(String args[]) {
        final int port = 52000;
        ServerSocket server = null;
        HttpServerSession session = null;

        try {
            server = new ServerSocket(port);
            System.out.println("Web Server Starting on port " + port);

            Socket s = server.accept();
            System.out.println("Client connected");
            InetAddress clientAddress = s.getInetAddress();
            String clientIP = clientAddress.getHostAddress();
            System.out.println(clientIP);

            session = new HttpServerSession(s);

            session.start();
            HttpServerRequest request = new HttpServerRequest();
            // s.close();

        } catch (Exception e) {
            // System.out.println("Error: " + e.getMessage());
        } finally {
            // Ensure the server socket is closed when done
            try {
                if (server != null && !server.isClosed()) {
                    server.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

}
