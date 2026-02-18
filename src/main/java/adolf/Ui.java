package adolf;

import java.util.Scanner;

/**
 * Handles all user interface interactions.
 */
public class Ui {
    private static final String LINE =
            "____________________________________________________________";

    private static final String LOGO =
            "   ___      ____    ___    _      _____ \n"
                    + "  / _ \\    |  _ \\  / _ \\  | |    |  ___|\n"
                    + " | |_| |   | | | || | | | | |    | |__  \n"
                    + " |  _  |   | | | || | | | | |    |  __| \n"
                    + " | | | |   | |_| || |_| | | |____| |___ \n"
                    + " |_| |_|   |____/  \\___/  |______|_____|";

    private final Scanner scanner = new Scanner(System.in);

    /** Reads one line of user input. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Prints the greeting and logo. */
    public void showGreeting() {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println("Hello! I'm Adolf");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /** Prints a horizontal line. */
    public void showLine() {
        System.out.println(LINE);
    }

    /** Prints a message inside a box. */
    public void showBox(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }

    /** Prints an error message in a box. */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + message);
        System.out.println(LINE);
    }

    /** Prints confirmation that a task was added. */
    public void showAddedTask(String formattedTask, int totalCount) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + formattedTask);
        System.out.println(" Now you have " + totalCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /** Prints confirmation that a task was removed. */
    public void showDeletedTask(String removedTaskLine, int newCount) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("  " + removedTaskLine);
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        System.out.println(LINE);
    }

    /** Prints the header for the task list. */
    public void showTaskListHeader() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
    }

    /** Prints one task line with its display index. */
    public void showTaskListItem(int displayIndex, String formattedTask) {
        System.out.println(" " + displayIndex + "." + formattedTask);
    }

    /** Prints the footer after the task list. */
    public void showTaskListFooter() {
        System.out.println(LINE);
    }

    /** Prints confirmation that a task was marked done or not done. */
    public void showMarked(boolean done, String fullTaskLine) {
        System.out.println(LINE);
        if (done) {
            System.out.println(" Nice! I've marked this task as done:");
        } else {
            System.out.println(" OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + fullTaskLine);
        System.out.println(LINE);
    }
}
