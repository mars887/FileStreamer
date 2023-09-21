import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Scanner;

public class User extends Thread {
    private final String ip;
    private final int port;
    private final Socket client;
    private final BufferedReader userInput;
    private final BufferedReader clientInput;
    private final InputStream clientInputStream;
    private final PrintWriter clientOutput;


    public User(String ip, int port, Socket client, InputStream clientInputStream, PrintWriter clientOutput) {
        this.ip = ip;
        this.port = port;
        this.client = client;
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        this.clientInputStream = clientInputStream;
        clientInput = new BufferedReader(new InputStreamReader(clientInputStream));
        this.clientOutput = clientOutput;
        this.start();
        System.out.println("user started");
    }

    @Override
    public void run() {
        System.out.println("run started");
        while (true) {
            try {
                if (System.in.available() > 0) {
                    String input = userInput.readLine();
                    if (input.startsWith("send")) {
                        String path = input.substring(5);
                        if (!Files.exists(Path.of(path)) || Files.isDirectory(Path.of(path))) {
                            System.out.println("start failed - " + path);
                            continue;
                        }
                        clientOutput.println("get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName() + ";" + Path.of(path).toFile().length() + "\n");
                        clientOutput.flush();
                        System.out.println("sendedToClient - " + "get;" + ip + ";" + (port + 10) + ";" + Path.of(path).getFileName() + ";" + Path.of(path).toFile().length());
                        new fileStreamSend(Paths.get(path), port + 10);
                    }
                }
            } catch (IOException e) {
                System.out.println("userInputStream available failed");
            }
            try {
                int clsa = clientInputStream.available();
                if (clsa > 0) {
                    Thread.sleep(100);
                    String input = clientInput.readLine();
                    System.out.println("clientInput - " + input);
                    if (input.startsWith("get")) {
                        boolean pathIntered = false;
                        String path;
                        do {
                            System.out.print("getting file, enter saving path - ");

                            path = userInput.readLine();
                            if (Files.isDirectory(Path.of(path))) {
                                System.out.println("start failed - " + path);
                            } else if (!Files.exists(Path.of(path))) {
                                pathIntered = true;
                            }
                        } while (!pathIntered);

                        String[] data = input.split(";");
                        System.out.println(Arrays.toString(data));
                        new fileStreamGet(Path.of(path), data[1], Integer.parseInt(data[2]), Long.parseLong(data[4]));
                    }
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("thread interrupted");
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("clientInputStream available failed");
            }
        }

    }
}
