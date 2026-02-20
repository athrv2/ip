package adolf;

/**
 * Parses user input commands into structured data.
 */
public class Parser {

    /**
     * Normalizes command input by collapsing multiple spaces and trimming.
     * Handles leading/trailing and multiple spaces where only one is expected.
     */
    public static String normalizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }

    /**
     * Returns true if the input is exactly the command or starts with command and a space.
     */
    public static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    /**
     * Parses the numeric index after the command word (1-based in input, returns 0-based).
     */
    public static Integer parseIndex(String input, String command) {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            return null;
        }
        try {
            return Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Extracts the todo description from "todo &lt;description&gt;" input.
     */
    public static String parseTodoDescription(String input) {
        if (!input.startsWith("todo ")) {
            return null;
        }
        String desc = input.substring(5).trim();
        return desc.isEmpty() ? null : desc;
    }

    /**
     * Parses the keyword part of a {@code find} command.
     *
     * @param input full user input, e.g. {@code "find book"}.
     * @return the keyword to search for, or {@code null} if the command is
     *     not a valid find command or the keyword is empty.
     */
    public static String parseFindKeyword(String input) {
        if (!input.startsWith("find ")) {
            return null;
        }
        String keyword = input.substring(5).trim();
        return keyword.isEmpty() ? null : keyword;
    }

    /**
     * Parses "deadline &lt;desc&gt; /by &lt;date&gt;" into {description, byPart}.
     */
    public static String[] parseDeadline(String input) {
        if (!input.startsWith("deadline ")) {
            return null;
        }

        String rest = input.substring(9).trim();
        if (rest.contains(" /by ") && rest.indexOf(" /by ") != rest.lastIndexOf(" /by ")) {
            return null;
        }
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            return null;
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    /**
     * Parses "event &lt;desc&gt; /from &lt;date&gt; /to &lt;date&gt;" into {description, fromPart, toPart}.
     */
    public static String[] parseEvent(String input) {
        if (!input.startsWith("event ")) {
            return null;
        }

        String rest = input.substring(6).trim();
        if (rest.contains(" /from ") && rest.indexOf(" /from ") != rest.lastIndexOf(" /from ")) {
            return null;
        }
        if (rest.contains(" /to ") && rest.indexOf(" /to ") != rest.lastIndexOf(" /to ")) {
            return null;
        }
        String[] first = rest.split(" /from ", 2);
        if (first.length < 2 || first[0].trim().isEmpty()) {
            return null;
        }

        String[] second = first[1].split(" /to ", 2);
        if (second.length < 2) {
            return null;
        }

        return new String[]{
                first[0].trim(),
                second[0].trim(),
                second[1].trim()
        };
    }
}
