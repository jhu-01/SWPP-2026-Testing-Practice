# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the feature's D-day label for a task. An active task due in N calendar days shows `D-N`, one due today shows `D-Day`, an overdue task shows `D+N`, and an active task without a due date shows no label. A completed task shows `CLEAR` with or without a due date.

**Pre-conditions**
- `today` is a valid `LocalDate`; `dueDate` is either null or a valid `LocalDate`.

**Post-conditions**
- Success cases
  - A completed task returns `CLEAR`, whether it has a due date or not. `[Unit]`
    - Tip: Given dueDate `2026-10-15`, today `2026-10-14`, and isCompleted `true` / When dueDateLabel is called / Then the result is `CLEAR`.
  - A completed task with no due date also returns `CLEAR`. `[Unit]`
    - Tip: Given dueDate `null`, today `2026-10-14`, and isCompleted `true` / When dueDateLabel is called / Then the result is `CLEAR`.
  - An active task with no due date returns an empty string. `[Unit]`
    - Tip: Given dueDate `null`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `""`.
  - An active task due today returns `D-Day`. `[Unit]`
    - Tip: Given dueDate `2026-10-14`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-Day`.
  - An active task due 3 calendar days later returns `D-3`. `[Unit]`
    - Tip: Given dueDate `2026-10-17`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-3`.
  - An active task overdue by 3 calendar days returns `D+3`. `[Unit]`
    - Tip: Given dueDate `2026-10-11`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D+3`.
  - Calendar-day distances remain correct across a month boundary. `[Unit]`
    - Tip: Given dueDate `2026-11-02`, today `2026-10-30`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-3`.
  - Calendar-day distances remain correct across a year boundary. `[Unit]`
    - Tip: Given dueDate `2027-01-02`, today `2026-12-30`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-3`.
  - Calendar-day distances count February 29 in a leap year. `[Unit]`
    - Tip: Given dueDate `2024-03-01`, today `2024-02-28`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-2`.
- Error cases
  - None. `[Unit]`
    - Tip: Given dueDate `null`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `""` and no error is thrown.
- Edge cases
  - The day immediately before the reference date returns `D+1`. `[Unit]`
    - Tip: Given dueDate `2026-10-13`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D+1`.
  - The reference date returns `D-Day`, at the boundary between overdue and upcoming dates. `[Unit]`
    - Tip: Given dueDate `2026-10-14`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-Day`.
  - The day immediately after the reference date returns `D-1`. `[Unit]`
    - Tip: Given dueDate `2026-10-15`, today `2026-10-14`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-1`.
  - The first day of a new month is one day after the prior month's last day. `[Unit]`
    - Tip: Given dueDate `2026-11-01`, today `2026-10-31`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-1`.
  - The last day of the prior month is one day before the first day of the current month. `[Unit]`
    - Tip: Given dueDate `2026-10-31`, today `2026-11-01`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D+1`.
  - New Year's Day is one day after December 31. `[Unit]`
    - Tip: Given dueDate `2027-01-01`, today `2026-12-31`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D-1`.
  - December 31 is one day before New Year's Day. `[Unit]`
    - Tip: Given dueDate `2026-12-31`, today `2027-01-01`, and isCompleted `false` / When dueDateLabel is called / Then the result is `D+1`.

**Undefined behavior** (optional)
- None.
