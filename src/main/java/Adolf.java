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


        char[] type = new char[100];
        String[] desc = new String[100];
        String[] by = new String[100];
        String[] from = new String[100];
        String[] to = new String[100];
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
                printList(type, desc, by, from, to, isDone, taskCount);
                continue;
            }

            if (cleaned.startsWith("mark ")) {
                int index = Integer.parseInt(cleaned.split(" ")[1]) - 1;
                isDone[index] = true;
                printMark(true, formatTask(type, desc, by, from, to, isDone, index));
                continue;
            }

            if (cleaned.startsWith("unmark ")) {
                int index = Integer.parseInt(cleaned.split(" ")[1]) - 1;
                isDone[index] = false;
                printMark(false, formatTask(type, desc, by, from, to, isDone, index));
                continue;
            }


            if (cleaned.startsWith("todo ")) {
                type[taskCount] = 'T';
                desc[taskCount] = cleaned.substring(5).trim();
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            if (cleaned.startsWith("deadline ")) {
                // Format: deadline <desc> /by <by>
                String rest = cleaned.substring(9).trim();
                String[] parts = rest.split(" /by ", 2);

                type[taskCount] = 'D';
                desc[taskCount] = parts[0].trim();
                by[taskCount] = (parts.length == 2) ? parts[1].trim() : "";
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            if (cleaned.startsWith("event ")) {
                String rest = cleaned.substring(6).trim();
                String[] firstSplit = rest.split(" /from ", 2);

                String eventDesc = firstSplit[0].trim();
                String fromToPart = (firstSplit.length == 2) ? firstSplit[1] : "";
                String[] secondSplit = fromToPart.split(" /to ", 2);

                type[taskCount] = 'E';
                desc[taskCount] = eventDesc;
                from[taskCount] = (secondSplit.length >= 1) ? secondSplit[0].trim() : "";
                to[taskCount] = (secondSplit.length == 2) ? secondSplit[1].trim() : "";
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            type[taskCount] = 'T';
            desc[taskCount] = cleaned;
            isDone[taskCount] = false;
            taskCount++;
            printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
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

    private static void printAdd(char[] type, String[] desc, String[] by, String[] from, String[] to,
                                 boolean[] isDone, int addedIndex, int totalCount) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + formatTask(type, desc, by, from, to, isDone, addedIndex));
        System.out.println(" Now you have " + totalCount + " tasks in the list.");
        System.out.println(LINE);
    }

    private static void printList(char[] type, String[] desc, String[] by, String[] from, String[] to,
                                  boolean[] isDone, int count) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < count; i++) {
            System.out.println(" " + (i + 1) + "." + formatTask(type, desc, by, from, to, isDone, i));
        }
        System.out.println(LINE);
    }

    private static void printMark(boolean done, String fullTaskLine) {
        System.out.println(LINE);
        if (done) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + fullTaskLine);
        System.out.println(LINE);
    }

    private static String formatTask(char[] type, String[] desc, String[] by, String[] from, String[] to,
                                     boolean[] isDone, int index) {
        String status = isDone[index] ? "[X]" : "[ ]";
        char t = type[index];

        if (t == 'T') {
            return "[" + t + "]" + status + " " + desc[index];
        }

        if (t == 'D') {
            return "[" + t + "]" + status + " " + desc[index] + " (by: " + by[index] + ")";
        }

        // Event
        return "[" + t + "]" + status + " " + desc[index]
                + " (from: " + from[index] + " to: " + to[index] + ")";
    }
}