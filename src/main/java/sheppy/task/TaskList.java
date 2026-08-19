package sheppy.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sheppy.SheppyException;

/** Owns Sheppy's collection of tasks and its task-level operations. */
public class TaskList {
    /** The tasks currently tracked by Sheppy. */
    private final List<Task> tasks;

    /**
     * Creates a task list using the supplied initial tasks.
     *
     * @param initialTasks tasks loaded from storage, if any
     */
    public TaskList(List<Task> initialTasks) {
        tasks = new ArrayList<>(initialTasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns a task using the one-based number shown to the user.
     *
     * @param taskNumber the number shown in the task list
     * @return the selected task
     * @throws SheppyException if the number is outside the list
     */
    public Task get(int taskNumber) throws SheppyException {
        return tasks.get(toIndex(taskNumber));
    }

    /**
     * Marks a task done or undone.
     *
     * @param taskNumber the one-based task number
     * @param markDone whether the task should be marked done
     * @return the updated task
     * @throws SheppyException if the number is outside the list
     */
    public Task updateStatus(int taskNumber, boolean markDone) throws SheppyException {
        Task task = get(taskNumber);
        if (markDone) {
            task.markAsDone();
        } else {
            task.markAsUndone();
        }
        return task;
    }

    /**
     * Removes a task using the one-based number shown to the user.
     *
     * @param taskNumber the one-based task number
     * @return the removed task
     * @throws SheppyException if the number is outside the list
     */
    public Task remove(int taskNumber) throws SheppyException {
        return tasks.remove(toIndex(taskNumber));
    }

    /** @return the number of tasks in the list */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a read-only view for display and storage.
     *
     * @return the current tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /** Converts a user-facing one-based number into an internal index. */
    private int toIndex(int taskNumber) throws SheppyException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new SheppyException("that task number is not in your list.");
        }
        return taskNumber - 1;
    }
}
