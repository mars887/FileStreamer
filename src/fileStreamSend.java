import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class fileStreamSend extends Thread {
    private Path path;
    private int port;

    public fileStreamSend(Path file, int port) {
        this.port = port;
        this.path = file;
        this.start();
    }

    @Override
    public void run() {
        ServerSocket server = null;
        Socket client = null;
        OutputStream clientOut = null;
        InputStream fileIn = null;
        try {
            server = new ServerSocket(port);
            client = server.accept();
            clientOut = client.getOutputStream();
            fileIn = Files.newInputStream(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        try {
            byte mass = new byte[65536];
            int c = 0;
            while((c = fileIn.read(mass)) > 0) {
                clientOut.write(mass,c);
                clientOut.flush();
            }
        } catch (IOException e) {}
    }
}
