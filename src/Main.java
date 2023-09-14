import java.util.Scanner;

public class Main {
    public static Scanner scan;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        System.out.print("mode - ");
        String mode = scan.nextLine();
        System.out.print("ip - ");
        String ip = scan.nextLine();

        if (mode.equals("client")) {
            new Client(1234, ip,scan);
        } else if (mode.equals("server")) {
            new Server(1234, ip);
        }

    }
}