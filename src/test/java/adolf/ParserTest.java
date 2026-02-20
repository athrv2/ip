package adolf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void normalizeInput_collapsesSpacesAndTrims() {
        assertEquals("mark 1", Parser.normalizeInput("mark  1"));
        assertEquals("todo x", Parser.normalizeInput("  todo   x  "));
        assertEquals("", Parser.normalizeInput("   "));
    }

    @Test
    public void isCommand_exactMatch_returnsTrue() {
        assertTrue(Parser.isCommand("list", "list"));
        assertTrue(Parser.isCommand("bye", "bye"));
        assertTrue(Parser.isCommand("mark", "mark"));
    }

    @Test
    public void isCommand_commandWithArgs_returnsTrue() {
        assertTrue(Parser.isCommand("mark 1", "mark"));
        assertTrue(Parser.isCommand("todo read book", "todo"));
        assertTrue(Parser.isCommand("delete 2", "delete"));
    }

    @Test
    public void isCommand_wrongCommand_returnsFalse() {
        assertFalse(Parser.isCommand("list", "mark"));
        assertFalse(Parser.isCommand("todo", "deadline"));
        assertFalse(Parser.isCommand("unknown", "list"));
    }

    @Test
    public void parseTodoDescription_validInput_returnsDescription() {
        assertEquals("read book", Parser.parseTodoDescription("todo read book"));
        assertEquals("a", Parser.parseTodoDescription("todo a"));
    }

    @Test
    public void parseTodoDescription_emptyOrInvalid_returnsNull() {
        assertNull(Parser.parseTodoDescription("todo "));
        assertNull(Parser.parseTodoDescription("todo"));
        assertNull(Parser.parseTodoDescription("list"));
        assertNull(Parser.parseTodoDescription("todo"));
    }

    @Test
    public void parseIndex_validInput_returnsZeroBasedIndex() {
        assertEquals(0, Parser.parseIndex("mark 1", "mark"));
        assertEquals(2, Parser.parseIndex("delete 3", "delete"));
    }

    @Test
    public void parseIndex_invalidInput_returnsNull() {
        assertNull(Parser.parseIndex("mark", "mark"));
        assertNull(Parser.parseIndex("mark one", "mark"));
    }

    @Test
    public void parseFindKeyword_validInput_returnsKeyword() {
        assertEquals("book", Parser.parseFindKeyword("find book"));
        assertEquals("read", Parser.parseFindKeyword("find read"));
    }

    @Test
    public void parseFindKeyword_invalidOrEmpty_returnsNull() {
        assertNull(Parser.parseFindKeyword("find"));
        assertNull(Parser.parseFindKeyword("find "));
        assertNull(Parser.parseFindKeyword("todo read book"));
    }
}
