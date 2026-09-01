package sheppy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import sheppy.SheppyException;

/** Tests adding, updating, retrieving, and removing tasks. */
class TaskListTest {
    /** Checks that tasks are added and retrieved using one-based numbering. */
    @Test
    void addAndGet_usesOneBasedTaskNumbers() throws SheppyException {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertEquals(todo, tasks.get(1));
    }

    /** Checks that a task can be marked done and then marked undone. */
    @Test
    void updateStatus_changesCompletionMarker() throws SheppyException {
        TaskList tasks = new TaskList(new Todo("read book"));

        tasks.updateStatus(1, true);
        assertEquals("X", tasks.get(1).getStatusIcon());

        tasks.updateStatus(1, false);
        assertEquals(" ", tasks.get(1).getStatusIcon());
    }

    /** Checks that removing a task returns it and reduces the list size. */
    @Test
    void remove_returnsTaskAndReducesSize() throws SheppyException {
        TaskList tasks = new TaskList(new Todo("read book"), new Todo("return book"));

        Task removed = tasks.remove(1);

        assertEquals("read book", removed.getDescription());
        assertEquals(1, tasks.size());
        assertEquals("return book", tasks.get(1).getDescription());
    }

    /** Checks that task numbers outside the list are rejected. */
    @Test
    void get_outOfRangeNumberThrowsSheppyException() throws SheppyException {
        TaskList tasks = new TaskList(new Todo("read book"));

        assertThrows(SheppyException.class, () -> tasks.get(0));
        assertThrows(SheppyException.class, () -> tasks.get(2));
    }

    /** Checks that find returns matching tasks in their original order. */
    @Test
    void find_keywordReturnsMatchingTasks() throws SheppyException {
        TaskList tasks = new TaskList(new Todo("read book"), new Todo("exercise"),
                new Todo("return book"));

        List<Task> matchingTasks = tasks.find("book");

        assertEquals(List.of(tasks.get(1), tasks.get(3)), matchingTasks);
    }
}
