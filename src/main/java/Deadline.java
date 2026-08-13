/** Represents a task that must be completed by a specified date or time. */
public class Deadline extends Task {
    /** The date or time by which the task should be completed. */
    private final String by;

    /**
     * Creates a deadline task.
     *
     * @param description the text describing the task
     * @param by the date or time by which it should be completed
     */
    public Deadline(String description, String by) throws SheppyException {
        super(description);
        if (by == null || by.isBlank()) {
            throw new SheppyException("a deadline needs a date or time after /by.");
        }
        this.by = by;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.DEADLINE;
    }

    @Override
    public String getDisplayDescription() {
        return super.getDisplayDescription() + " (by: " + by + ")";
    }
}
