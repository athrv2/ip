package adolf;

import java.util.Scanner;

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

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showGreeting() {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println("Hello! I'm Adolf");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showBox(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" OOPS!!! " + message);
        System.out.println(LINE);
    }

    public void showAddedTask(String formattedTask, int totalCount) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("  " + formattedTask);
        System.out.println(" Now you have " + totalCount + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showDeletedTask(String removedTaskLine, int newCount) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("  " + removedTaskLine);
        System.out.println(" Now you have " + newCount + " tasks in the list.");
        System.out.println(LINE);
    }

    public void showTaskListHeader() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
    }

    public void showTaskListItem(int displayIndex, String formattedTask) {
        System.out.println(" " + displayIndex + "." + formattedTask);
    }

    public void showTaskListFooter() {
        System.out.println(LINE);
    }

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
