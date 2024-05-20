package juan.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.background
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import java.time.DayOfWeek
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

        val firstWeekday = today.withDayOfMonth(1).dayOfWeek.value // Monday is 1, Sunday is 7.
        val numberOfDays = today.lengthOfMonth()
        val numberOfDaysPrev = today.minusMonths(1).lengthOfMonth()
        val currentDay = today.dayOfMonth
        // DayOfWeek.MONDAY
        // Display the current date.
        Log.d("DATESSSSS", "Current date: $today, firstWeekDay: $firstWeekday")
        provideContent {
            calendarView(firstWeekday, currentDay, numberOfDays, numberOfDaysPrev)
        }
    }

    @Composable
    fun calendarView(firstWeekDay: Int, currentDay: Int, nDays: Int, prevnDays: Int) {
        Column(
            modifier = GlanceModifier.fillMaxSize().background(Color.Black),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            weekRow() // L M X ...
            daysRow(1, firstWeekDay-1, currentDay, nDays)
            // X X X X X X 1
            /*
            1, 7-1, Y
            2 = 7 - 6 + 1
             */
            val firstDay = 7 - firstWeekDay + 2
            daysRow(firstDay, 0, currentDay, nDays)
            daysRow(firstDay+7*1, 0, currentDay, nDays)
            daysRow(firstDay+7*2, 0, currentDay, nDays)
            daysRow(firstDay+7*3, 0, currentDay, nDays)
            daysRow(firstDay+7*4, 0, currentDay, nDays)
        }
    }

    /**
     * numberStart: First number to draw
     * dayStart: Position to start drawing
     * selectedDay: Number of the day to select
     */
    @Composable
    fun daysRow(numberStart: Int, dayStart: Int, selectedDay: Int, maxDay:Int) {
        Row {
//            for (nday in 0 until dayStart) {
//                DayChar(day = " ")
//            }
//            for (nday in dayStart until (7-dayStart)) {
//                val n = numberStart + nday
//                DayChar(day = n.toString(), n == selectedDay)
//            }
            var currentDayNumber = numberStart
            for (nday in 0 until 7) {
                if (nday < dayStart || currentDayNumber > maxDay)
                    DayChar(day = " ")
                else {
                    DayChar(day = currentDayNumber.toString(), currentDayNumber == selectedDay)
                    currentDayNumber += 1
                }
            }
        }
    }

    // Muestra la fila con la cabecera de los dias,
// TODO - de momento está solo en español
    @Composable
    fun weekRow() {
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
    fun DayChar(day: String, selected: Boolean = false, bold: Boolean = false,) {

        Box(modifier = GlanceModifier.width(25.dp).height(25.dp),
            contentAlignment = Alignment(
                horizontal = Alignment.CenterHorizontally,
                vertical = Alignment.CenterVertically
            )
        ) {
            if (selected)
                Image(provider = ImageProvider(R.drawable.selected), contentDescription = "selected")
            Text(day, modifier = GlanceModifier.padding(2.dp),
                style = TextStyle(
                    fontWeight = if (bold) {FontWeight.Bold} else {FontWeight.Normal},
                    color = ColorProvider(Color.White)
                ),

            )
        }

    }

}

