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
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
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
        MonthWidgetContentView(selectedDay, monthName, calendarMatrix, GlanceModifier.fillMaxSize())
    }
}

@Composable
fun MonthWidgetContentView(selectedDay: Int, monthName: String, calendarMatrix:Array<IntArray>, modifier: GlanceModifier = GlanceModifier) {

    Column(
        modifier = modifier
            .background(colors.background)
            .padding(Dp(16f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = GlanceModifier.defaultWeight()
                .fillMaxWidth()
                .padding(start = 6.dp),
//                .background(colors.primary),
            horizontalAlignment = Alignment.Start
        ) {
            MonthWidgetText(
                monthName = monthName.toUpperCase(Locale.current),
                modifier = GlanceModifier.defaultWeight(),)
        }
        WeekDaysRowWidget(modifier = GlanceModifier.defaultWeight()) // L M X ...
        for (week in calendarMatrix) {
            WeekNumbersRowWidget(
                weekNumbers = week,
                selectedDay = selectedDay,
                modifier = GlanceModifier.defaultWeight())
        }
    }
}

@Composable
fun MonthWidgetText(monthName: String, modifier: GlanceModifier = GlanceModifier) {
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
fun WeekNumbersRowWidget(weekNumbers:IntArray, selectedDay: Int, modifier: GlanceModifier = GlanceModifier) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for (nDay in weekNumbers) {
            DayCharWidget(
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
fun WeekDaysRowWidget(modifier: GlanceModifier = GlanceModifier) {
    Row (modifier = modifier.fillMaxWidth()) {
        for (firstLetterDay in listOf("L", "M", "X", "J", "V", "S", "D")) {
            DayCharWidget(day = firstLetterDay, bold = true, modifier = GlanceModifier.defaultWeight())
        }
    }
}

@Composable
fun DayCharWidget(day: String, selected: Boolean = false, bold: Boolean = false, modifier: GlanceModifier = GlanceModifier) {

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

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
fun CalendarWidgetPreviewExample() {
    val (a,b,c) = getCurrentMonthValues()
    Row(modifier = GlanceModifier.width(200.dp).height(200.dp)) {
        MonthWidgetContentView(a, b, c)
    }
}

