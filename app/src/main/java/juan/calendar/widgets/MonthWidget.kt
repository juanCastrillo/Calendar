package juan.calendar.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
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
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import juan.calendar.R

// TODO - Use dynamic theme color
// TODO - Set sizes dynamically depending on widget size
class MonthWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MonthWidget()
}

class MonthWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val (currentDay, currentMonthName, calendarMatrix) = getCurrentMonthValues()
        provideContent {
            MonthWidgetView(currentDay, currentMonthName, calendarMatrix)
        }
    }
}

@Composable
fun MonthWidgetView(selectedDay: Int, monthName: String, calendarMatrix:Array<IntArray>, modifier: GlanceModifier = GlanceModifier) {
    GlanceTheme {
        MonthView(selectedDay, monthName, calendarMatrix, GlanceModifier.fillMaxSize())
    }
}

@Composable
fun MonthView(selectedDay: Int, monthName: String, calendarMatrix:Array<IntArray>, modifier: GlanceModifier = GlanceModifier) {

    Column(
        modifier = modifier
            .background(colors.background)
            .padding(Dp(16f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = GlanceModifier.defaultWeight(),
            horizontalAlignment = Alignment.Start
        ) {
            MonthText(
                monthName = monthName.toUpperCase(Locale.current),
                modifier = GlanceModifier.defaultWeight(),)
        }
        WeekDaysRow(modifier = GlanceModifier.defaultWeight()) // L M X ...
        for (week in calendarMatrix) {
            WeekNumbersRow(
                weekNumbers = week,
                selectedDay = selectedDay,
                modifier = GlanceModifier.defaultWeight())
        }
    }
}

@Composable
fun MonthText(monthName: String, modifier: GlanceModifier = GlanceModifier) {
    Text(monthName.toUpperCase(Locale.current),
        modifier = modifier,
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
fun WeekNumbersRow(weekNumbers:IntArray, selectedDay: Int, modifier: GlanceModifier = GlanceModifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for (nDay in weekNumbers) {
            DayChar(
                day = if (nDay == 0) " " else nDay.toString(),
                selected = nDay == selectedDay,
                modifier = GlanceModifier.defaultWeight()
            )
        }
    }
}

// Muestra la fila con la cabecera de los dias,
// TODO - de momento está solo en español
@Composable
fun WeekDaysRow(modifier: GlanceModifier = GlanceModifier) {
    Row (modifier = modifier.fillMaxWidth()) {
        for (firstLetterDay in listOf("L", "M", "X", "J", "V", "S", "D")) {
            DayChar(day = firstLetterDay, bold = true, modifier = GlanceModifier.defaultWeight())
        }
    }
}

@Composable
fun DayChar(day: String, selected: Boolean = false, bold: Boolean = false, modifier: GlanceModifier = GlanceModifier) {

    Box(
        modifier = modifier,
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

//@OptIn(ExperimentalGlancePreviewApi::class)
//@Preview
//@Composable
//fun CalendarPreviewExample() {
//    MonthView(1, "MARZO", constructCalendarMatrix(1, 31))
//}

