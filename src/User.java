import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.*;
import java.util.Scanner;

public class User extends Thread {
    private final String ip;
    private final int port;
    private final Socket client;
    private final Scanner userInput;
    private final Scanner clientInput;
    private final PrintWriter clientOutput;


    public User(String ip, int port, Socket client, Scanner userInput, Scanner clientIntput, PrintWriter clientOutput) {
        this.ip = ip;
        this.port = port;
        this.client = client;
        this.userInput = userInput;
        this.clientInput = clientIntput;
        this.clientOutput = clientOutput;
        this.start();
        System.out.println("user started");
    }

    @Override
    public void run() {
        System.out.println("run started");
        while (true) {
            if (userInput.hasNextLine()) {
                String input = userInput.nextLine();
                if (input.startsWith("send")) {
                    String path = input.substring(5);
                    if (!Files.exists(Path.of(path)) || Files.isDirectory(Path.of(path))) {
                        System.out.println("start failed - " + path);
                        continue;
                    }
                    clientOutput.println("get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName() + ";" + Path.of(path).toFile().length());
                    clientOutput.flush();
                    System.out.println("sendedToClient - " + "get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName() + ";" + Path.of(path).toFile().length());
                    new fileStreamSend(Paths.get(path), port + 10);
                }
            }
            if (clientInput.hasNextLine()) {
                String input = clientInput.nextLine();
                System.out.println("clientInput - " + input);
                if (input.startsWith("get")) {
                    boolean pathIntered = false;
                    String path;
                    do {
                        System.out.print("getting file, enter saving path - ");

                        path = userInput.nextLine();
                        if (!Files.isDirectory(Path.of(path))) {
                            System.out.println("start failed - " + path);
                        } else if (Files.exists(Path.of(path))) {
                            pathIntered = true;
                        }
                    } while (!pathIntered);

                    String[] data = input.split(";");
                    new fileStreamGet(Path.of(path), data[1], Integer.parseInt(data[2]), Long.parseLong(data[3]));
                }
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    System.out.println("thread interrupted");
                }
            }
        }

    }
}
