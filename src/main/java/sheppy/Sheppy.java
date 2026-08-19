package sheppy;

import java.util.List;

import sheppy.storage.Storage;
import sheppy.task.Task;
import sheppy.task.TaskList;

/** Coordinates Sheppy's user interface, parser, task list, and storage. */
public class Sheppy {
    /** Prevents instantiation of this command-line application class. */
    private Sheppy() {
    }

    /**
     * Starts Sheppy's command loop.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data/tasks.txt");
        TaskList tasks;
        try {
            tasks = storage.load();
        } catch (SheppyException exception) {
            ui.showError(exception.getMessage());
            tasks = new TaskList(List.of());
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            try {
                CommandType commandType = Parser.parseCommand(command);
                if (commandType == CommandType.BYE) {
                    ui.showBye();
                    return;
                } else if (commandType == CommandType.LIST) {
                    ui.showTasks(tasks);
                } else if (commandType == CommandType.MARK) {
                    updateTaskStatus(command, tasks, storage, ui, true);
                } else if (commandType == CommandType.UNMARK) {
                    updateTaskStatus(command, tasks, storage, ui, false);
                } else if (commandType == CommandType.DELETE) {
                    deleteTask(command, tasks, storage, ui);
                } else if (commandType == CommandType.TODO
                        || commandType == CommandType.DEADLINE
                        || commandType == CommandType.EVENT) {
                    addTask(Parser.parseTask(command), tasks, storage, ui);
                } else {
                    throw Parser.unknownCommand();
                }
            } catch (SheppyException exception) {
                ui.showError(exception.getMessage());
            }
        }
    }

    /** Adds a task, saves the updated list, and displays a confirmation. */
    private static void addTask(Task task, TaskList tasks, Storage storage, Ui ui)
            throws SheppyException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(task, tasks.size());
    }

    /** Updates a task's completion status, saves it, and displays a confirmation. */
    private static void updateTaskStatus(String command, TaskList tasks, Storage storage,
                                         Ui ui, boolean markDone) throws SheppyException {
        int taskNumber = Parser.parseTaskNumber(command);
        Task task = tasks.updateStatus(taskNumber, markDone);
        storage.save(tasks);
        ui.showTaskStatus(task, markDone);
    }

    /** Deletes a task, saves the updated list, and displays a confirmation. */
    private static void deleteTask(String command, TaskList tasks, Storage storage, Ui ui)
            throws SheppyException {
        int taskNumber = Parser.parseTaskNumber(command);
        Task deletedTask = tasks.remove(taskNumber);
        storage.save(tasks);
        ui.showTaskDeleted(deletedTask, tasks.size());
    }
}
