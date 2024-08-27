import java.io.BufferedOutputStream;
import java.io.BufferedReader;
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

        try {

            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            writer = new BufferedOutputStream(s.getOutputStream());

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) {
                    break;
                }

            }
            sendResponse(writer);

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
