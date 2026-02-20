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
        System.out.println("Hey! I'm Adolf, your task buddy.");
        System.out.println("What would you like to do?");
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
        System.out.println(" Oops — " + message);
        System.out.println(LINE);
    }

    /** Prints confirmation that a task was added. */
    public void showAddedTask(String formattedTask, int totalCount) {
        System.out.println(LINE);
        System.out.println(" Done! I've added this task:");
        System.out.println("  " + formattedTask);
        System.out.println(" You now have " + totalCount + " task(s) in the list.");
        System.out.println(LINE);
    }

    /** Prints confirmation that a task was removed. */
    public void showDeletedTask(String removedTaskLine, int newCount) {
        System.out.println(LINE);
        System.out.println(" Removed as requested:");
        System.out.println("  " + removedTaskLine);
        System.out.println(" You now have " + newCount + " task(s) in the list.");
        System.out.println(LINE);
    }

    /** Prints the header for the task list. */
    public void showTaskListHeader() {
        System.out.println(LINE);
        System.out.println(" Here's your list:");
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
            System.out.println(" Marked as done:");
        } else {
            System.out.println(" Marked as not done yet:");
        }
        System.out.println("  " + fullTaskLine);
        System.out.println(LINE);
    }
}
