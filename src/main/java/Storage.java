import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Handles saving and loading Sheppy's tasks from a data file. */
public class Storage {
    /** The path of the file used to store tasks. */
    private final Path filePath;

    /**
     * Creates storage for a relative or absolute path.
     *
     * @param filePath the path of the task data file
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Saves the current task list.
     *
     * @param tasks the tasks to save
     * @throws SheppyException if the directory or file cannot be written
     */
    public void save(TaskList tasks) throws SheppyException {
        try {
            Files.createDirectories(filePath.getParent());
            List<String> lines = tasks.asList().stream()
                    .map(Task::toStorageString)
                    .toList();
            Files.write(filePath, lines);
        } catch (IOException exception) {
            throw new SheppyException("I couldn't save your tasks: " + exception.getMessage());
        }
    }

    /**
     * Loads tasks from the data file.
     *
     * @return the loaded tasks, or an empty task list if the file is absent
     * @throws SheppyException if the file cannot be read or contains invalid data
     */
    public TaskList load() throws SheppyException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return new TaskList(tasks);
        }

        try {
            for (String line : Files.readAllLines(filePath)) {
                if (!line.isBlank()) {
                    tasks.add(parseStoredTask(line));
                }
            }
        } catch (IOException exception) {
            throw new SheppyException("I couldn't load your tasks: " + exception.getMessage());
        }
        return new TaskList(tasks);
    }

    /** Parses one task line from the storage format. */
    private Task parseStoredTask(String line) throws SheppyException {
        String[] fields = line.split("\\s*\\|\\s*", -1);
        if (fields.length < 3) {
            throw new SheppyException("your task file contains an invalid line: " + line);
        }

        String type = fields[0];
        String status = fields[1];
        String description = fields[2];
        Task task = switch (type) {
        case "T" -> {
            requireFieldCount(fields, 3);
            yield new Todo(description);
        }
        case "D" -> {
            requireFieldCount(fields, 4);
            yield new Deadline(description, parseDate(fields[3]));
        }
        case "E" -> {
            requireFieldCount(fields, 5);
            yield new Event(description, fields[3], fields[4]);
        }
        default -> throw new SheppyException("your task file contains an unknown task type: " + type);
        };

        if (status.equals("1")) {
            task.markAsDone();
        } else if (!status.equals("0")) {
            throw new SheppyException("your task file contains an invalid status: " + status);
        }
        return task;
    }

    /** Checks that a stored task has exactly the expected number of fields. */
    private void requireFieldCount(String[] fields, int expected) throws SheppyException {
        if (fields.length != expected) {
            throw new SheppyException("your task file contains the wrong number of fields.");
        }
    }

    /** Parses a stored date in the Level 8 ISO format. */
    private LocalDate parseDate(String value) throws SheppyException {
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new SheppyException("please use dates in yyyy-MM-dd format, such as 2019-10-15.");
        }
    }
}
