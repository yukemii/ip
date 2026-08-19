import java.util.List;
import java.util.Scanner;

/**
 * A simple text-based personal assistant named Sheppy.
 *
 * <p>Sheppy stores tasks in memory, displays them on request, and exits when
 * the user asks it to leave.</p>
 */
public class Sheppy {
    /**
     * Runs Sheppy's greeting, task-management loop, and exit command.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "  _____ _                       _   _\n"
                + " / ____| |                     | | | |\n"
                + "| (___ | |__   ___ _ __  _ __  | |_| |\n"
                + " \\___ \\| '_ \\ / _ \\ '_ \\| '_ \\ |  _  |\n"
                + " ____) | | | |  __/ |_) | |_) || | | |\n"
                + "|_____/|_| |_|\\___| .__/| .__/ |_| |_|\n"
                + "                   | |   | |\n"
                + "                   |_|   |_|\n";
        System.out.println(banner);
        System.out.println("Baa-hello! I'm Sheppy, your woolly little helper.");
        System.out.println("What shall we graze on today?");

        Storage storage = new Storage("data/tasks.txt");
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (SheppyException exception) {
            System.out.println("Baa-error: " + exception.getMessage());
            tasks = new TaskList(List.of());
        }
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            try {
                if (command.equals("bye")) {
                    System.out.println("Baa-bye! Keep your thoughts cozy and your tasks tidy.");
                    return;
                }
                if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 1; i <= tasks.size(); i++) {
                        System.out.println(i + "." + tasks.get(i));
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    updateTaskStatus(command, tasks, storage, true);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    updateTaskStatus(command, tasks, storage, false);
                } else if (command.equals("delete") || command.startsWith("delete ")) {
                    deleteTask(command, tasks, storage);
                } else if (command.equals("todo") || command.startsWith("todo ")) {
                    addTask(new Todo(command.substring(4).trim()), tasks, storage);
                } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                    addDeadline(command, tasks, storage);
                } else if (command.equals("event") || command.startsWith("event ")) {
                    addEvent(command, tasks, storage);
                } else {
                    throw new SheppyException(
                            "I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or delete.");
                }
            } catch (SheppyException exception) {
                System.out.println("Baa-error: " + exception.getMessage());
            }
        }
    }

    /** Adds a task to the list and reports the new total. */
    private static void addTask(Task task, TaskList tasks, Storage storage)
            throws SheppyException {
        tasks.add(task);
        storage.save(tasks);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Parses and adds a deadline command. */
    private static void addDeadline(String command, TaskList tasks, Storage storage)
            throws SheppyException {
        String details = command.substring("deadline ".length());
        int separator = details.indexOf(" /by ");
        if (separator < 0) {
            throw new SheppyException("a deadline needs a description and a /by date or time.");
        }
        String description = details.substring(0, separator).trim();
        String by = details.substring(separator + " /by ".length()).trim();
        addTask(new Deadline(description, parseDate(by)), tasks, storage);
    }

    /** Parses and adds an event command. */
    private static void addEvent(String command, TaskList tasks, Storage storage)
            throws SheppyException {
        String details = command.substring("event ".length());
        int fromSeparator = details.indexOf(" /from ");
        int toSeparator = details.indexOf(" /to ");
        if (fromSeparator < 0 || toSeparator < 0 || toSeparator < fromSeparator) {
            throw new SheppyException("an event needs a description, /from time, and /to time.");
        }
        String description = details.substring(0, fromSeparator).trim();
        String from = details.substring(fromSeparator + " /from ".length(), toSeparator).trim();
        String to = details.substring(toSeparator + " /to ".length()).trim();
        addTask(new Event(description, from, to), tasks, storage);
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param command the complete command entered by the user
     * @param tasks the current task list
     * @param markDone whether the task should be marked done
    */
    private static void updateTaskStatus(String command, TaskList tasks, Storage storage,
                                         boolean markDone) throws SheppyException {
        String commandName = command.trim().split("\\s+")[0];
        int taskNumber = parseTaskNumber(command, commandName);
        Task task = tasks.updateStatus(taskNumber, markDone);
        if (markDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        storage.save(tasks);
        System.out.println("  " + task);
    }

    /** Deletes a task and reports the remaining total. */
    private static void deleteTask(String command, TaskList tasks, Storage storage)
            throws SheppyException {
        int taskNumber = parseTaskNumber(command, "delete");
        Task deletedTask = tasks.remove(taskNumber);
        storage.save(tasks);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Parses the task number from a mark, unmark, or delete command. */
    private static int parseTaskNumber(String command, String commandName)
            throws SheppyException {
        String[] parts = command.trim().split("\\s+");
        if (parts.length != 2) {
            throw new SheppyException("use " + commandName
                    + " followed by a task number, such as " + commandName + " 2.");
        }
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new SheppyException("the task number must be a whole number.");
        }
    }

    /** Parses a date entered in the Level 8 ISO format. */
    private static java.time.LocalDate parseDate(String value) throws SheppyException {
        try {
            return java.time.LocalDate.parse(value);
        } catch (java.time.format.DateTimeParseException exception) {
            throw new SheppyException("please use dates in yyyy-MM-dd format, such as 2019-10-15.");
        }
    }
}
