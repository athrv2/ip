package adolf;

import java.time.LocalDateTime;

/**
 * Represents a list of tasks and provides operations to manage them.
 * Internally stores tasks in parallel arrays (type/desc/isDone + date/time fields).
 */
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

    /**
     * Creates a TaskList with the given capacity.
     *
     * @param capacity maximum number of tasks allowed
     */
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

    /**
     * Returns the current number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return size;
    }

    /**
     * Sets the size after loading from storage.
     * Assumes the arrays already contain valid loaded data up to the given size.
     *
     * @param size new size (must be between 0 and capacity inclusive)
     */
    public void setSize(int size) {
        if (size < 0 || size > type.length) {
            // keep it safe: do nothing if invalid
            return;
        }
        this.size = size;
    }

    /**
     * Marks a task as done or not done.
     *
     * @param index task index (0-based)
     * @param done  true to mark done, false to mark not done
     */
    public void markDone(int index, boolean done) {
        if (!isValidIndex(index)) {
            return;
        }

        assert index >= 0 && index < size : "markDone index out of range: " + index;
        isDone[index] = done;
    }

    /**
     * Adds a todo task.
     *
     * @param description task description
     * @return index of the newly added task, or -1 if capacity is full
     */
    public int addTodo(String description) {
        if (isFull()) {
            return -1;
        }
        assert description != null : "Todo description is null";
        assert !description.isBlank() : "Todo description is blank";
        assert size < type.length : "TaskList is full";

        type[size] = 'T';
        desc[size] = description;
        isDone[size] = false;

        // clear date/time fields
        deadlineBy[size] = null;
        deadlineHasTime[size] = false;

        eventFrom[size] = null;
        eventTo[size] = null;
        eventFromHasTime[size] = false;
        eventToHasTime[size] = false;

        int addedIndex = size;
        size++;
        return addedIndex;
    }

    /**
     * Adds a deadline task.
     *
     * @param description task description
     * @param by          deadline date/time (or date at start-of-day)
     * @param hasTime     whether the user provided a time
     * @return index of the newly added task, or -1 if capacity is full
     */
    public int addDeadline(String description, LocalDateTime by, boolean hasTime) {
        if (isFull()) {
            return -1;
        }

        assert description != null : "Deadline description is null";
        assert !description.isBlank() : "Deadline description is blank";
        assert by != null : "Deadline 'by' is null";
        assert size < type.length : "TaskList is full";

        type[size] = 'D';
        desc[size] = description;
        isDone[size] = false;

        deadlineBy[size] = by;
        deadlineHasTime[size] = hasTime;

        // clear event fields
        eventFrom[size] = null;
        eventTo[size] = null;
        eventFromHasTime[size] = false;
        eventToHasTime[size] = false;

        int addedIndex = size;
        size++;
        return addedIndex;
    }

    /**
     * Adds an event task.
     *
     * @param description  task description
     * @param from         start date/time (or date at start-of-day)
     * @param fromHasTime  whether the user provided a start time
     * @param to           end date/time (or date at start-of-day)
     * @param toHasTime    whether the user provided an end time
     * @return index of the newly added task, or -1 if capacity is full
     */
    public int addEvent(String description,
                        LocalDateTime from, boolean fromHasTime,
                        LocalDateTime to, boolean toHasTime) {

        if (isFull()) {
            return -1;
        }

        assert description != null : "Event description is null";
        assert !description.isBlank() : "Event description is blank";
        assert from != null : "Event 'from' is null";
        assert to != null : "Event 'to' is null";
        assert size < type.length : "TaskList is full";

        type[size] = 'E';
        desc[size] = description;
        isDone[size] = false;

        eventFrom[size] = from;
        eventTo[size] = to;
        eventFromHasTime[size] = fromHasTime;
        eventToHasTime[size] = toHasTime;

        // clear deadline fields
        deadlineBy[size] = null;
        deadlineHasTime[size] = false;

        int addedIndex = size;
        size++;
        return addedIndex;
    }

    /**
     * Deletes the task at the given index by shifting later tasks left.
     *
     * @param index task index (0-based)
     */
    public void delete(int index) {
        if (!isValidIndex(index)) {
            return;
        }
        
        assert index >= 0 && index < size : "delete index out of range: " + index;

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

        // clear last slot
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

    // ---- helper methods ----

    private boolean isFull() {
        return size >= type.length;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }

    // Temporary getters so Storage + Adolf can keep working while we refactor
    public char[] types() {
        return type;
    }

    public String[] descs() {
        return desc;
    }

    public boolean[] dones() {
        return isDone;
    }

    public LocalDateTime[] deadlineBy() {
        return deadlineBy;
    }

    public boolean[] deadlineHasTime() {
        return deadlineHasTime;
    }

    public LocalDateTime[] eventFrom() {
        return eventFrom;
    }

    public LocalDateTime[] eventTo() {
        return eventTo;
    }

    public boolean[] eventFromHasTime() {
        return eventFromHasTime;
    }

    public boolean[] eventToHasTime() {
        return eventToHasTime;
    }
}
