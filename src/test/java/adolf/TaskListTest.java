package adolf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
