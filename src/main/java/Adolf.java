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
                    + " |_| |_|   |____/  \\___/  |______|_____|";

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

            if (cleaned.equals("mark") || cleaned.startsWith("mark ")) {
                Integer index = parseIndex(cleaned, "mark");
                if (index == null) {
                    continue;
                }
                if (index < 0 || index >= taskCount) {
                    printError("That task number doesn't exist. Use: list (then mark <number>).");
                    continue;
                }
                isDone[index] = true;
                printMark(true, formatTask(type, desc, by, from, to, isDone, index));
                continue;
            }

            if (cleaned.equals("unmark") || cleaned.startsWith("unmark ")) {
                Integer index = parseIndex(cleaned, "unmark");
                if (index == null) {
                    continue;
                }
                if (index < 0 || index >= taskCount) {
                    printError("That task number doesn't exist. Use: list (then unmark <number>).");
                    continue;
                }
                isDone[index] = false;
                printMark(false, formatTask(type, desc, by, from, to, isDone, index));
                continue;
            }

            if (cleaned.equals("delete") || cleaned.startsWith("delete ")) {
                Integer index = parseIndex(cleaned, "delete");
                if (index == null) {
                    continue;
                }
                if (index < 0 || index >= taskCount) {
                    printError("That task number doesn't exist. Use: list (then delete <number>).");
                    continue;
                }

                String removed = formatTask(type, desc, by, from, to, isDone, index);

                for (int i = index; i < taskCount - 1; i++) {
                    type[i] = type[i + 1];
                    desc[i] = desc[i + 1];
                    by[i] = by[i + 1];
                    from[i] = from[i + 1];
                    to[i] = to[i + 1];
                    isDone[i] = isDone[i + 1];
                }

                type[taskCount - 1] = '\0';
                desc[taskCount - 1] = null;
                by[taskCount - 1] = null;
                from[taskCount - 1] = null;
                to[taskCount - 1] = null;
                isDone[taskCount - 1] = false;

                taskCount--;

                printDelete(removed, taskCount);
                continue;
            }

            if (cleaned.equals("todo")) {
                printError("The description of a todo cannot be empty. Usage: todo <description>");
                continue;
            }
            if (cleaned.startsWith("todo ")) {
                String todoDesc = cleaned.substring(5).trim();
                if (todoDesc.isEmpty()) {
                    printError("The description of a todo cannot be empty. Usage: todo <description>");
                    continue;
                }

                type[taskCount] = 'T';
                desc[taskCount] = todoDesc;
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            if (cleaned.equals("deadline")) {
                printError("Deadline needs a description and /by. Usage: deadline <desc> /by <when>");
                continue;
            }
            if (cleaned.startsWith("deadline ")) {
                String rest = cleaned.substring(9).trim();
                String[] parts = rest.split(" /by ", 2);

                if (parts[0].trim().isEmpty()) {
                    printError("Deadline description cannot be empty. Usage: deadline <desc> /by <when>");
                    continue;
                }
                if (parts.length < 2 || parts[1].trim().isEmpty()) {
                    printError("Deadline must include /by <when>. Usage: deadline <desc> /by <when>");
                    continue;
                }

                type[taskCount] = 'D';
                desc[taskCount] = parts[0].trim();
                by[taskCount] = parts[1].trim();
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            if (cleaned.equals("event")) {
                printError("Event needs /from and /to. Usage: event <desc> /from <start> /to <end>");
                continue;
            }
            if (cleaned.startsWith("event ")) {
                String rest = cleaned.substring(6).trim();
                String[] firstSplit = rest.split(" /from ", 2);

                if (firstSplit[0].trim().isEmpty()) {
                    printError("Event description cannot be empty. Usage: event <desc> /from <start> /to <end>");
                    continue;
                }
                if (firstSplit.length < 2) {
                    printError("Event must include /from <start> /to <end>. Usage: event <desc> /from <start> /to <end>");
                    continue;
                }

                String eventDesc = firstSplit[0].trim();
                String fromToPart = firstSplit[1];

                String[] secondSplit = fromToPart.split(" /to ", 2);
                if (secondSplit.length < 2) {
                    printError("Event must include /to <end>. Usage: event <desc> /from <start> /to <end>");
                    continue;
                }

                String eventFrom = secondSplit[0].trim();
                String eventTo = secondSplit[1].trim();

                if (eventFrom.isEmpty()) {
                    printError("Event /from time cannot be empty. Usage: event <desc> /from <start> /to <end>");
                    continue;
                }
                if (eventTo.isEmpty()) {
                    printError("Event /to time cannot be empty. Usage: event <desc> /from <start> /to <end>");
                    continue;
                }

                type[taskCount] = 'E';
                desc[taskCount] = eventDesc;
                from[taskCount] = eventFrom;
                to[taskCount] = eventTo;
                isDone[taskCount] = false;
                taskCount++;

                printAdd(type, desc, by, from, to, isDone, taskCount - 1, taskCount);
                continue;
            }

            printError("I'm sorry, I don't know what that means. Try: todo, deadline, event, list, mark, unmark, delete, bye");
        }
    }

    private static Integer parseIndex(String cleaned, String command) {
        String[] parts = cleaned.split(" ");
        if (parts.length < 2) {
            printError("Please provide a task number. Usage: " + command + " <number>");
            return null;
        }
        try {
            int number = Integer.parseInt(parts[1]);
            return number - 1;
        } catch (NumberFormatException e) {
            printError("Task number must be an integer. Usage: " + command + " <number>");
            return null;
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

    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + message);
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

    private static void printDelete(String removedTaskLine, int newCount) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("  " + removedTaskLine);
        System.out.println(" Now you have " + newCount + " tasks in the list.");
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

        return "[" + t + "]" + status + " " + desc[index]
                + " (from: " + from[index] + " to: " + to[index] + ")";
    }
}