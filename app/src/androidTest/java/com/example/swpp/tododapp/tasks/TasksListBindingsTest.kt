package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class TasksListBindingsTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    private fun newTextView(text: String = "", visibility: Int = View.GONE): TextView {
        lateinit var textView: TextView
        instrumentation.runOnMainSync {
            textView = TextView(instrumentation.targetContext).apply {
                this.text = text
                this.visibility = visibility
            }
        }
        return textView
    }

    private fun bindAndAssert(
        task: Task?,
        expectedText: String,
        expectedVisibility: Int,
        initialText: String = "",
        initialVisibility: Int = View.GONE
    ) {
        val textView = newTextView(initialText, initialVisibility)
        instrumentation.runOnMainSync {
            setDueDateLabel(textView, task)
            assertEquals(expectedText, textView.text.toString())
            assertEquals(expectedVisibility, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_showsClear() {
        // Given a completed task with a due date
        val task = Task(title = "Task", description = "Description", isCompleted = true,
            dueDate = LocalDate.now().plusDays(1))

        // When the task is bound to its label TextView
        // Then completed status is visible as CLEAR
        bindAndAssert(task, "CLEAR", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_clearsAndHidesLabel() {
        // Given an active task without a due date and a recycled row showing D-3
        val task = Task(title = "Task", description = "Description", isCompleted = false)

        // When the task is bound to the recycled TextView
        // Then no old label remains and the label is hidden
        bindAndAssert(task, "", View.GONE, initialText = "D-3", initialVisibility = View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeDueToday_showsDDay() {
        // Given an active task due on the current date
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now())

        // When the task is bound to its label TextView
        // Then the label is D-Day and visible
        bindAndAssert(task, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeDueInThreeDays_showsDMinusThree() {
        // Given an active task due three days from the current date
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now().plusDays(3))

        // When the task is bound to its label TextView
        // Then the label is D-3 and visible
        bindAndAssert(task, "D-3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_activeOverdueThreeDays_showsDPlusThree() {
        // Given an active task due three days before the current date
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now().minusDays(3))

        // When the task is bound to its label TextView
        // Then the label is D+3 and visible
        bindAndAssert(task, "D+3", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_missingTask_clearsAndHidesLabel() {
        // Given no task and a recycled row showing D-3
        // When the null task is bound to the TextView
        // Then no error occurs and the old label is cleared and hidden
        bindAndAssert(null, "", View.GONE, initialText = "D-3", initialVisibility = View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_checkingTaskWithDueDate_changesDMinusThreeToClear() {
        // Given an active task due three days from now, initially showing D-3
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now().plusDays(3))
        val textView = newTextView()

        // When it is bound, marked completed, and bound again
        instrumentation.runOnMainSync {
            setDueDateLabel(textView, task)
            assertEquals("D-3", textView.text.toString())
            task.isCompleted = true
            setDueDateLabel(textView, task)

            // Then the updated label is CLEAR
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_uncheckingTaskWithDueDate_restoresDMinusThree() {
        // Given a completed task due three days from now, initially showing CLEAR
        val task = Task(title = "Task", description = "Description", isCompleted = true,
            dueDate = LocalDate.now().plusDays(3))
        val textView = newTextView()

        // When it is bound, marked active, and bound again
        instrumentation.runOnMainSync {
            setDueDateLabel(textView, task)
            assertEquals("CLEAR", textView.text.toString())
            task.isCompleted = false
            setDueDateLabel(textView, task)

            // Then the due-date label is restored
            assertEquals("D-3", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_checkingTaskWithoutDueDate_showsClear() {
        // Given an active task without a due date, initially showing no label
        val task = Task(title = "Task", description = "Description", isCompleted = false)
        val textView = newTextView()

        // When it is bound, marked completed, and bound again
        instrumentation.runOnMainSync {
            setDueDateLabel(textView, task)
            task.isCompleted = true
            setDueDateLabel(textView, task)

            // Then completed status is visible as CLEAR
            assertEquals("CLEAR", textView.text.toString())
            assertEquals(View.VISIBLE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_uncheckingTaskWithoutDueDate_clearsAndHidesLabel() {
        // Given a completed task without a due date, initially showing CLEAR
        val task = Task(title = "Task", description = "Description", isCompleted = true)
        val textView = newTextView()

        // When it is bound, marked active, and bound again
        instrumentation.runOnMainSync {
            setDueDateLabel(textView, task)
            assertEquals("CLEAR", textView.text.toString())
            task.isCompleted = false
            setDueDateLabel(textView, task)

            // Then there is no label and the TextView is hidden
            assertEquals("", textView.text.toString())
            assertEquals(View.GONE, textView.visibility)
        }
    }

    @Test
    fun setDueDateLabel_nullTask_doesNotThrowAndClearsLabel() {
        // Given a valid TextView currently showing D-3
        // When setDueDateLabel receives a null task
        // Then it does not throw and clears and hides the label
        bindAndAssert(null, "", View.GONE, initialText = "D-3", initialVisibility = View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_taskDueYesterday_showsDPlusOne() {
        // Given an active task due one day before today
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now().minusDays(1))

        // When the task is bound to its label TextView
        // Then the label is D+1 and visible
        bindAndAssert(task, "D+1", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_taskDueToday_showsDDayAtBoundary() {
        // Given an active task due today, at the boundary between overdue and upcoming
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now())

        // When the task is bound to its label TextView
        // Then the label is D-Day and visible
        bindAndAssert(task, "D-Day", View.VISIBLE)
    }

    @Test
    fun setDueDateLabel_taskDueTomorrow_showsDMinusOne() {
        // Given an active task due one day after today
        val task = Task(title = "Task", description = "Description", isCompleted = false,
            dueDate = LocalDate.now().plusDays(1))

        // When the task is bound to its label TextView
        // Then the label is D-1 and visible
        bindAndAssert(task, "D-1", View.VISIBLE)
    }
}
