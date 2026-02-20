package adolf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class AdolfTest {

    @Test
    public void parseDateOrDateTime_validDate_returnsParsedDateTime() {
        Adolf.ParsedDateTime r = Adolf.parseDateOrDateTime("2025-12-31");
        assertNotNull(r);
        assertEquals(LocalDateTime.of(2025, 12, 31, 0, 0), r.getValue());
        assertFalse(r.isHasTime());
    }

    @Test
    public void parseDateOrDateTime_validDateTime_returnsParsedDateTime() {
        Adolf.ParsedDateTime r = Adolf.parseDateOrDateTime("2025-12-31 1800");
        assertNotNull(r);
        assertEquals(LocalDateTime.of(2025, 12, 31, 18, 0), r.getValue());
        assertTrue(r.isHasTime());
    }

    @Test
    public void parseDateOrDateTime_invalid_returnsNull() {
        assertNull(Adolf.parseDateOrDateTime("31-12-2025"));
        assertNull(Adolf.parseDateOrDateTime("not a date"));
        assertNull(Adolf.parseDateOrDateTime(""));
    }

    @Test
    public void formatTaskForUi_todoFormatsCorrectly() {
        TaskList list = new TaskList(100);
        list.addTodo("read book");
        String line = Adolf.formatTaskForUi(list, 0);
        assertEquals("[T][ ] read book", line);

        list.markDone(0, true);
        line = Adolf.formatTaskForUi(list, 0);
        assertEquals("[T][X] read book", line);
    }

    @Test
    public void formatTaskForUi_deadlineFormatsCorrectly() {
        TaskList list = new TaskList(100);
        list.addDeadline("submit", LocalDateTime.of(2025, 3, 15, 23, 59), true);
        String line = Adolf.formatTaskForUi(list, 0);
        assertEquals("[D][ ] submit (by: Mar 15 2025 2359)", line);
    }

    @Test
    public void formatTaskForUi_eventFormatsCorrectly() {
        TaskList list = new TaskList(100);
        list.addEvent("meeting",
                LocalDateTime.of(2025, 6, 1, 9, 0), true,
                LocalDateTime.of(2025, 6, 1, 10, 0), true);
        String line = Adolf.formatTaskForUi(list, 0);
        assertEquals("[E][ ] meeting (from: Jun 01 2025 0900 to: Jun 01 2025 1000)", line);
    }
}
