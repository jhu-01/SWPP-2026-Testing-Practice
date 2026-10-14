package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {
    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // Given a completed task with a due date
        val dueDate = LocalDate.parse("2026-10-15")
        val today = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, true)

        // Then completed status takes precedence over the due date
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // Given a completed task without a due date
        val today = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(null, today, true)

        // Then completed status still produces CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // Given an active task without a due date
        val today = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(null, today, false)

        // Then no label is returned
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        // Given an active task due today
        val date = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(date, date, false)

        // Then the label marks today
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeDueInThreeDays_returnsDMinusThree() {
        // Given an active task due three days later
        val today = LocalDate.parse("2026-10-14")
        val dueDate = LocalDate.parse("2026-10-17")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the label shows three days remaining
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeOverdueThreeDays_returnsDPlusThree() {
        // Given an active task overdue by three days
        val today = LocalDate.parse("2026-10-14")
        val dueDate = LocalDate.parse("2026-10-11")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the label shows three overdue days
        assertEquals("D+3", label)
    }

    @Test
    fun dueDateLabel_activeAcrossMonthBoundary_countsCalendarDays() {
        // Given an active task due three calendar days after October 30
        val today = LocalDate.parse("2026-10-30")
        val dueDate = LocalDate.parse("2026-11-02")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the label counts all three calendar days
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeAcrossYearBoundary_countsCalendarDays() {
        // Given an active task due three calendar days after December 30
        val today = LocalDate.parse("2026-12-30")
        val dueDate = LocalDate.parse("2027-01-02")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the label counts all three calendar days
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeAcrossLeapDay_countsFebruary29() {
        // Given an active task due after February 29 in leap year 2024
        val today = LocalDate.parse("2024-02-28")
        val dueDate = LocalDate.parse("2024-03-01")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then February 29 is included in the two-day distance
        assertEquals("D-2", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_doesNotThrowAndReturnsEmpty() {
        // Given a valid reference date and no due date
        val today = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(null, today, false)

        // Then the call succeeds with no label
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_dueYesterday_returnsDPlusOne() {
        // Given a due date one day before today
        val today = LocalDate.parse("2026-10-14")
        val dueDate = LocalDate.parse("2026-10-13")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then it is one day overdue
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_dueToday_returnsDDayAtBoundary() {
        // Given a due date equal to today
        val date = LocalDate.parse("2026-10-14")

        // When the due-date label is computed
        val label = dueDateLabel(date, date, false)

        // Then it is the boundary label
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_dueTomorrow_returnsDMinusOne() {
        // Given a due date one day after today
        val today = LocalDate.parse("2026-10-14")
        val dueDate = LocalDate.parse("2026-10-15")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then one day remains
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_monthStartsTomorrow_returnsDMinusOne() {
        // Given October 31 and a due date on November 1
        val today = LocalDate.parse("2026-10-31")
        val dueDate = LocalDate.parse("2026-11-01")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the due date is one day away
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_monthEndedYesterday_returnsDPlusOne() {
        // Given November 1 and a due date on October 31
        val today = LocalDate.parse("2026-11-01")
        val dueDate = LocalDate.parse("2026-10-31")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the due date was one day ago
        assertEquals("D+1", label)
    }

    @Test
    fun dueDateLabel_newYearTomorrow_returnsDMinusOne() {
        // Given December 31 and a due date on January 1
        val today = LocalDate.parse("2026-12-31")
        val dueDate = LocalDate.parse("2027-01-01")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the due date is one day away
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_newYearStartedYesterday_returnsDPlusOne() {
        // Given January 1 and a due date on December 31
        val today = LocalDate.parse("2027-01-01")
        val dueDate = LocalDate.parse("2026-12-31")

        // When the due-date label is computed
        val label = dueDateLabel(dueDate, today, false)

        // Then the due date was one day ago
        assertEquals("D+1", label)
    }
}
