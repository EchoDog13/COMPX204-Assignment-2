import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class HttpServerSession extends Thread {
    private Socket s;

    public HttpServerSession(Socket s) {
        this.s = s;

        run();
    }

    public void run() {
        System.out.println("Client connected");
        InetAddress clientAddress = s.getInetAddress();
        String clientIP = clientAddress.getHostAddress();
        System.out.println(clientIP);

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));

            while (reader.readLine() != null) {
                System.out.println(reader.readLine());

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        s.getPort();

        try {

        } catch (Exception e) {
            // TODO: handle exception

        }

    }

}
