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

        Task[] tasks = new Task[100];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.println("Baa-bye! Keep your thoughts cozy and your tasks tidy.");
                return;
            }
            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                taskCount = updateTaskStatus(command, tasks, taskCount, true);
            } else if (command.startsWith("unmark ")) {
                taskCount = updateTaskStatus(command, tasks, taskCount, false);
            } else if (command.startsWith("todo ")) {
                taskCount = addTask(new Todo(command.substring(5)), tasks, taskCount);
            } else if (command.startsWith("deadline ")) {
                taskCount = addDeadline(command, tasks, taskCount);
            } else if (command.startsWith("event ")) {
                taskCount = addEvent(command, tasks, taskCount);
            } else if (taskCount < tasks.length) {
                taskCount = addTask(new Todo(command), tasks, taskCount);
            }
        }
    }

    /** Adds a task to the list and reports the new total. */
    private static int addTask(Task task, Task[] tasks, int taskCount) {
        if (taskCount >= tasks.length) {
            System.out.println("Your task list is full.");
            return taskCount;
        }
        tasks[taskCount] = task;
        taskCount++;
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
        return taskCount;
    }

    /** Parses and adds a deadline command. */
    private static int addDeadline(String command, Task[] tasks, int taskCount) {
        String details = command.substring("deadline ".length());
        int separator = details.indexOf(" /by ");
        if (separator < 0) {
            System.out.println("A deadline needs a /by date or time.");
            return taskCount;
        }
        String description = details.substring(0, separator);
        String by = details.substring(separator + " /by ".length());
        return addTask(new Deadline(description, by), tasks, taskCount);
    }

    /** Parses and adds an event command. */
    private static int addEvent(String command, Task[] tasks, int taskCount) {
        String details = command.substring("event ".length());
        int fromSeparator = details.indexOf(" /from ");
        int toSeparator = details.indexOf(" /to ");
        if (fromSeparator < 0 || toSeparator < 0 || toSeparator < fromSeparator) {
            System.out.println("An event needs both /from and /to times.");
            return taskCount;
        }
        String description = details.substring(0, fromSeparator);
        String from = details.substring(fromSeparator + " /from ".length(), toSeparator);
        String to = details.substring(toSeparator + " /to ".length());
        return addTask(new Event(description, from, to), tasks, taskCount);
    }

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param command the complete command entered by the user
     * @param tasks the current task array
     * @param taskCount the number of stored tasks
     * @param markDone whether the task should be marked done
     * @return the unchanged number of stored tasks
     */
    private static int updateTaskStatus(String command, Task[] tasks, int taskCount,
                                        boolean markDone) {
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            System.out.println("Please provide a task number.");
            return taskCount;
        }

        try {
            int taskNumber = Integer.parseInt(parts[1]);
            if (taskNumber < 1 || taskNumber > taskCount) {
                System.out.println("That task number is not in your list.");
                return taskCount;
            }

            Task task = tasks[taskNumber - 1];
            if (markDone) {
                task.markAsDone();
                System.out.println("Nice! I've marked this task as done:");
            } else {
                task.markAsUndone();
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println("  " + task);
        } catch (NumberFormatException exception) {
            System.out.println("Please provide a valid task number.");
        }
        return taskCount;
    }
}
