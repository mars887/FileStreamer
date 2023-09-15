import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

public class Server {
    private final int port;
    private final String ip;
    private Scanner in = null;
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
            in = new Scanner(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }
        }
        new User(ip,port,client,Main.scan,in,out);
    }

}
