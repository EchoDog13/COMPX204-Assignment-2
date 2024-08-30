import java.net.*;
import java.io.*;
import java.util.*;

/**
 * HttpServer to receive the request from the browser and send the response back
 * Capable of receiving GET and POST requests, and sending files back or 404
 * errors if the file is not found or the request is invalid
 */
public class HttpServer {

    public static void main(String args[]) {

        // Port number to listen on for incoming requests
        final int port = 52000;
        // Server socket to listen for incoming requests
        ServerSocket server = null;
        // Session to handle the request and response
        HttpServerSession session = null;

        try {
            // Create a server socket to listen for incoming requests
            server = new ServerSocket(port);
            // Print the port number the server is listening on
            System.out.println("Web Server Starting on port " + port);
            // Loop to keep the server running and listening for incoming requests
            while (true) {
                // Accept the incoming request and print out client details
                Socket s = server.accept();
                System.out.println("Client connected");
                InetAddress clientAddress = s.getInetAddress();
                String clientIP = clientAddress.getHostAddress();
                System.out.println(clientIP);

                // Create a new session to handle the request and response
                session = new HttpServerSession(s);
                // Start the session - calling the run method
                session.start();
            }
            // Catch any exceptions that occur
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            // Ensure the server socket is closed when done
            try {
                if (server != null && !server.isClosed()) {
                    server.close();
                }
                // Catch any exceptions that occur when closing the server socket
            } catch (Exception e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
    }
}
