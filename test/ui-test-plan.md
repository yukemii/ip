# Sheppy UI test plan

## Welcome banner

- Aim: display a readable ASCII banner for Sheppy when the application starts.
- Expected: the banner clearly displays S H E P P Y and the chatbot's sheep
  personality.

The cases in `test/ui-test-cases.json` are run with the project-specific
`.codex/skills/test-ui/scripts/run_ui_tests.py` script.

## Level 4 task types

- Aim: add and display a todo, deadline, and event.
- Input: `todo borrow book`, `deadline submit report /by 2019-06-06`,
  `event project meeting /from Monday 2pm /to 4pm`, `list`, `bye`.
- Expected: `[T][ ] borrow book`, `[D][ ] submit report (by: Jun 06 2019)`,
  and `[E][ ] project meeting (from: Monday 2pm to: 4pm)`.

## Completion status

- Aim: mark and unmark a typed task.
- Input: `todo read book`, `mark 1`, `list`, `unmark 1`, `list`, `bye`.
- Expected: `[T][X] read book`, then `[T][ ] read book`.

## Error handling

- Aim: reject empty descriptions, unknown commands, malformed date commands,
  and invalid task numbers without changing the task list.
- Input: `todo`, `todo read book`, `blah`, `deadline report`, `event meeting`,
  `mark x`, `list`, `bye`.
- Expected: specific `Baa-error:` messages, followed by a list containing only
  `read book`.

## Delete tasks

- Aim: remove a task and confirm that the remaining tasks are renumbered.
- Input: add three tasks, mark the second task, delete the first task, list,
  and attempt `delete x`.
- Expected: the removed task is reported with its current status, the former
  second task becomes task 1, and the invalid delete produces a `Baa-error:`.

## Level 7 saving

- Aim: save every task-list change to the relative `data/tasks.txt` file.
- Input: add a todo, mark it done, then delete it.
- Expected: normal command output, with the saved file ending with zero task
  lines after the deletion. Loading the file is intentionally not tested yet.

## Level 7 loading

- Aim: restore saved tasks and their completion status when Sheppy starts.
- Setup: save todo, deadline, and event tasks in one run, then start Sheppy
  again and use `list`.
- Expected: all three task types and their saved done status are displayed.
- Missing-file case: starting without `data/tasks.txt` starts with an empty
  task list rather than crashing.

## Level 7 corrupted data

- Aim: report malformed saved data and start with an empty list instead of
  crashing.
- Fixtures: unknown task type, invalid completion status, and incomplete task
  fields.
- Expected: a specific `Baa-error:` message followed by an empty task list.

## Level 8 typed deadline dates

- Aim: parse deadline dates as `LocalDate` rather than plain strings.
- Input: `deadline submit report /by 2019-10-15`, `list`, `bye`.
- Expected: the deadline is displayed as `Oct 15 2019`.
- Invalid input: dates such as `October 15` are rejected with guidance to use
  `yyyy-MM-dd`.

## Level 9 find

- Aim: find tasks whose descriptions contain a keyword.
- Input: add tasks containing and not containing `book`, then run `find book`.
- Expected: the matching tasks are displayed under the matching-tasks heading
  in their original order.

## Level 10 JavaFX GUI

- Aim: provide a graphical interface without changing Sheppy's command
  behavior or removing the command-line interface used by scripted tests.
- Launch: run `./gradlew run` and confirm that a window titled `Sheppy` opens.
- Input: enter commands using both the Enter key and the Send button.
- Expected: each user command and Sheppy response appears in a separate,
  readable dialogue box; the newest exchange remains visible as the dialogue
  grows; all existing commands still produce their normal responses.
- Exit: enter `bye` and confirm that Sheppy shows its farewell before the
  window closes.
- Regression: run every scripted text-UI case to verify that the reusable
  chatbot logic still supports the command-line interface.
