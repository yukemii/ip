package sheppy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sheppy.storage.Storage;
import sheppy.task.Task;
import sheppy.task.TaskList;

/** Coordinates Sheppy's user interface, parser, task list, and storage. */
public class Sheppy {
    /** The default location used to save tasks. */
    private static final String DEFAULT_FILE_PATH = "data/tasks.txt";

    /** Stores tasks between application sessions. */
    private final Storage storage;

    /** Contains the tasks in the current session. */
    private TaskList tasks;

    /** Contains a loading error to show after startup, or an empty string. */
    private final String startupMessage;

    /** Creates Sheppy using the default task-data file. */
    public Sheppy() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates Sheppy using a specified task-data file.
     *
     * @param filePath the path of the task-data file
     */
    public Sheppy(String filePath) {
        storage = new Storage(filePath);

        TaskList loadedTasks;
        String loadingMessage = "";
        try {
            loadedTasks = storage.load();
        } catch (SheppyException exception) {
            loadedTasks = new TaskList();
            loadingMessage = formatError(exception.getMessage());
        }
        tasks = loadedTasks;
        startupMessage = loadingMessage;
    }

    /**
     * Starts Sheppy's command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Sheppy sheppy = new Sheppy();
        ui.showWelcome();
        if (!sheppy.getStartupMessage().isEmpty()) {
            ui.showResponse(sheppy.getStartupMessage());
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showResponse(sheppy.getResponse(command));
            if (sheppy.isExitCommand(command)) {
                return;
            }
        }
    }

    /**
     * Returns any message produced while loading saved tasks.
     *
     * @return the startup error, or an empty string if loading succeeded
     */
    public String getStartupMessage() {
        return startupMessage;
    }

    /**
     * Returns Sheppy's response to one user command.
     *
     * @param command the command entered by the user
     * @return Sheppy's response, including any validation error
     */
    public String getResponse(String command) {
        List<Task> previousTasks = new ArrayList<>(tasks.asList());
        List<String> previousStatuses = previousTasks.stream().map(Task::getStatusIcon).toList();
        try {
            command = Parser.normalize(command);
            if (command.isEmpty()) {
                throw new SheppyException("please enter a command, such as list.");
            }
            CommandType commandType = Parser.parseCommand(command);
            if (!startupMessage.isEmpty() && commandType != CommandType.LIST
                    && commandType != CommandType.FIND && commandType != CommandType.BYE
                    && commandType != CommandType.UNKNOWN) {
                throw new SheppyException("your data could not be loaded. Back up and repair the data file, "
                        + "then restart Sheppy before changing tasks.");
            }
            return switch (commandType) {
                case BYE -> "Baa-bye! Keep your thoughts cozy and your tasks tidy.";
                case LIST -> formatTasks("Here are the tasks in your list:", tasks.asList());
                case FIND -> formatTasks("Here are the matching tasks in your list:",
                        tasks.find(Parser.parseFindKeyword(command)));
                case SORT -> sortTasks();
                case MARK -> updateTaskStatus(command, true);
                case UNMARK -> updateTaskStatus(command, false);
                case DELETE -> deleteTask(command);
                case TODO, DEADLINE, EVENT -> addTask(Parser.parseTask(command));
                case UNKNOWN -> throw Parser.unknownCommand();
            };
        } catch (SheppyException exception) {
            tasks = new TaskList(previousTasks);
            for (int index = 0; index < previousTasks.size(); index++) {
                if (previousStatuses.get(index).equals("X")) {
                    previousTasks.get(index).markAsDone();
                } else {
                    previousTasks.get(index).markAsUndone();
                }
            }
            return formatError(exception.getMessage());
        }
    }

    /**
     * Checks whether a command tells Sheppy to exit.
     *
     * @param command the command entered by the user
     * @return true if the command is {@code bye}
     */
    public boolean isExitCommand(String command) {
        return Parser.parseCommand(command) == CommandType.BYE;
    }

    /** Adds a task, saves the updated list, and returns a confirmation. */
    private String addTask(Task task) throws SheppyException {
        tasks.add(task);
        storage.save(tasks);
        return "Got it. I've added this task:\n"
                + "  " + task + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /** Updates a task's completion status, saves it, and returns a confirmation. */
    private String updateTaskStatus(String command, boolean markDone) throws SheppyException {
        int taskNumber = Parser.parseTaskNumber(command);
        Task task = tasks.updateStatus(taskNumber, markDone);
        storage.save(tasks);
        String confirmation = markDone
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        return confirmation + "\n  " + task;
    }

    /** Deletes a task, saves the updated list, and returns a confirmation. */
    private String deleteTask(String command) throws SheppyException {
        int taskNumber = Parser.parseTaskNumber(command);
        Task deletedTask = tasks.remove(taskNumber);
        storage.save(tasks);
        return "Noted. I've removed this task:\n"
                + "  " + deletedTask + "\n"
                + "Now you have " + tasks.size() + " tasks in the list.";
    }

    /** Sorts tasks alphabetically, saves the new order, and returns the sorted list. */
    private String sortTasks() throws SheppyException {
        tasks.sortByDescription();
        storage.save(tasks);
        return formatTasks("All sorted! Here are your tasks in alphabetical order:", tasks.asList());
    }

    /** Formats a numbered collection of tasks under a heading. */
    private String formatTasks(String heading, List<Task> displayedTasks) {
        String lineSeparator = System.lineSeparator();
        String formattedTasks = IntStream.range(0, displayedTasks.size())
                .mapToObj(index -> (index + 1) + "." + displayedTasks.get(index))
                .collect(Collectors.joining(lineSeparator));
        return formattedTasks.isEmpty()
                ? heading
                : heading + lineSeparator + formattedTasks;
    }

    /** Formats an exception message using Sheppy's error prefix. */
    private String formatError(String message) {
        return "Baa-error: " + message;
    }
}
