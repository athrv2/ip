import java.time.LocalDateTime;

public class TaskList {
    private final char[] type;
    private final String[] desc;
    private final boolean[] isDone;

    private final LocalDateTime[] deadlineBy;
    private final boolean[] deadlineHasTime;

    private final LocalDateTime[] eventFrom;
    private final LocalDateTime[] eventTo;
    private final boolean[] eventFromHasTime;
    private final boolean[] eventToHasTime;

    private int size;

    public TaskList(int capacity) {
        type = new char[capacity];
        desc = new String[capacity];
        isDone = new boolean[capacity];

        deadlineBy = new LocalDateTime[capacity];
        deadlineHasTime = new boolean[capacity];

        eventFrom = new LocalDateTime[capacity];
        eventTo = new LocalDateTime[capacity];
        eventFromHasTime = new boolean[capacity];
        eventToHasTime = new boolean[capacity];

        size = 0;
    }

    public int size() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void incrementSize() {
        size++;
    }

    public void decrementSize() {
        size--;
    }

    public void markDone(int index, boolean done) {
        isDone[index] = done;
    }

    // Temporary getters so Storage + Adolf can keep working while we refactor
    public char[] types() { return type; }
    public String[] descs() { return desc; }
    public boolean[] dones() { return isDone; }

    public LocalDateTime[] deadlineBy() { return deadlineBy; }
    public boolean[] deadlineHasTime() { return deadlineHasTime; }

    public LocalDateTime[] eventFrom() { return eventFrom; }
    public LocalDateTime[] eventTo() { return eventTo; }
    public boolean[] eventFromHasTime() { return eventFromHasTime; }
    public boolean[] eventToHasTime() { return eventToHasTime; }
}