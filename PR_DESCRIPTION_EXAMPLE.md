# Adolf

> "Your mind is for having ideas, not holding them." - David Allen ([source](https://gettingthingsdone.com/))

Adolf frees your mind of having to remember things you need to do. It's,

* text-based
* easy to learn
* **FAST** *SUPER* ~~FAST~~ to use

All you need to do is,

1. download it from [here](https://github.com/athrv2/ip/releases).
2. double-click it.
3. add your tasks.
4. let it manage your tasks for you 😅

And it is **FREE!**

## Features:

- [x] Managing tasks
- [ ] Managing deadlines (coming soon)
- [ ] Reminders (coming soon)

If you are a Java programmer, you can use it to practice Java too. Here's the `main` method:

```java
public class Adolf {
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList tasks = new TaskList(100);
        Storage storage = new Storage("./data/adolf.txt");
        // ... rest of the code
    }
}
```
