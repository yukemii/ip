package sheppy;

import java.util.List;
import java.util.Scanner;

import sheppy.task.Task;
import sheppy.task.TaskList;

/** Handles Sheppy's interaction with the user. */
public class Ui {
    /** Reads commands from standard input. */
    private final Scanner scanner = new Scanner(System.in);

    /** Creates a user-interface handler connected to standard input. */
    public Ui() {
    }

    /** Displays Sheppy's welcome messages. */
    public void showWelcome() {
        String banner = "        __\n"
                + "       (oo)\n"
                + "  +---/----\\-----------------------+\n"
                + "  |        S H E P P Y             |\n"
                + "  |      your woolly helper        |\n"
                + "  +--------------------------------+\n";
        System.out.println(banner);
        System.out.println("Baa-hello! I'm Sheppy, your woolly little helper.");
        System.out.println("What shall we graze on today?");
    }

    /**
     * Checks whether another command is available.
     *
     * @return whether another command can be read
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return the next command line
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the exit message. */
    public void showBye() {
        System.out.println("Baa-bye! Keep your thoughts cozy and your tasks tidy.");
    }

    /**
     * Displays all tasks.
     *
     * @param tasks the tasks to display
     * @throws SheppyException if a task number cannot be retrieved
     */
    public void showTasks(TaskList tasks) throws SheppyException {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks matching a search keyword.
     *
     * @param matchingTasks the tasks whose descriptions matched the keyword
     */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /**
     * Displays a task-add confirmation.
     *
     * @param task the task that was added
     * @param taskCount the number of tasks after adding the task
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays a completion-status confirmation.
     *
     * @param task the task whose status changed
     * @param markDone whether the task was marked done rather than undone
     */
    public void showTaskStatus(Task task, boolean markDone) {
        if (markDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /**
     * Displays a task-deletion confirmation.
     *
     * @param task the task that was removed
     * @param taskCount the number of tasks remaining
     */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Displays an error message.
     *
     * @param message the explanation to show the user
     */
    public void showError(String message) {
        System.out.println("Baa-error: " + message);
    }
}
