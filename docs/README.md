# Adolf — User Guide

**Adolf** is your task-buddy chatbot. It helps you manage todos, deadlines and events from the command line or a simple GUI.

---

## Quick start

- **GUI:** Run `./gradlew run` (or run `adolf.Launcher` in your IDE). The window title shows **Adolf — Your task buddy**.
- **Command line:** Run `adolf.Adolf.main()` (or the generated JAR). Type commands and press Enter.

Type `bye` to exit.

---

## Features

### List tasks

- **Command:** `list`
- Shows all tasks with indices. Use these indices for `mark`, `unmark`, `delete`.

### Add a todo

- **Command:** `todo <description>`
- Example: `todo read book`

### Add a deadline

- **Command:** `deadline <description> /by <date> [time]`
- **Date:** `yyyy-MM-dd` (e.g. `2025-12-31`)
- **Optional time:** add a space and `HHmm` (e.g. `2025-12-31 2359`)
- Example: `deadline submit report /by 2025-12-31 1800`

### Add an event

- **Command:** `event <description> /from <date> [time] /to <date> [time]`
- **Rule:** End date/time must be **after** start.
- Example: `event meeting /from 2025-06-01 0900 /to 2025-06-01 1000`

### Mark / unmark

- **Mark done:** `mark <number>` (number from `list`)
- **Mark not done:** `unmark <number>`

### Delete a task

- **Command:** `delete <number>`

### Find tasks

- **Command:** `find <keyword>`
- Lists only tasks whose description contains the keyword (case-sensitive).

### Exit

- **Command:** `bye`

---

## Data

Tasks are saved automatically to `./data/adolf.txt`. They load again when you start Adolf. Leading/trailing spaces in commands are ignored; multiple spaces are treated as one.

---

## Errors

If a command is invalid or a parameter is missing, Adolf replies with a short message (e.g. **Oops — …**). In the GUI, error replies are shown in a highlighted style. Fix the command and try again.
