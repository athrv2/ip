package adolf;

public class AdolfBot {
    private final Ui ui;
    private final TaskList tasks;
    private final Storage storage;

    public AdolfBot() {
        ui = new Ui(); // we will stop using System.out inside Ui later
        tasks = new TaskList(100);
        storage = new Storage("./data/adolf.txt");

        int loadedCount = storage.load(tasks.types(), tasks.descs(), tasks.dones(),
                tasks.deadlineBy(), tasks.deadlineHasTime(),
                tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime());
        tasks.setSize(loadedCount);
    }

    /**
     * Processes one user input line and returns Adolf's response as a String.
     */
    public String getResponse(String input) {
        String cleaned = input.trim();
    
        if (cleaned.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        }
    
        if (cleaned.equals("list")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                sb.append(i + 1).append(". ").append(formatTask(i)).append("\n");
            }
            return sb.toString().trim();
        }
    
        // MARK
        if (Parser.isCommand(cleaned, "mark")) {
            Integer index = Parser.parseIndex(cleaned, "mark");
            if (index == null) {
                return "OOPS!!! Please provide a valid task number. Usage: mark <number>";
            }
            if (index < 0 || index >= tasks.size()) {
                return "OOPS!!! That task number doesn't exist. Use: list (then mark <number>).";
            }
            tasks.markDone(index, true);
            save();
            return "Nice! I've marked this task as done:\n" + formatTask(index);
        }
    
        // UNMARK
        if (Parser.isCommand(cleaned, "unmark")) {
            Integer index = Parser.parseIndex(cleaned, "unmark");
            if (index == null) {
                return "OOPS!!! Please provide a valid task number. Usage: unmark <number>";
            }
            if (index < 0 || index >= tasks.size()) {
                return "OOPS!!! That task number doesn't exist. Use: list (then unmark <number>).";
            }
            tasks.markDone(index, false);
            save();
            return "OK, I've marked this task as not done yet:\n" + formatTask(index);
        }
    
        // DELETE
        if (Parser.isCommand(cleaned, "delete")) {
            Integer index = Parser.parseIndex(cleaned, "delete");
            if (index == null) {
                return "OOPS!!! Please provide a valid task number. Usage: delete <number>";
            }
            if (index < 0 || index >= tasks.size()) {
                return "OOPS!!! That task number doesn't exist. Use: list (then delete <number>).";
            }
    
            String removed = formatTask(index);
            tasks.delete(index);
            save();
    
            return "Noted. I've removed this task:\n"
                    + removed + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        }
    
        // TODO
        String todoDesc = Parser.parseTodoDescription(cleaned);
        if (todoDesc != null) {
            int addedIndex = tasks.addTodo(todoDesc);
            save();
    
            return "Got it. I've added this task:\n"
                    + formatTask(addedIndex) + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        } else if (Parser.isCommand(cleaned, "todo")) {
            return "OOPS!!! The description of a todo cannot be empty. Usage: todo <description>";
        }
    
        // DEADLINE
        String[] deadlineParts = Parser.parseDeadline(cleaned);
        if (deadlineParts != null) {
            Adolf.ParsedDateTime parsed = Adolf.parseDateOrDateTime(deadlineParts[1]); // see note below
            if (parsed == null) {
                return "OOPS!!! Invalid date format. Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-10-15 or 2019-10-15 1800)";
            }
    
            int addedIndex = tasks.addDeadline(deadlineParts[0], parsed.getValue(), parsed.isHasTime());
    
            return "Got it. I've added this task:\n"
                    + formatTask(addedIndex) + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        } else if (Parser.isCommand(cleaned, "deadline")) {
            return "OOPS!!! Deadline usage: deadline <desc> /by <yyyy-MM-dd> [HHmm]";
        }
    
        // EVENT
        String[] eventParts = Parser.parseEvent(cleaned);
        if (eventParts != null) {
            Adolf.ParsedDateTime parsedFrom = Adolf.parseDateOrDateTime(eventParts[1]);
            Adolf.ParsedDateTime parsedTo = Adolf.parseDateOrDateTime(eventParts[2]);
    
            if (parsedFrom == null || parsedTo == null) {
                return "OOPS!!! Invalid date format. Use: yyyy-MM-dd or yyyy-MM-dd HHmm.";
            }
    
            int addedIndex = tasks.addEvent(eventParts[0],
                parsedFrom.getValue(), parsedFrom.isHasTime(),
                parsedTo.getValue(), parsedTo.isHasTime());
            save();
    
            return "Got it. I've added this task:\n"
                    + formatTask(addedIndex) + "\n"
                    + "Now you have " + tasks.size() + " tasks in the list.";
        } else if (Parser.isCommand(cleaned, "event")) {
            return "OOPS!!! Event usage: event <desc> /from <yyyy-MM-dd> [HHmm] /to <yyyy-MM-dd> [HHmm]";
        }
    
        return "OOPS!!! I'm sorry, I don't know what that means. Try: todo, deadline, event, list, mark, unmark, delete, bye";
    }

    private String formatTask(int index) {
        return Adolf.formatTaskForUi(tasks, index);
    }
    
    private void save() {
        storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                tasks.deadlineBy(), tasks.deadlineHasTime(),
                tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                tasks.size());
    }

    public boolean isExitCommand(String input) {
        return input != null && input.trim().equals("bye");
    }
}