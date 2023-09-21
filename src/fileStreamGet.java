import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class fileStreamGet extends Thread {
    private Path path;
    private long length;
    private String ip;
    private int port;

    public fileStreamGet(Path path, String ip, int port, long length) {
        this.path = path;
        this.length = length;
        this.ip = ip;
        this.port = port;
        this.start();
    }

    @Override
    public void run() {
        Socket client = null;
        InputStream clientIn = null;
        OutputStream fileOut = null;
        try {
            client = new Socket(ip, port);
            clientIn = client.getInputStream();
            fileOut = Files.newOutputStream(path);
            System.out.println("server connected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }
        long bytesRead = 0;
        try {
            while (bytesRead <= length) {

                if (clientIn.available() > 0) {
                    byte[] bytes = clientIn.readAllBytes();
                    bytesRead += bytes.length;
                    System.out.println(bytesRead + "    " + bytes.length);
                    fileOut.write(bytes);
                    fileOut.flush();
                }
            }
            fileOut.close();
            System.out.println("finished");
        } catch (IOException e) {
            System.out.println("closed on error");
        }


    }
}
