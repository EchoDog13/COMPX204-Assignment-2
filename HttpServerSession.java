import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * HttpServerSession
 */

public class HttpServerSession extends Thread {
    // private socket
    private Socket socket;
    // private reader to read request details
    private BufferedReader reader;
    // private writer to write response back to browser
    private BufferedOutputStream writer;
    // private request instance to process request
    private HttpServerRequest request;

    /**
     * Constructor of socket session
     * 
     * @param socket parameter of the socket which is connected
     */
    public HttpServerSession(Socket socket) {
        this.socket = socket;
    }

    /**
     * Run method to handle the request and response when session is started
     */
    @Override
    public void run() {
        // Print the client IP address and shows that client is connected
        System.out.println("Client connected");
        InetAddress clientAddress = socket.getInetAddress();
        String clientIP = clientAddress.getHostAddress();
        System.out.println(clientIP);

        try {

            // Initialize the reader and writer
            // Reader to read HTTP request
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Writer to write HTTP response
            writer = new BufferedOutputStream(socket.getOutputStream());

            // Initialize the request class used to process the request
            request = new HttpServerRequest();

            // Process the request

            // Store the line read from the reader
            String line;
            // Read the line from the reader and process the request while the line is not
            // null and not empty
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                // Print the line read from the reader
                System.out.println(line);
                // Process the request using HttpServerRequest class
                request.process(line);
            }

            // Send the response after processing the request
            sendResponse(writer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the socker when finished
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                // Catch error if socket is not closed
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    /**
     * Used to send the response to the client
     * 
     * @param bos BufferedOutputStream used to send response to client
     */
    private void sendResponse(BufferedOutputStream bos) {
        // FileInputStream to read the file to send
        FileInputStream fis = null;
        try {
            // Determine the file path based on the request by getting the host
            String hostPath = request.getHost();

            int colonIndex = 0;
            // Find the index of the colon in the host path
            colonIndex = hostPath.indexOf(':');

            // Extract the substring before the colon
            String prePortHost = hostPath.substring(0, colonIndex);

            // Combine the prePortHost with the file path to get the full path
            String path = prePortHost + "/" + request.getFile();
            // Create a file instance with the path
            File file = new File(path);
            // Print the path
            System.out.println("File path: " + path);
            // Print if the file exists
            System.out.println("File exists: " + file.exists());

            // Check if the file exists and is a file and if so send the file
            if (file.exists() && file.isFile()) {
                // Send HTTP headers
                println(bos, "HTTP/1.1 200 OK");
                // println(bos, "Content-Type: text/html; charset=UTF-8");
                println(bos, "Content-Length: " + file.length());
                println(bos, ""); // Blank line to separate headers from the body

                // Send the file content
                // Synchronize the file input stream to avoid multiple threads reading the file
                synchronized (this) {
                    // Create a new instance file input stream to read the file
                    fis = new FileInputStream(file);
                    // Create a buffer to store the file content
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                        // Thread.sleep(800);
                    }
                }

            } else {
                // Handle file not found case by sending a 404 response
                println(bos, "HTTP/1.1 404 Not Found");
                println(bos, "");
                println(bos, "404: File Not Found");
                System.out.println("File not found at path: " + path);
            }
            // Flush the buffer to send the response
            bos.flush();
            // Catch error if response cannot be sent
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
        } finally {
            // Close the file input stream when finished
            try {
                if (fis != null) {
                    fis.close();
                }
                // Catch error if file input stream cannot be closed
            } catch (IOException e) {
                System.out.println("Error closing FileInputStream: " + e.getMessage());
            }
        }
    }

    /**
     * Used to print the response to the client
     * 
     * @param bos BufferedOutputStream used to send response to client in html
     *            format
     * @param s   String to be sent to the client
     * @return boolean value to indicate if the response was sent successfully
     */
    private boolean println(BufferedOutputStream bos, String s) {
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

}
