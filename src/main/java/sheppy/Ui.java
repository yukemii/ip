package sheppy;

import java.util.Scanner;

import sheppy.task.Task;
import sheppy.task.TaskList;

/** Handles Sheppy's interaction with the user. */
public class Ui {
    /** Reads commands from standard input. */
    private final Scanner scanner = new Scanner(System.in);

    /** Displays Sheppy's welcome messages. */
    public void showWelcome() {
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
    }

    /** @return whether another command is available */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** @return the next command entered by the user */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the exit message. */
    public void showBye() {
        System.out.println("Baa-bye! Keep your thoughts cozy and your tasks tidy.");
    }

    /** Displays all tasks. */
    public void showTasks(TaskList tasks) throws SheppyException {
        System.out.println("Here are the tasks in your list:");
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + "." + tasks.get(i));
        }
    }

    /** Displays a task-add confirmation. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays a completion-status confirmation. */
    public void showTaskStatus(Task task, boolean markDone) {
        if (markDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
    }

    /** Displays a task-deletion confirmation. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /** Displays an error message. */
    public void showError(String message) {
        System.out.println("Baa-error: " + message);
    }
}
