package adolf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void addTodo_validDescription_increasesSizeAndReturnsIndex() {
        TaskList list = new TaskList(100);
        assertEquals(0, list.size());

        int idx = list.addTodo("read book");
        assertEquals(0, idx);
        assertEquals(1, list.size());

        idx = list.addTodo("submit assignment");
        assertEquals(1, idx);
        assertEquals(2, list.size());
    }

    @Test
    public void size_afterSetSize_reflectsLoadedCount() {
        TaskList list = new TaskList(100);
        list.addTodo("a");
        list.addTodo("b");
        assertEquals(2, list.size());

        list.setSize(0);
        assertEquals(0, list.size());

        list.setSize(2);
        assertEquals(2, list.size());
    }

    @Test
    public void markDone_thenCheckViaGetters() {
        TaskList list = new TaskList(100);
        list.addTodo("task");
        assertEquals(1, list.size());

        list.markDone(0, true);
        boolean[] dones = list.dones();
        assertEquals(true, dones[0]);
    }

    @Test
    public void addDeadline_increasesSizeAndStoresBy() {
        TaskList list = new TaskList(100);
        LocalDateTime by = LocalDateTime.of(2025, 12, 31, 18, 0);
        int idx = list.addDeadline("submit report", by, true);
        assertEquals(0, idx);
        assertEquals(1, list.size());
        assertEquals('D', list.types()[0]);
        assertEquals("submit report", list.descs()[0]);
        assertEquals(by, list.deadlineBy()[0]);
        assertEquals(true, list.deadlineHasTime()[0]);
    }

    @Test
    public void addEvent_increasesSizeAndStoresFromTo() {
        TaskList list = new TaskList(100);
        LocalDateTime from = LocalDateTime.of(2025, 6, 1, 9, 0);
        LocalDateTime to = LocalDateTime.of(2025, 6, 1, 17, 0);
        int idx = list.addEvent("meeting", from, true, to, true);
        assertEquals(0, idx);
        assertEquals(1, list.size());
        assertEquals('E', list.types()[0]);
        assertEquals("meeting", list.descs()[0]);
        assertEquals(from, list.eventFrom()[0]);
        assertEquals(to, list.eventTo()[0]);
    }

    @Test
    public void delete_removesTaskAndShiftsRemaining() {
        TaskList list = new TaskList(100);
        list.addTodo("first");
        list.addTodo("second");
        list.addTodo("third");
        assertEquals(3, list.size());

        list.delete(1);
        assertEquals(2, list.size());
        assertEquals("first", list.descs()[0]);
        assertEquals("third", list.descs()[1]);
    }
}
