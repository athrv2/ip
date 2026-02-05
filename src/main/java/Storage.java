import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;
    private static final DateTimeFormatter SAVE_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    public void save(char[] type, String[] desc, boolean[] isDone,
                     LocalDateTime[] deadlineBy, boolean[] deadlineHasTime,
                     LocalDateTime[] eventFrom, LocalDateTime[] eventTo,
                     boolean[] eventFromHasTime, boolean[] eventToHasTime,
                     int taskCount) {

        try {
            ensureParentFolderExists();

            List<String> lines = new ArrayList<>();
            for (int i = 0; i < taskCount; i++) {

                if (type[i] == 'T') {
                    lines.add("T | " + (isDone[i] ? 1 : 0) + " | " + safe(desc[i]));
                } else if (type[i] == 'D') {
                    String dt = saveDateOrDateTime(deadlineBy[i], deadlineHasTime[i]);
                    lines.add("D | " + (isDone[i] ? 1 : 0) + " | " + safe(desc[i])
                            + " | " + dt + " | " + (deadlineHasTime[i] ? 1 : 0));
                } else if (type[i] == 'E') {
                    String fromStr = saveDateOrDateTime(eventFrom[i], eventFromHasTime[i]);
                    String toStr = saveDateOrDateTime(eventTo[i], eventToHasTime[i]);
                    lines.add("E | " + (isDone[i] ? 1 : 0) + " | " + safe(desc[i])
                            + " | " + fromStr + " | " + (eventFromHasTime[i] ? 1 : 0)
                            + " | " + toStr + " | " + (eventToHasTime[i] ? 1 : 0));
                }
            }

            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Could not save tasks: " + e.getMessage());
        }
    }

    public int load(char[] type, String[] desc, boolean[] isDone,
                    LocalDateTime[] deadlineBy, boolean[] deadlineHasTime,
                    LocalDateTime[] eventFrom, LocalDateTime[] eventTo,
                    boolean[] eventFromHasTime, boolean[] eventToHasTime) {

        if (!Files.exists(filePath)) {
            return 0;
        }

        try {
            List<String> lines = Files.readAllLines(filePath);
            int count = 0;

            for (String line : lines) {
                // Skip junk lines (stretch: corrupted file handling)
                if (line == null || line.trim().isEmpty()) continue;

                String[] parts = line.split(" \\| ");

                char t = parts[0].trim().charAt(0);

                if (t == 'T' && parts.length >= 3) {
                    type[count] = 'T';
                    isDone[count] = parts[1].trim().equals("1");
                    desc[count] = parts[2].trim();
                    count++;
                } else if (t == 'D' && parts.length >= 5) {
                    type[count] = 'D';
                    isDone[count] = parts[1].trim().equals("1");
                    desc[count] = parts[2].trim();
                    boolean hasTime = parts[4].trim().equals("1");
                    deadlineHasTime[count] = hasTime;
                    deadlineBy[count] = parseSavedDateOrDateTime(parts[3].trim(), hasTime);
                    count++;
                } else if (t == 'E' && parts.length >= 7) {
                    type[count] = 'E';
                    isDone[count] = parts[1].trim().equals("1");
                    desc[count] = parts[2].trim();

                    boolean fromHas = parts[4].trim().equals("1");
                    boolean toHas = parts[6].trim().equals("1");
                    eventFromHasTime[count] = fromHas;
                    eventToHasTime[count] = toHas;

                    eventFrom[count] = parseSavedDateOrDateTime(parts[3].trim(), fromHas);
                    eventTo[count] = parseSavedDateOrDateTime(parts[5].trim(), toHas);
                    count++;
                } else {
                }

                if (count >= 100) break;
            }

            return count;
        } catch (IOException e) {
            System.out.println("Could not load tasks: " + e.getMessage());
            return 0;
        }
    }

    private void ensureParentFolderExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static String saveDateOrDateTime(LocalDateTime dt, boolean hasTime) {
        if (dt == null) return "";
        if (!hasTime) {
            return dt.toLocalDate().toString();
        }
        return dt.format(SAVE_DATE_TIME);
    }

    private static LocalDateTime parseSavedDateOrDateTime(String s, boolean hasTime) {
        try {
            if (!hasTime) {
                return LocalDateTime.parse(s + " 0000", SAVE_DATE_TIME);
            }
            return LocalDateTime.parse(s, SAVE_DATE_TIME);
        } catch (Exception e) {
            return null;
        }
    }
}