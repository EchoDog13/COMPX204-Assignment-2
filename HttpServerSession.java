import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServerSession extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private BufferedOutputStream writer;
    private HttpServerRequest request;

    public HttpServerSession(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("Client connected");
        InetAddress clientAddress = socket.getInetAddress();
        String clientIP = clientAddress.getHostAddress();
        System.out.println(clientIP);

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedOutputStream(socket.getOutputStream());

            request = new HttpServerRequest();

            // Process the request
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
                request.process(line);
            }

            // Send the response after processing the request
            sendResponse(writer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private void sendResponse(BufferedOutputStream bos) throws InterruptedException {
        FileInputStream fis = null;
        try {
            // Determine the file path based on the request
            String hostPath = request.getHost();

            int colonIndex = hostPath.indexOf(':');

            // Extract the substring before the colon
            String prePortHost = hostPath.substring(0, colonIndex);

            String path = prePortHost + "/" + request.getFile();
            // path = "localhost/index.html";

            File file = new File(path);

            System.out.println("print path " + path);

            System.out.println("file exists" + file.exists());
            if (file.exists() && file.isFile()) {
                // Send HTTP headers
                println(bos, "HTTP/1.1 200 OK");
                // println(bos, "Content-Type: text/html; charset=UTF-8");
                println(bos, "Content-Length: " + file.length());
                println(bos, ""); // Blank line to separate headers from the body

                // Send the file content

                synchronized (this) {

                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        bos.write(buffer, 0, bytesRead);
                        // Thread.sleep(800);
                    }
                }
                System.out.println("File found at path: " + path);
            } else {
                // Handle file not found case
                println(bos, "HTTP/1.1 404 Not Found");
                println(bos, "");
                println(bos, "404: File Not Found");
                System.out.println("File not found at path: " + path);
            }

            bos.flush();
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
        }

        finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                System.out.println("Error closing FileInputStream: " + e.getMessage());
            }
        }
    }

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

    /**
     * InnerHttpServerSession
     */
    public class HttpSession {
        private ArrayList<HttpServerSession> sessions = new ArrayList<HttpServerSession>();

    }

    public static void main(String[] args) {
        // TODO: Server setup and connection handling code goes here
    }
}
