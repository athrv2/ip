public class Parser {

    public static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

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

    public static String parseTodoDescription(String input) {
        if (!input.startsWith("todo ")) {
            return null;
        }
        String desc = input.substring(5).trim();
        return desc.isEmpty() ? null : desc;
    }

    public static String[] parseDeadline(String input) {
        // returns {description, byPart} or null
        if (!input.startsWith("deadline ")) return null;

        String rest = input.substring(9).trim();
        String[] parts = rest.split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            return null;
        }
        return new String[]{parts[0].trim(), parts[1].trim()};
    }

    public static String[] parseEvent(String input) {
        // returns {description, fromPart, toPart} or null
        if (!input.startsWith("event ")) return null;

        String rest = input.substring(6).trim();
        String[] first = rest.split(" /from ", 2);
        if (first.length < 2 || first[0].trim().isEmpty()) return null;

        String[] second = first[1].split(" /to ", 2);
        if (second.length < 2) return null;

        return new String[]{
                first[0].trim(),
                second[0].trim(),
                second[1].trim()
        };
    }
}