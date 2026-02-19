package adolf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Entry point of the Adolf task management chatbot.
 * Handles user interaction and command execution.
 */
public class Adolf {

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HHmm");


    /**
     * Runs the Adolf chatbot.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        Ui ui = new Ui();

        TaskList tasks = new TaskList(100);

        Storage storage = new Storage("./data/adolf.txt");
        int loadedCount = storage.load(tasks.types(), tasks.descs(), tasks.dones(),
                tasks.deadlineBy(), tasks.deadlineHasTime(),
                tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime());
        tasks.setSize(loadedCount);

        ui.showGreeting();

        while (true) {
            String input = ui.readCommand();
            String cleaned = input.trim();

            if (cleaned.equals("bye")) {
                ui.showBox("Bye. Hope to see you again soon!");
                break;
            }

            if (cleaned.equals("list")) {
                ui.showTaskListHeader();
                for (int i = 0; i < tasks.size(); i++) {
                    ui.showTaskListItem(i + 1, formatTask(
                            tasks.types(), tasks.descs(), tasks.dones(),
                            tasks.deadlineBy(), tasks.deadlineHasTime(),
                            tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                            i));
                }
                ui.showTaskListFooter();
                continue;
            }

            if (Parser.isCommand(cleaned, "mark")) {
                Integer index = Parser.parseIndex(cleaned, "mark");
                if (index == null) {
                    ui.showError("Please provide a valid task number. Usage: mark <number>");
                    continue;
                }

                if (index < 0 || index >= tasks.size()) {
                    ui.showError("That task number doesn't exist. Use: list (then mark <number>).");
                    continue;
                }

                tasks.markDone(index, true);

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                ui.showMarked(true, formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        index));
                continue;
            }

            if (Parser.isCommand(cleaned, "unmark")) {
                Integer index = Parser.parseIndex(cleaned, "unmark");
                if (index == null) {
                    ui.showError("Please provide a valid task number. Usage: unmark <number>");
                    continue;
                }

                if (index < 0 || index >= tasks.size()) {
                    ui.showError("That task number doesn't exist. Use: list (then unmark <number>).");
                    continue;
                }

                tasks.markDone(index, false);

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                ui.showMarked(false, formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        index));
                continue;
            }

            // delete / todo / deadline / event remain in Adolf for now (still using arrays via tasks getters)

            if (Parser.isCommand(cleaned, "delete")) {
                Integer index = Parser.parseIndex(cleaned, "delete");
                if (index == null) {
                    ui.showError("Please provide a valid task number. Usage: delete <number>");
                    continue;
                }

                if (index < 0 || index >= tasks.size()) {
                    ui.showError("That task number doesn't exist. Use: list (then delete <number>).");
                    continue;
                }

                String removed = formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        index);

                tasks.delete(index);

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                ui.showDeletedTask(removed, tasks.size());
                continue;
            }

            if (Parser.isCommand(cleaned, "find")) {
                String keyword = Parser.parseFindKeyword(cleaned);
                if (keyword == null) {
                    ui.showError("Find usage: find <keyword>");
                    continue;
                }

                ui.showTaskListHeader();
                int displayed = 0;
                for (int i = 0; i < tasks.size(); i++) {
                    String description = tasks.descs()[i];
                    if (description != null && description.contains(keyword)) {
                        ui.showTaskListItem(displayed + 1, formatTask(
                                tasks.types(), tasks.descs(), tasks.dones(),
                                tasks.deadlineBy(), tasks.deadlineHasTime(),
                                tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                                i));
                        displayed++;
                    }
                }
                if (displayed == 0) {
                    System.out.println(" No matching tasks found.");
                }
                ui.showTaskListFooter();
                continue;
            }

            if (cleaned.equals("todo")) {
                ui.showError("The description of a todo cannot be empty. Usage: todo <description>");
                continue;
            }

            String todoDesc = Parser.parseTodoDescription(cleaned);
            if (todoDesc != null) {
                int addedIndex = tasks.addTodo(todoDesc);

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                String formatted = formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        addedIndex);

                ui.showAddedTask(formatted, tasks.size());
                continue;
            } else if (Parser.isCommand(cleaned, "todo")) {
                ui.showError("The description of a todo cannot be empty. Usage: todo <description>");
                continue;
            }

            if (cleaned.equals("deadline")) {
                ui.showError("Deadline needs a description and /by. Usage: deadline <desc> /by <yyyy-MM-dd> [HHmm]");
                continue;
            }

            String[] deadlineParts = Parser.parseDeadline(cleaned);
            if (deadlineParts != null) {
                ParsedDateTime parsed = parseDateOrDateTime(deadlineParts[1]);
                if (parsed == null) {
                    ui.showError("Invalid date format. Use: yyyy-MM-dd or yyyy-MM-dd HHmm "
                            + "(e.g. 2019-10-15 or 2019-10-15 1800)");
                    continue;
                }

                int addedIndex = tasks.addDeadline(
                    deadlineParts[0],
                    parsed.getValue(),
                    parsed.isHasTime()
                );

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                String formatted = formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        addedIndex);

                ui.showAddedTask(formatted, tasks.size());
                continue;
            } else if (Parser.isCommand(cleaned, "deadline")) {
                ui.showError("Deadline usage: deadline <desc> /by <yyyy-MM-dd> [HHmm]");
                continue;
            }

            if (cleaned.equals("event")) {
                ui.showError("Event needs /from and /to. Usage: event <desc> /from <yyyy-MM-dd> [HHmm] "
                        + "/to <yyyy-MM-dd> [HHmm]");
                continue;
            }

            String[] eventParts = Parser.parseEvent(cleaned);
            if (eventParts != null) {
                ParsedDateTime parsedFrom = parseDateOrDateTime(eventParts[1]);
                ParsedDateTime parsedTo = parseDateOrDateTime(eventParts[2]);

                if (parsedFrom == null || parsedTo == null) {
                    ui.showError("Invalid date format. Use: yyyy-MM-dd or yyyy-MM-dd HHmm.");
                    continue;
                }

                int addedIndex = tasks.addEvent(
                    eventParts[0],
                    parsedFrom.getValue(), parsedFrom.isHasTime(),
                    parsedTo.getValue(), parsedTo.isHasTime()
                );

                storage.save(tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        tasks.size());

                String formatted = formatTask(
                        tasks.types(), tasks.descs(), tasks.dones(),
                        tasks.deadlineBy(), tasks.deadlineHasTime(),
                        tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                        addedIndex);

                ui.showAddedTask(formatted, tasks.size());
                continue;
            } else if (Parser.isCommand(cleaned, "event")) {
                ui.showError("Event usage: event <desc> /from <date> /to <date>");
                continue;
            }

            ui.showError("I'm sorry, I don't know what that means. Try: todo, deadline, event, list, "
                    + "mark, unmark, delete, bye");
        }
    }

    public static class ParsedDateTime {
        private final LocalDateTime value;
        private final boolean hasTime;

        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }

        public LocalDateTime getValue() {
            return value;
        }
        
        public boolean isHasTime() {
            return hasTime;
        }
    }

    public static ParsedDateTime parseDateOrDateTime(String s) {
        try {
            LocalDateTime dt = LocalDateTime.parse(s, INPUT_DATE_TIME);
            return new ParsedDateTime(dt, true);
        } catch (DateTimeParseException ignored) {
            // not in datetime format, try date-only
        }

        try {
            LocalDate d = LocalDate.parse(s, INPUT_DATE);
            return new ParsedDateTime(d.atStartOfDay(), false);
        } catch (DateTimeParseException ignored) {
            // invalid format
        }

        return null;
    }

    private static String formatTask(char[] type, String[] desc, boolean[] isDone,
                                     LocalDateTime[] deadlineBy, boolean[] deadlineHasTime,
                                     LocalDateTime[] eventFrom, LocalDateTime[] eventTo,
                                     boolean[] eventFromHasTime, boolean[] eventToHasTime,
                                     int index) {

        String status = isDone[index] ? "[X]" : "[ ]";
        char t = type[index];

        if (t == 'T') {
            return "[" + t + "]" + status + " " + desc[index];
        }

        if (t == 'D') {
            String pretty = prettyPrint(deadlineBy[index], deadlineHasTime[index]);
            return "[" + t + "]" + status + " " + desc[index] + " (by: " + pretty + ")";
        }

        String prettyFrom = prettyPrint(eventFrom[index], eventFromHasTime[index]);
        String prettyTo = prettyPrint(eventTo[index], eventToHasTime[index]);

        return "[" + t + "]" + status + " " + desc[index]
                + " (from: " + prettyFrom + " to: " + prettyTo + ")";
    }

    private static String prettyPrint(LocalDateTime dt, boolean hasTime) {
        if (dt == null) {
            return "";
        }
        String datePart = dt.toLocalDate().format(OUTPUT_DATE);
        if (!hasTime) {
            return datePart;
        }
        return datePart + " " + dt.toLocalTime().format(OUTPUT_TIME);
    }

    public static String formatTaskForUi(TaskList tasks, int index) {
        return formatTask(
                tasks.types(), tasks.descs(), tasks.dones(),
                tasks.deadlineBy(), tasks.deadlineHasTime(),
                tasks.eventFrom(), tasks.eventTo(), tasks.eventFromHasTime(), tasks.eventToHasTime(),
                index
        );
    }
}
