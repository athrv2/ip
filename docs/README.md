# Adolf — User Guide

**Adolf** is your task-buddy chatbot. It helps you manage todos, deadlines and events from the command line or a simple GUI.

---

## Quick start

- **GUI:** Run `./gradlew run` (or run `adolf.Launcher` in your IDE). The window title shows **Adolf — Your task buddy**.
- **Command line:** Run `adolf.Adolf.main()` (or the generated JAR). Type commands and press Enter.

Type `bye` to exit.

---

## Notes about the command format

- **Parameters:** Words in `UPPER_CASE` are parameters you supply. Example: `todo DESCRIPTION` means you type something like `todo read book`, where `read book` is the description.
- **Optional items:** Items in square brackets `[]` are optional. Example: `deadline DESCRIPTION /by DATE [TIME]` can be `deadline submit report /by 2025-12-31 1800` (with time) or `deadline submit report /by 2025-12-31` (date only).
- **Parameter order:** For each command, parameters must appear in the order shown. For example, `deadline` requires the description first, then ` /by `, then the date (and optionally the time).
- **Extraneous parameters:** For commands that do not take parameters (e.g. `list`, `bye`), only the command word is needed. Extra words may be treated as an unknown command.
- **Spaces:** Leading and trailing spaces are ignored. Multiple spaces between words are treated as a single space.
- **Copy-paste:** If you copy commands from a PDF or a web page, check that spaces (especially around `/by`, `/from`, `/to`) are preserved; line breaks can sometimes remove spaces.

---

## Features

### Listing all tasks : `list` command

Shows all tasks in your list with their indices (1, 2, 3, …). Use these indices with `mark`, `unmark`, and `delete`.

**Format:** `list`

**Example:**

```
list
```

**Tip:** Task indices start at 1 and match the numbers you use in `mark`, `unmark`, and `delete`.

---

### Adding a todo : `todo` command

Adds a todo task with a description. The description cannot be empty.

**Format:** `todo DESCRIPTION`

**Examples:**

```
todo read book
todo submit assignment
```

**Tip:** A todo has no date; use `deadline` or `event` for time-based tasks.

---

### Adding a deadline : `deadline` command

Adds a deadline task: a description and a due date. You can optionally include a time of day.

**Format:** `deadline DESCRIPTION /by DATE [TIME]`

- **DATE:** `yyyy-MM-dd` (e.g. `2025-12-31`)
- **TIME (optional):** a space followed by `HHmm` in 24-hour format (e.g. `1800` for 6:00 PM)

**Examples:**

```
deadline submit report /by 2025-12-31
deadline submit report /by 2025-12-31 1800
deadline return book /by 2025-06-15 0900
```

**Tip:** Use the same date format so Adolf can parse it correctly; invalid formats are rejected.

---

### Adding an event : `event` command

Adds an event with a description, a start date/time, and an end date/time. The end must be **after** the start.

**Format:** `event DESCRIPTION /from DATE [TIME] /to DATE [TIME]`

- **DATE:** `yyyy-MM-dd`
- **TIME (optional):** space and `HHmm` (24-hour)

**Examples:**

```
event meeting /from 2025-06-01 /to 2025-06-01
event meeting /from 2025-06-01 0900 /to 2025-06-01 1000
event workshop /from 2025-07-10 1400 /to 2025-07-10 1700
```

**Tip:** Do not use `/from` or `/to` more than once in the same command; duplicate keywords are invalid.

---

### Marking a task as done : `mark` command

Marks the task at the given index as done. The index is the number shown in `list` (1-based).

**Format:** `mark INDEX`

**Examples:**

```
mark 1
mark 3
```

**Tip:** If the index is invalid or out of range, Adolf will show an error. Use `list` to see current indices.

---

### Marking a task as not done : `unmark` command

Marks the task at the given index as not done yet.

**Format:** `unmark INDEX`

**Examples:**

```
unmark 1
unmark 2
```

---

### Deleting a task : `delete` command

Removes the task at the given index from the list. Remaining tasks are renumbered.

**Format:** `delete INDEX`

**Examples:**

```
delete 1
delete 2
```

**Tip:** After deleting, run `list` again to see the updated indices before using `mark`, `unmark`, or `delete` on other tasks.

---

### Finding tasks : `find` command

Shows only tasks whose description contains the given keyword. Matching is case-sensitive.

**Format:** `find KEYWORD`

**Examples:**

```
find book
find meeting
find 2025
```

**Tip:** The keyword must not be empty. To see all tasks again, use `list`.

---

### Exiting : `bye` command

Says goodbye and closes the application (GUI window closes; CLI exits).

**Format:** `bye`

**Example:**

```
bye
```

---

## Data

Tasks are saved automatically to `./data/adolf.txt` and loaded when you start Adolf. If the file is missing or unreadable, Adolf starts with an empty list.

---

## Errors

If a command is invalid or a parameter is missing or wrong, Adolf replies with a short message (e.g. **Oops — …**). In the GUI, error messages are shown in a highlighted style. Fix the command (check format, index, or date) and try again.
