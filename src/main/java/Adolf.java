import java.util.Scanner;

public class Adolf {
    private static final String LINE =
            "____________________________________________________________";

    private static final String LOGO =
            "   ___      ____    ___    _      _____ \n"
                    + "  / _ \\    |  _ \\  / _ \\  | |    |  ___|\n"
                    + " | |_| |   | | | || | | | | |    | |__  \n"
                    + " |  _  |   | | | || | | | | |    |  __| \n"
                    + " | | | |   | |_| || |_| | | |____| |___ \n"
                    + " |_| |_|   |____/  \\___/  |______|_____|\n";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] tasks = new String[100];
        int taskCount = 0;

        printGreeting();

        while (true) {
            String input = sc.nextLine();
            String cleaned = input.trim();

            if (cleaned.equals("bye")) {
                printBox("Bye. Hope to see you again soon!");
                break;
            }

            if (cleaned.equals("list")) {
                printList(tasks, taskCount);
                continue;
            }

            tasks[taskCount] = cleaned;
            taskCount++;

            printBox("added: " + cleaned);
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

    private static void printList(String[] tasks, int count) {
        System.out.println(LINE);
        for (int i = 0; i < count; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        System.out.println(LINE);
    }
}