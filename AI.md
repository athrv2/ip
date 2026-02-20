# AI-Assisted Development (A-AiAssisted)

This document records the use of AI tools in this project, as per the A-AiAssisted increment guidelines.

## Tool used

- **Cursor** (with AI assistant) – for code enhancement, refactoring, and implementing optional increments.

## Where and how AI was used

### Optional increments (A-Personality, A-BetterGui, A-MoreErrorHandling)

- **A-Personality:** AI assisted in defining a consistent personality (friendly task assistant), phrasing for CLI and GUI responses, and GUI styling (colors, fonts) in `Main.java`, `Ui.java`, and `AdolfBot.java`.
- **A-BetterGui:** AI assisted in implementing an asymmetric conversation layout (user vs bot messages styled differently), error-message highlighting, and visual tweaks (padding, fonts, colors, window resizing) in `Main.java`.
- **A-MoreErrorHandling:** AI assisted in adding handling for: command-format issues (normalized spaces, missing/duplicate parameters), environment issues (missing data file, read/write failures), and data validation (event start before end, invalid dates) in `Parser.java`, `Storage.java`, `Adolf.java`, and `AdolfBot.java`.

### General

- AI was used to improve code quality (e.g. Checkstyle compliance), add Javadoc, and suggest tests where applicable.
- All AI-suggested code was reviewed and adjusted to fit the existing codebase and course requirements.

## Credit

AI assistance was provided by **Cursor** (cursor.com). Ideas and structure were adapted to this project’s constraints and style.
