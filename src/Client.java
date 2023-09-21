import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private final int port;
    private final String ip;
    private InputStream in = null;
    private PrintWriter out = null;
    private Socket socket = null;

    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
        do {
            try {
                if (socket == null) {
                    socket = new Socket(ip, port);
                    System.out.println("socket connected" + socket);
                }

                in = socket.getInputStream();
                out = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                }
            }
        } while (socket == null || in == null || out == null);
        new User(ip,port,socket,in,out);
    }

}
