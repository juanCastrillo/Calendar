package juan.calendar.widgets

import androidx.compose.ui.text.intl.Locale
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.text.format

data class TimeRange(val startTime: LocalTime, val endTime: LocalTime) {
    override fun toString():String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return "${startTime.format(formatter)}-${endTime.format(formatter)}"
    }
}

/**
 * currentDay: Int, currentMonth: String, calendarMatrix:Array<IntArray>
 */
fun getCurrentMonthValues(): Triple<Int, String, Array<IntArray>> {
    val today = LocalDate.now()

    val firstWeekday = today.withDayOfMonth(1).dayOfWeek.value // Monday:1, Sunday:7
    val numberOfDays = today.lengthOfMonth() // Number of days of the current month

    val currentDay = today.dayOfMonth
    val currentMonth = today.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.current.platformLocale)

    return Triple(currentDay, currentMonth, constructCalendarMatrix(firstWeekday, numberOfDays))
}

private fun constructCalendarMatrix(firstWeekday:Int, numberOfDays:Int): Array<IntArray> {
    val nWeeks = 6; val nDaysWeek = 7
    val calendarMatrix = Array(nWeeks) { IntArray(nDaysWeek) }
    var tempDay = 1
    for(nWeek in 0 until nWeeks) {
        for(nDay in 0 until nDaysWeek) {
            if ((nWeek == 0 && nDay < firstWeekday-1) || tempDay > numberOfDays) { continue }
            else {
                calendarMatrix[nWeek][nDay] = tempDay
                tempDay += 1
            }
        }
    }
    return calendarMatrix
}