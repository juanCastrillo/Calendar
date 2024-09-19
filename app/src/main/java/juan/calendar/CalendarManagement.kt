package juan.calendar

import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import androidx.compose.foundation.layout.add
import juan.calendar.widgets.TimeRange
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


fun getCalendarEvents(context: Context, startTime: LocalDateTime, endTime: LocalDateTime): List<CalendarEvent> {
    val events = mutableListOf<CalendarEvent>()
    val projection = arrayOf(
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND
    )
    val startMillis = startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val endMillis = endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

//    val selection = "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTEND} <= ?"
    val selection = "${CalendarContract.Events.DTEND} >= ? AND ${CalendarContract.Events.DTEND} <= ?"
    val selectionArgs = arrayOf(startMillis.toString(), endMillis.toString())
    val cursor = context.contentResolver.query(
        CalendarContract.Events.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        null
    )
    cursor?.use {
        while (it.moveToNext()) {
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val title = if (titleIndex >= 0) it.getString(titleIndex) else ""

            val startIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val startTime = if (startIndex >= 0) {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it.getLong(startIndex)), ZoneId.systemDefault())
            } else {LocalDateTime.now()}

            val endIndex = it.getColumnIndex(CalendarContract.Events.DTEND)
            val endTime = if (endIndex >= 0) {
                LocalDateTime.ofInstant(Instant.ofEpochMilli(it.getLong(endIndex)), ZoneId.systemDefault())
            } else {LocalDateTime.now()}

            events.add(
                CalendarEvent(
                    title,
                    TimeRange(startTime.toLocalTime() , endTime.toLocalTime()),
                    date=startTime.toLocalDate()
                )
            )
        }
    }
    return events
}

data class CalendarEvent(
    val title: String,
    val timeRange: TimeRange,
    val date: LocalDate = LocalDate.now()
)

fun getTodayRemainingEvents(context:Context): List<CalendarEvent> {
    val nowToday = LocalDateTime.now()
    val endToday = nowToday.withHour(23).withMinute(59).withSecond(59)
    val events = getCalendarEvents(context, nowToday, endToday)

    Log.d("EVENTS_CalendarManagement", "Fetching Events ...")
    for (event in events) {
        Log.d("EVENT_CalendarManagement", event.title)
    }

    return events
}