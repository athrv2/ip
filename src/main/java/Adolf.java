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
        boolean[] isDone = new boolean[100];
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
                printList(tasks, isDone, taskCount);
                continue;
            }

            if (cleaned.startsWith("mark ")) {
                int index = Integer.parseInt(cleaned.split(" ")[1]) - 1;
                isDone[index] = true;
                printMark(true, tasks[index]);
                continue;
            }

            if (cleaned.startsWith("unmark ")) {
                int index = Integer.parseInt(cleaned.split(" ")[1]) - 1;
                isDone[index] = false;
                printMark(false, tasks[index]);
                continue;
            }

            tasks[taskCount] = cleaned;
            isDone[taskCount] = false;
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

    private static void printList(String[] tasks, boolean[] isDone, int count) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < count; i++) {
            String status = isDone[i] ? "[X]" : "[ ]";
            System.out.println(" " + (i + 1) + "." + status + " " + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void printMark(boolean done, String task) {
        System.out.println(LINE);
        if (done) {
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   [X] " + task);
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   [ ] " + task);
        }
        System.out.println(LINE);
    }
}