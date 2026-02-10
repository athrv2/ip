package adolf;

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

    public void markDone(int index, boolean done) {
        isDone[index] = done;
    }

    public int addDeadline(String description, LocalDateTime by, boolean hasTime) {
        type[size] = 'D';
        desc[size] = description;
        isDone[size] = false;

        deadlineBy[size] = by;
        deadlineHasTime[size] = hasTime;

        int addedIndex = size;
        size++;

        return addedIndex;
    }

    public int addEvent(String description,
                    LocalDateTime from, boolean fromHasTime,
                    LocalDateTime to, boolean toHasTime) {

        type[size] = 'E';
        desc[size] = description;
        isDone[size] = false;

        eventFrom[size] = from;
        eventTo[size] = to;
        eventFromHasTime[size] = fromHasTime;
        eventToHasTime[size] = toHasTime;

        int addedIndex = size;
        size++;

        return addedIndex;
    }

    public int addTodo(String description) {
        type[size] = 'T';
        desc[size] = description;
        isDone[size] = false;

        // clear date/time fields (not strictly needed but keeps data clean)
        deadlineBy[size] = null;
        deadlineHasTime[size] = false;

        eventFrom[size] = null;
        eventTo[size] = null;
        eventFromHasTime[size] = false;
        eventToHasTime[size] = false;

        size++;
        return size - 1; // index of the added task
    }
    
    public void delete(int index) {
        if (size == 0) return;
        for (int i = index; i < size - 1; i++) {
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

        int last = size - 1;
        type[last] = '\0';
        desc[last] = null;
        isDone[last] = false;

        deadlineBy[last] = null;
        deadlineHasTime[last] = false;

        eventFrom[last] = null;
        eventTo[last] = null;
        eventFromHasTime[last] = false;
        eventToHasTime[last] = false;

        size--;
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
