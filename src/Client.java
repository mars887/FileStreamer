import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client extends Thread {
  private final int port;
  private final String ip;
  private Scanner in = null;
  private PrintWriter out = null;
  private Socket socket = null;

  public Client(int port, String ip) {
    this.port = port;
    this.ip = ip;
    do {
      try {
        if (socket == null) socket = new Socket(ip, port);

        in = new Scanner(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());
      } catch (IOException e) {
        try {
          Thread.sleep(200);
        } catch (InterruptedException ex) {
        }
      }
    } while (socket == null || in == null || out == null);
    this.start();
  }

  @Override
  public void run() {
    while (true) {
      String str = Main.scan.nextLine();
      if (str.startsWith("send")) {
        String path = str.substring(5);

        out.println(ip + ";" + (port + 10));
        out.flush();
        new fileStreamSend(Paths.get(path), port + 10);
      } else if (str.startsWith("get")) {

      }
    }
  }
}
