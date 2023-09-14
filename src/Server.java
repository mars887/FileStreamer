import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

public class Server extends Thread {
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

        this.start();
    }

    @Override
    public void run() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if(in.hasNextLine()) {
                        String line = in.nextLine();
                        if(line.startsWith("get")) {
                            System.out.print("getting file, enter saving path - ");
                            String path = Main.scan.nextLine();
                            if(!Files.isDirectory(Path.of(path))) {
                                System.out.println("path error");
                                out.println("stop");
                                out.flush();
                                continue;
                            }
                            String[] data = line.split(";");
                            new fileStreamGet(Path.of(path),data[1],Integer.parseInt(data[2]),Long.parseLong(data[3]));
                        }
                    }
                }
            }
        }.start();
        while (true) {
            String str = Main.scan.nextLine();
            if (str.startsWith("send")) {
                String path = str.substring(5);
                if (!Files.exists(Path.of(path)) || Files.isDirectory(Path.of(path))) {
                    System.out.println("start failed");
                    continue;
                }
                out.println("get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName());
                out.flush();
                new fileStreamSend(Paths.get(path), port + 10);

            }
        }
    }
}
