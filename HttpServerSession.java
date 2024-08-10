import java.net.InetAddress;
import java.net.Socket;

public class HttpServerSession extends Thread {
    private Socket s;

    public HttpServerSession(Socket s) {
        this.s = s;

    }

    public void runSession() {
        System.out.println("Client connected");
        InetAddress clientAddress = s.getInetAddress();
        String clientIP = clientAddress.getHostAddress();
        System.out.println(clientIP);
    }

}
