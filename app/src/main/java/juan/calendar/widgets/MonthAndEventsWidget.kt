package juan.calendar.widgets

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import java.time.LocalDate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceTheme
import androidx.glance.GlanceTheme.colors
import androidx.glance.layout.Alignment
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import java.time.LocalTime

class MonthAndEventsWidgetReceiver : GlanceAppWidgetReceiver() {

    // Let MyAppWidgetReceiver know which GlanceAppWidget to use
    override val glanceAppWidget: GlanceAppWidget = MonthAndEventsWidget()
}

class MonthAndEventsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val (currentDay, currentMonthName, calendarMatrix) = getCurrentMonthValues()
        provideContent {
            MonthAndEventsView(currentDay, currentMonthName, calendarMatrix)
        }
    }
}

@Composable
fun MonthAndEventsView(currentDay: Int, currentMonth: String, calendarMatrix:Array<IntArray>) {
    GlanceTheme {
        Row(
            modifier = GlanceModifier.background(colors.background)
                .fillMaxWidth()
        ) {
            MonthView(
                currentDay, currentMonth, calendarMatrix,
                modifier = GlanceModifier
                .defaultWeight()
            )
            EventsView(
                examplesCalendarEvents,
                modifier = GlanceModifier
                .defaultWeight()
            )
        }
    }
}

@Composable
fun EventsView(events: List<CalendarEvent>, modifier: GlanceModifier = GlanceModifier) {
    Column (
        modifier = modifier
            .padding(8.dp, end = 16.dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TODO - Add current day. ej: 16 Diciembre
        for (event in events)
            EventView(Color(150,218,181	), title = event.title, timeRange = event.timeRange)
    }
}

@Composable
fun EventView(color: Color, title: String, timeRange: TimeRange) {
    Row (
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        EventIndicator(color, modifier = GlanceModifier.defaultWeight()) // Vertical line with event color
//        Spacer(modifier = GlanceModifier.width(-.dp))
        Box(modifier = GlanceModifier
            .background(color.copy(alpha = 0.2f))
            .cornerRadius(6.dp)
            .defaultWeight()
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column {
                Text(
                    title,
                    style = TextStyle(fontWeight = FontWeight.Bold, color = colors.onSurface),
                )// Title
                Text(
                    timeRange.toString(),
                    style = TextStyle(color = colors.onSurface),
                )// Time start - Time end
            }
        }
    }
}

@Composable
fun EventIndicator(color: Color, modifier: GlanceModifier = GlanceModifier) {
    Row(
        modifier = modifier
            .width(Dp(16f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = modifier
                .defaultWeight()
                .background(color)
                .cornerRadius(Dp(16f))
                .width(Dp(6f))

        ) {
            Text("")
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview
@Composable
fun EventViewPreview() {
    EventsView(
        events = examplesCalendarEvents
    )
}

val examplesCalendarEvents = listOf(
    CalendarEvent("Ask dad sumthing",
        TimeRange(LocalTime.now().minusHours(1), LocalTime.now())),
    CalendarEvent("Go to mums",
        TimeRange(LocalTime.now(), LocalTime.now().plusHours(1))),
    CalendarEvent("Dumb thing",
        TimeRange(LocalTime.now().plusHours(1), LocalTime.now().plusHours(2))),
)

data class CalendarEvent(val title: String, val timeRange: TimeRange, val date: LocalDate = LocalDate.now())

