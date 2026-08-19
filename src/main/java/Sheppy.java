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
                CommandType commandType = Parser.parseCommand(command);
                if (commandType == CommandType.BYE) {
                    System.out.println("Baa-bye! Keep your thoughts cozy and your tasks tidy.");
                    return;
                }
                if (commandType == CommandType.LIST) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 1; i <= tasks.size(); i++) {
                        System.out.println(i + "." + tasks.get(i));
                    }
                } else if (commandType == CommandType.MARK) {
                    updateTaskStatus(command, tasks, storage, true);
                } else if (commandType == CommandType.UNMARK) {
                    updateTaskStatus(command, tasks, storage, false);
                } else if (commandType == CommandType.DELETE) {
                    deleteTask(command, tasks, storage);
                } else if (commandType == CommandType.TODO
                        || commandType == CommandType.DEADLINE
                        || commandType == CommandType.EVENT) {
                    addTask(Parser.parseTask(command), tasks, storage);
                } else {
                    throw Parser.unknownCommand();
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

    /**
     * Updates a task's completion status based on a mark or unmark command.
     *
     * @param command the complete command entered by the user
     * @param tasks the current task list
     * @param markDone whether the task should be marked done
    */
    private static void updateTaskStatus(String command, TaskList tasks, Storage storage,
                                         boolean markDone) throws SheppyException {
        int taskNumber = Parser.parseTaskNumber(command);
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
        int taskNumber = Parser.parseTaskNumber(command);
        Task deletedTask = tasks.remove(taskNumber);
        storage.save(tasks);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

}
