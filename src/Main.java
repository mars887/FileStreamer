import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    public static Scanner scan;
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        System.out.print("mode - ");
        String mode = scan.nextLine();
        System.out.print("ip - ");
        String ip = scan.nextLine();

        switch(mode) {
            case"client":
                new Client(1234,ip);
                break;

            case"server":
                //new Server(1234,ip);
                break;
        }
    }
}