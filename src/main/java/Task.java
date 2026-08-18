/** Represents a task in Sheppy's task list. */
public class Task {
    /** The text describing this task. */
    private final String description;

    /** Whether this task has been completed. */
    private boolean isDone;

    /**
     * Creates an unfinished task.
     *
     * @param description the text describing the task
     */
    public Task(String description) throws SheppyException {
        if (description == null || description.isBlank()) {
            throw new SheppyException("a task description cannot be empty.");
        }
        this.description = description;
        this.isDone = false;
    }

    /** Marks this task as done. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsUndone() {
        isDone = false;
    }

    /**
     * Returns the status marker used when displaying this task.
     *
     * @return {@code X} for a completed task, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the task description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type of this task.
     *
     * @return this task's type
     */
    public TaskType getTaskType() {
        return TaskType.TODO;
    }

    /**
     * Returns the task's complete display description.
     *
     * @return the display description
     */
    public String getDisplayDescription() {
        return description;
    }

    /**
     * Returns this task in the format used by Level 7 file storage.
     *
     * @return the serialized task
     */
    public String toStorageString() {
        return getTaskType().getIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the task in the format used by the user interface.
     *
     * @return the formatted task
     */
    @Override
    public String toString() {
        return "[" + getTaskType().getIcon() + "][" + getStatusIcon() + "] "
                + getDisplayDescription();
    }
}
