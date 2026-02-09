import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Adolf {

    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_DATE = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter OUTPUT_TIME = DateTimeFormatter.ofPattern("HHmm");

    public static void main(String[] args) {
        Ui ui = new Ui();

        char[] type = new char[100];
        String[] desc = new String[100];
        boolean[] isDone = new boolean[100];

        LocalDateTime[] deadlineBy = new LocalDateTime[100];
        boolean[] deadlineHasTime = new boolean[100];

        LocalDateTime[] eventFrom = new LocalDateTime[100];
        LocalDateTime[] eventTo = new LocalDateTime[100];
        boolean[] eventFromHasTime = new boolean[100];
        boolean[] eventToHasTime = new boolean[100];

        Storage storage = new Storage("./data/adolf.txt");
        int taskCount = storage.load(type, desc, isDone,
                deadlineBy, deadlineHasTime,
                eventFrom, eventTo, eventFromHasTime, eventToHasTime);

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
                for (int i = 0; i < taskCount; i++) {
                    ui.showTaskListItem(i + 1, formatTask(type, desc, isDone,
                            deadlineBy, deadlineHasTime,
                            eventFrom, eventTo, eventFromHasTime, eventToHasTime,
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

                if (index < 0 || index >= taskCount) {
                    ui.showError("That task number doesn't exist. Use: list (then mark <number>).");
                    continue;
                }

                isDone[index] = true;
                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                ui.showMarked(true, formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        index));
                continue;
            }

            if (Parser.isCommand(cleaned, "unmark")) {
                Integer index = Parser.parseIndex(cleaned, "unmark");
                if (index == null) {
                    ui.showError("Please provide a valid task number. Usage: unmark <number>");
                    continue;
                }

                if (index < 0 || index >= taskCount) {
                    ui.showError("That task number doesn't exist. Use: list (then unmark <number>).");
                    continue;
                }

                isDone[index] = false;
                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                ui.showMarked(false, formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        index));
                continue;
            }

            if (Parser.isCommand(cleaned, "delete")) {
                Integer index = Parser.parseIndex(cleaned, "delete");
                if (index == null) {
                    ui.showError("Please provide a valid task number. Usage: delete <number>");
                    continue;
                }

                if (index < 0 || index >= taskCount) {
                    ui.showError("That task number doesn't exist. Use: list (then delete <number>).");
                    continue;
                }

                String removed = formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        index);

                for (int i = index; i < taskCount - 1; i++) {
                    type[i] = type[i + 1];
                    desc[i] = desc[i + 1];
                    isDone[i] = isDone[i + 1];

                    deadlineBy[i] = deadlineBy[i + 1];
                    deadlineHasTime[i] = deadlineHasTime[i + 1];

                    eventFrom[i] = eventFrom[i + 1];
                    eventTo[i] = eventTo[i + 1];
                    eventFromHasTime[i] = eventFromHasTime[i + 1];
                    eventToHasTime[i] = eventToHasTime[i + 1];
                }

                int last = taskCount - 1;
                type[last] = '\0';
                desc[last] = null;
                isDone[last] = false;

                deadlineBy[last] = null;
                deadlineHasTime[last] = false;

                eventFrom[last] = null;
                eventTo[last] = null;
                eventFromHasTime[last] = false;
                eventToHasTime[last] = false;

                taskCount--;

                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                ui.showDeletedTask(removed, taskCount);
                continue;
            }

            if (cleaned.equals("todo")) {
                ui.showError("The description of a todo cannot be empty. Usage: todo <description>");
                continue;
            }

            String todoDesc = Parser.parseTodoDescription(cleaned);
            if (todoDesc != null) {
                type[taskCount] = 'T';
                desc[taskCount] = todoDesc;
                isDone[taskCount] = false;
                taskCount++;

                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                String formatted = formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount - 1);

                ui.showAddedTask(formatted, taskCount);
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
                    ui.showError("Invalid date format. Use: yyyy-MM-dd or yyyy-MM-dd HHmm (e.g. 2019-10-15 or 2019-10-15 1800)");
                    continue;
                }

                type[taskCount] = 'D';
                desc[taskCount] = deadlineParts[0];
                deadlineBy[taskCount] = parsed.value;
                deadlineHasTime[taskCount] = parsed.hasTime;
                isDone[taskCount] = false;
                taskCount++;

                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                String formatted = formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount - 1);

                ui.showAddedTask(formatted, taskCount);
                continue;
            } else if (Parser.isCommand(cleaned, "deadline")) {
                ui.showError("Deadline usage: deadline <desc> /by <yyyy-MM-dd> [HHmm]");
                continue;
            }

            if (cleaned.equals("event")) {
                ui.showError("Event needs /from and /to. Usage: event <desc> /from <yyyy-MM-dd> [HHmm] /to <yyyy-MM-dd> [HHmm]");
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

                type[taskCount] = 'E';
                desc[taskCount] = eventParts[0];
                eventFrom[taskCount] = parsedFrom.value;
                eventTo[taskCount] = parsedTo.value;
                eventFromHasTime[taskCount] = parsedFrom.hasTime;
                eventToHasTime[taskCount] = parsedTo.hasTime;
                isDone[taskCount] = false;
                taskCount++;

                storage.save(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount);

                String formatted = formatTask(type, desc, isDone,
                        deadlineBy, deadlineHasTime,
                        eventFrom, eventTo, eventFromHasTime, eventToHasTime,
                        taskCount - 1);

                ui.showAddedTask(formatted, taskCount);
                continue;
            } else if (Parser.isCommand(cleaned, "event")) {
                ui.showError("Event usage: event <desc> /from <date> /to <date>");
                continue;
            }

            ui.showError("I'm sorry, I don't know what that means. Try: todo, deadline, event, list, mark, unmark, delete, bye");
        }
    }

    private static class ParsedDateTime {
        LocalDateTime value;
        boolean hasTime;

        ParsedDateTime(LocalDateTime value, boolean hasTime) {
            this.value = value;
            this.hasTime = hasTime;
        }
    }

    private static ParsedDateTime parseDateOrDateTime(String s) {
        try {
            LocalDateTime dt = LocalDateTime.parse(s, INPUT_DATE_TIME);
            return new ParsedDateTime(dt, true);
        } catch (DateTimeParseException ignored) { }

        try {
            LocalDate d = LocalDate.parse(s, INPUT_DATE);
            return new ParsedDateTime(d.atStartOfDay(), false);
        } catch (DateTimeParseException ignored) { }

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
        if (dt == null) return "";
        String datePart = dt.toLocalDate().format(OUTPUT_DATE);
        if (!hasTime) return datePart;
        return datePart + " " + dt.toLocalTime().format(OUTPUT_TIME);
    }
}