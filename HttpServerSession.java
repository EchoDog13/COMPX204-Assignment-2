import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class HttpServerSession extends Thread {
    private Socket s;
    private BufferedReader reader;
    private BufferedOutputStream writer;

    public HttpServerSession(Socket s) {
        this.s = s;

    }

    @Override
    public void run() {
        System.out.println("Client connected");
        InetAddress clientAddress = s.getInetAddress();
        String clientIP = clientAddress.getHostAddress();
        System.out.println(clientIP);
        HttpServerRequest request = new HttpServerRequest();
        // request.process(""); // NEED TO GIVE REQUEST
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;

        try {
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new BufferedOutputStream(s.getOutputStream());

            String line;
            while (request.isDone() == false) {
                line = reader.readLine();
                System.out.println(line);
                request.process(line);
                if (line.isEmpty()) {
                    break;
                }

            }
            System.out.println("!!!!!!!!!!!!");
            System.out.println("Request Type: " + request.getRequestType());
            System.out.println("File: " + request.getFile());
            System.out.println("Host: " + request.getHost());

            fis = new FileInputStream(new File(request.getFile()));

            while (fis.available() > 0) {
                int bytesRead = fis.read(buffer);
                writer.write(buffer, 0, bytesRead);
            }
            sendResponse(writer);

            writer.flush();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if (s != null && !s.isClosed()) {
                    // s.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }

        // s.getPort();

    }

    private void sendResponse(BufferedOutputStream bos) {
        try {

            println(bos, "HTTP/1.1 200 OK");
            println(bos, "");
            println(bos, "Hello World");
            bos.flush();
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.getMessage());
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
