import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final int port;
    private final String ip;
    private InputStream in = null;
    private PrintWriter out = null;
    private ServerSocket serverSocket = null;
    private Socket client = null;

    public Server(int port, String ip) {
        this.port = port;
        this.ip = ip;
        try {
            serverSocket = new ServerSocket(port);
            client = serverSocket.accept();
            System.out.println("client accepted" + client.toString());
            in = client.getInputStream();
            out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("error on creating server");
        }
        new User(ip,port,client,in,out);
    }

}
