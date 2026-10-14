# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?)`

**Intent**: Apply the feature's D-day label to a task row. Active tasks show `D-N`, `D-Day`, or `D+N` based on calendar days; active tasks without a due date show no label; completed tasks show `CLEAR` with or without a due date. Rebinding after checking or unchecking completion updates the label to match the new state.

**Pre-conditions**
- `textView` is a valid `TextView`.
- `task` is null or has a valid due date and completion state.

**Post-conditions**
- Success cases
  - A completed task displays `CLEAR` and the label is visible, regardless of its due date. `[Integration]`
    - Tip: Given a task with dueDate `2026-10-15`, isCompleted `true`, and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `CLEAR` and its visibility is `View.VISIBLE`.
  - An active task with no due date shows no label. `[Integration]`
    - Tip: Given an active task with dueDate `null`, an initially visible TextView with text `D-3`, and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is empty and its visibility is `View.GONE`.
  - An active task due today displays `D-Day` and the label is visible. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-14` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D-Day` and its visibility is `View.VISIBLE`.
  - An active task due 3 calendar days later displays `D-3` and the label is visible. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-17` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D-3` and its visibility is `View.VISIBLE`.
  - An active task overdue by 3 calendar days displays `D+3` and the label is visible. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-11` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D+3` and its visibility is `View.VISIBLE`.
  - A missing task shows no label. `[Integration]`
    - Tip: Given task `null`, an initially visible TextView with text `D-3`, and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is empty and its visibility is `View.GONE`.
  - Rebinding after checking completion changes the due-date label to `CLEAR`. `[Integration]`
    - Tip: Given an active task due `2026-10-17` with current date `2026-10-14`, so the first binding shows `D-3` / When the task is marked completed and setDueDateLabel is called again with that task / Then the TextView text is `CLEAR` and its visibility is `View.VISIBLE`.
  - Rebinding after unchecking completion restores the due-date label. `[Integration]`
    - Tip: Given a completed task due `2026-10-17` with current date `2026-10-14`, so the first binding shows `CLEAR` / When the task is marked active and setDueDateLabel is called again with that task / Then the TextView text is `D-3` and its visibility is `View.VISIBLE`.
  - Checking a task with no due date changes the label to `CLEAR`. `[Integration]`
    - Tip: Given an active task with dueDate `null` and current date `2026-10-14`, so the first binding shows no label / When the task is marked completed and setDueDateLabel is called again with that task / Then the TextView text is `CLEAR` and its visibility is `View.VISIBLE`.
  - Unchecking a completed task with no due date removes the `CLEAR` label. `[Integration]`
    - Tip: Given a completed task with dueDate `null` and current date `2026-10-14`, so the first binding shows `CLEAR` / When the task is marked active and setDueDateLabel is called again with that task / Then the TextView text is empty and its visibility is `View.GONE`.
- Error cases
  - None. `[Integration]`
    - Tip: Given task `null`, a valid TextView, and current text `D-3` / When setDueDateLabel is called / Then no error is thrown, the TextView text is empty, and its visibility is `View.GONE`.
- Edge cases
  - The day immediately before the current date displays `D+1` and is visible. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-13` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D+1` and its visibility is `View.VISIBLE`.
  - A task due on the current date displays `D-Day` at the boundary between overdue and upcoming dates. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-14` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D-Day` and its visibility is `View.VISIBLE`.
  - The day immediately after the current date displays `D-1` and is visible. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-15` and the current date `2026-10-14` / When setDueDateLabel is called / Then the TextView text is `D-1` and its visibility is `View.VISIBLE`.
  - Crossing from the last day of a month to the first day of the next month displays `D-1`. `[Integration]`
    - Tip: Given an active task with dueDate `2026-11-01` and a test clock set to `2026-10-31` / When setDueDateLabel is called / Then the TextView text is `D-1` and its visibility is `View.VISIBLE`.
  - Crossing backward from the first day of a month to the prior month's last day displays `D+1`. `[Integration]`
    - Tip: Given an active task with dueDate `2026-10-31` and a test clock set to `2026-11-01` / When setDueDateLabel is called / Then the TextView text is `D+1` and its visibility is `View.VISIBLE`.
  - Crossing from December 31 to New Year's Day displays `D-1`. `[Integration]`
    - Tip: Given an active task with dueDate `2027-01-01` and a test clock set to `2026-12-31` / When setDueDateLabel is called / Then the TextView text is `D-1` and its visibility is `View.VISIBLE`.
  - Crossing backward from New Year's Day to December 31 displays `D+1`. `[Integration]`
    - Tip: Given an active task with dueDate `2026-12-31` and a test clock set to `2027-01-01` / When setDueDateLabel is called / Then the TextView text is `D+1` and its visibility is `View.VISIBLE`.

**Undefined behavior** (optional)
- None.
