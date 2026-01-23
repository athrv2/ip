import java.util.Scanner;

public class Adolf {
    private static final String LINE =
            "____________________________________________________________";
    private static final String LOGO =
            "   ___      ____    ___    _      _____ \\n"
                    + "  / _ \\    |  _ \\  / _ \\  | |    |  ___|\\n"
                    + " | |_| |   | | | || | | | | |    | |__  \\n"
                    + " |  _  |   | | | || | | | | |    |  __| \\n"
                    + " | | | |   | |_| || |_| | | |____| |___ \\n"
                    + " |_| |_|   |____/  \\___/  |______|_____|\\n";

    public static void main(String[] args) {
        printGreeting();

        Scanner sc = new Scanner(System.in);

        while (true) {
            String input = sc.nextLine();

            if (input.equals("bye")) {
                printBox("Bye. Hope to see you again soon!");
                break;
            }

            printBox(input);
        }
    }


    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println("Hello! I'm Adolf");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void printBox(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }
}