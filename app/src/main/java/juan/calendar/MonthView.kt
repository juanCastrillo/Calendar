package juan.calendar

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.GlanceTheme.colors
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.Dimension
import java.time.LocalDate

// TODO - Use dynamic theme color
// TODO - Set sizes dynamically depending on widget size
class MonthViewWidgetReceiver : GlanceAppWidgetReceiver() {

    // Let MyAppWidgetReceiver know which GlanceAppWidget to use
    override val glanceAppWidget: GlanceAppWidget = MonthViewWidget()
}

/**
 * Implementation of App Widget functionality.
 */

class MonthViewWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = LocalDate.now()

        // Which weekday is the 1st of the current month (What weekday the current month starts at)
        // DayOfWeek.MONDAY
        val firstWeekday = today.withDayOfMonth(1).dayOfWeek.value // Monday:1, Sunday:7
        val numberOfDays = today.lengthOfMonth() // Number of days of the current month
        val currentDay = today.dayOfMonth
        // val numberOfDaysPrev = today.minusMonths(1).lengthOfMonth()
        val currentMonth = today.month.getDisplayName(java.time.format.TextStyle.FULL, Locale.current.platformLocale)

        // Display the current date.
        Log.d("DATESSSSS", "Current date: $today, firstWeekDay: $firstWeekday")

        val calendarMatrix = constructCalendarMatrix(firstWeekday, numberOfDays)
        provideContent {
            CalendarView(currentDay, currentMonth, calendarMatrix)
        }
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

    @Composable
    fun CalendarView(currentDay: Int, currentMonth: String, calendarMatrix:Array<IntArray>) {
        GlanceTheme(
        ) {

            Column(
                modifier = GlanceModifier.fillMaxSize().background(colors.background).padding(
                    Dp(8f)
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = GlanceModifier.fillMaxWidth()
//                        .padding(horizontal = Dp(16f)),
//                    horizontalAlignment = Alignment.Start
                ) {
                    Box(modifier = GlanceModifier.width(16.dp)){}
                    MonthText(currentMonth.toUpperCase(Locale.current))
                }
                WeekDaysRow() // L M X ...
                for (week in calendarMatrix) {
                    WeekNumbersRow(week, currentDay)
                }
            }
        }
    }

    @Composable
    fun MonthText(monthName: String) {
        Text(monthName.toUpperCase(Locale.current),

            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = colors.primary,
            )
        )
    }

    /**
     * numberStart: First number to draw
     * dayStart: Position to start drawing
     * selectedDay: Number of the day to select
     */
    @Composable
    fun WeekNumbersRow(weekNumbers:IntArray, selectedDay: Int) {
        Row {
            for (nDay in weekNumbers) {
                DayChar(
                    day = if (nDay == 0) " " else nDay.toString(),
                    selected = nDay == selectedDay
                )
            }
        }
    }

    // Muestra la fila con la cabecera de los dias,
    // TODO - de momento está solo en español
    @Composable
    fun WeekDaysRow() {
        Row {

            DayChar(day = "L", bold = true)
            DayChar(day = "M", bold = true)
            DayChar(day = "X", bold = true)
            DayChar(day = "J", bold = true)
            DayChar(day = "V", bold = true)
            DayChar(day = "S", bold = true)
            DayChar(day = "D", bold = true)

        }
    }

    @Composable
    fun DayChar(day: String, selected: Boolean = false, bold: Boolean = false) {

        Box(modifier = GlanceModifier.width(25.dp).height(25.dp),
            contentAlignment = Alignment(
                horizontal = Alignment.CenterHorizontally,
                vertical = Alignment.CenterVertically
            )
        ) {
            if (selected)
                Image(
                    provider = ImageProvider(R.drawable.selected),
                    colorFilter = ColorFilter.tint(colors.primary),
                    contentDescription = ""
                )
            Text(
                day, modifier = GlanceModifier.padding(2.dp),
                style = TextStyle(
                    fontWeight = if (bold) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Normal
                    },
                    color = colors.onSurface
                ),

                )
        }
    }

    @OptIn(ExperimentalGlancePreviewApi::class)
    @Preview
    @Composable
    fun CalendarPreviewExample() {
        CalendarView(1, "MARZO", constructCalendarMatrix(1, 31))
    }

}

