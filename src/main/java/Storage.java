import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }


    private void ensureFolderExists() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
    }

    public int load(char[] type, String[] desc, String[] by,
                    String[] from, String[] to, boolean[] isDone) {
        try {
            ensureFolderExists();

            if (!Files.exists(filePath)) {
                return 0;
            }

            List<String> lines = Files.readAllLines(filePath);
            int count = 0;

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue;
                }


                String[] parts = line.split("\\|", -1);
                if (parts.length != 6) {
                    continue;
                }

                char t = parts[0].isEmpty() ? '\0' : parts[0].charAt(0);
                String doneStr = parts[1];
                String d = parts[2];
                String b = parts[3];
                String f = parts[4];
                String tt = parts[5];

                if (t != 'T' && t != 'D' && t != 'E') {
                    continue;
                }
                if (!doneStr.equals("0") && !doneStr.equals("1")) {
                    continue;
                }
                if (d == null || d.trim().isEmpty()) {
                    continue;
                }

                type[count] = t;
                desc[count] = d;
                isDone[count] = doneStr.equals("1");

                by[count] = b.isEmpty() ? null : b;
                from[count] = f.isEmpty() ? null : f;
                to[count] = tt.isEmpty() ? null : tt;

                count++;

                if (count >= type.length) {
                    break;
                }
            }

            return count;

        } catch (IOException e) {
            return 0;
        }
    }

    public void save(char[] type, String[] desc, String[] by,
                     String[] from, String[] to, boolean[] isDone, int taskCount) {
        try {
            ensureFolderExists();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < taskCount; i++) {
                char t = type[i];
                String d = (desc[i] == null) ? "" : desc[i];
                String b = (by[i] == null) ? "" : by[i];
                String f = (from[i] == null) ? "" : from[i];
                String tt = (to[i] == null) ? "" : to[i];
                String done = isDone[i] ? "1" : "0";

                sb.append(t).append("|")
                        .append(done).append("|")
                        .append(d).append("|")
                        .append(b).append("|")
                        .append(f).append("|")
                        .append(tt).append("\n");
            }

            Files.writeString(filePath, sb.toString());

        } catch (IOException e) {
        }
    }
}