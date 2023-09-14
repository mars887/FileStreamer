import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client extends Thread {
    private final int port;
    private final String ip;
    private Scanner in = null;
    private Scanner sin = null;
    private PrintWriter out = null;
    private Socket socket = null;

    public Client(int port, String ip, Scanner sin) {
        this.sin = sin;
        this.port = port;
        this.ip = ip;
        do {
            try {
                if (socket == null) {
                    socket = new Socket(ip, port);
                    System.out.println("socket connected" + socket.toString());
                }

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
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        if (line.startsWith("get")) {
                            System.out.print("getting file, enter saving path - ");
//                            String path = sin.nextLine();
//                            if(!Files.isDirectory(Path.of(path))) {
//                                System.out.println("path error");
//                                out.println("stop");
//                                out.flush();
//                                continue;
//                            }
//                            String[] data = line.split(";");
//                            new fileStreamGet(Path.of(path),data[1],Integer.parseInt(data[2]),Long.parseLong(data[3]));
                        }
                    }
                }
            }
        }.start();
        while (true) {
            String str = sin.nextLine();
            if (str.startsWith("send")) {
                String path = str.substring(5);
                if (!Files.exists(Path.of(path)) || Files.isDirectory(Path.of(path))) {
                    System.out.println("start failed");
                    continue;
                }

                out.println("get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName() + ";" + Path.of(path).toFile().length());
                out.flush();
                new fileStreamSend(Paths.get(path), port + 10);

            }
        }
    }
}
