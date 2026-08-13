/** Represents a task that takes place over a specified time range. */
public class Event extends Task {
    /** The starting date or time of the event. */
    private final String from;

    /** The ending date or time of the event. */
    private final String to;

    /**
     * Creates an event task.
     *
     * @param description the text describing the event
     * @param from the event's starting date or time
     * @param to the event's ending date or time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String getDisplayDescription() {
        return super.getDisplayDescription() + " (from: " + from + " to: " + to + ")";
    }
}
