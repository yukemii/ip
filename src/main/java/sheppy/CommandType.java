package sheppy;

/** The commands understood by Sheppy. */
public enum CommandType {
    /** Exit the application. */
    BYE,

    /** Display all tasks. */
    LIST,

    /** Add a todo task. */
    TODO,

    /** Add a deadline task. */
    DEADLINE,

    /** Add an event task. */
    EVENT,

    /** Mark a task as done. */
    MARK,

    /** Mark a task as not done. */
    UNMARK,

    /** Delete a task. */
    DELETE,

    /** An unrecognized command. */
    UNKNOWN
}
