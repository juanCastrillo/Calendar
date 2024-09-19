//package juan.calendar.widgets
//
//import android.content.Context
//import android.database.ContentObserver
//import android.os.Handler
//import android.os.Looper
//import android.provider.CalendarContract
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.remember
//import androidx.compose.ui.graphics.Color
//import androidx.glance.GlanceId
//import androidx.glance.GlanceModifier
//import androidx.glance.appwidget.GlanceAppWidget
//import androidx.glance.appwidget.GlanceAppWidgetReceiver
//import androidx.glance.appwidget.cornerRadius
//import androidx.glance.appwidget.provideContent
//import androidx.glance.background
//import androidx.glance.layout.Box
//import androidx.glance.layout.Column
//import androidx.glance.layout.Row
//import java.time.LocalDate
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.glance.GlanceTheme
//import androidx.glance.GlanceTheme.colors
//import androidx.glance.layout.Alignment
//import androidx.glance.layout.fillMaxHeight
//import androidx.glance.layout.fillMaxWidth
//import androidx.glance.layout.height
//import androidx.glance.layout.padding
//import androidx.glance.layout.width
//import androidx.glance.preview.ExperimentalGlancePreviewApi
//import androidx.glance.preview.Preview
//import androidx.glance.text.FontWeight
//import androidx.glance.text.Text
//import androidx.glance.text.TextStyle
//import java.time.LocalTime
//import androidx.compose.ui.text.intl.Locale
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//import juan.calendar.CalendarContentObserver
//import juan.calendar.CalendarEvent
//import juan.calendar.CalendarUpdateWorker
//import juan.calendar.getTodayRemainingEvents
//
//class MonthAndEventsWidgetReceiver : GlanceAppWidgetReceiver() {
//    override val glanceAppWidget: GlanceAppWidget = MonthAndEventsWidget()
//}
//
//// TODO - Maybe fetch events when click on the widget.
//class MonthAndEventsWidget : GlanceAppWidget() {
//
//    private var calendarObserver: ContentObserver? = null
//    private var events: List<CalendarEvent> = listOf()
//
//    override suspend fun provideGlance(context: Context, id: GlanceId) {
//
//        val (currentDay, currentMonthName, calendarMatrix) = getCurrentMonthValues()
//        val weekDayName = LocalDate.now().dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.current.platformLocale)
//        events = getTodayRemainingEvents(context)
//
//        calendarObserver = CalendarContentObserver(Handler(Looper.getMainLooper())) {
//            // Trigger WorkManager to update widget
//            val workRequest =
//                OneTimeWorkRequestBuilder<CalendarUpdateWorker>().build()
//            WorkManager.getInstance(context).enqueue(workRequest)
//        }
//        context.contentResolver.registerContentObserver(
//            CalendarContract.Events.CONTENT_URI, true, calendarObserver!!)
//
//
//        Log.d("EVENTS_MonthAndEventsWidget", "Loaded data: $events, ready to draw")
//        provideContent {
//            MonthAndEventsView(currentDay, currentMonthName, calendarMatrix, events, weekDayName)
//        }
//    }
//    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
//        super.onDelete(context, glanceId)
//        calendarObserver?.let {
//            context.contentResolver.unregisterContentObserver(it)
//            calendarObserver = null
//        }
//
//    }
//
//    // Function to update the state
//    suspend fun fetchNewEvents(context: Context, glanceId: GlanceId) {
//        // Fetch latest calendar data
//        events = getTodayRemainingEvents(context)
////        update(context, glanceId)
//    }
//}
//
//
//@Composable
//fun MonthAndEventsView(selectedDay: Int, monthName: String, calendarMatrix:Array<IntArray>, events: List<CalendarEvent>, selectedWeekDayName: String) {
//    GlanceTheme {
//        Row(
//            modifier = GlanceModifier.background(colors.background)
//                .fillMaxWidth()
//        ) {
//            MonthView(
//                selectedDay, monthName, calendarMatrix,
//                modifier = GlanceModifier
//                .defaultWeight()
//            )
//            EventsView(
//                events,
//                selectedDay, selectedWeekDayName,
//                modifier = GlanceModifier
//                .defaultWeight()
//            )
//        }
//    }
//}
//
//@Composable
//fun EventsView(events: List<CalendarEvent>, selectedDay: Int, selectedWeekDayName: String, modifier: GlanceModifier = GlanceModifier) {
//    Column (
//        modifier = modifier
//            .padding(vertical = 16.dp)
//            .padding(end = 16.dp)
//            .fillMaxHeight(),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (events.isEmpty()) {
//            Column(
//                modifier = GlanceModifier.padding(start = 24.dp)
//                    .fillMaxHeight()
//            ) {
//                Text(
//                    selectedWeekDayName.uppercase(),
//                    style = TextStyle(
//                        fontWeight = FontWeight.Bold,
//                        color = colors.primary,
//                    ),
////                modifier = GlanceModifier.padding( start = 24.dp)
//                )
//                Row(
//                    modifier = GlanceModifier.defaultWeight(),
//                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
//                    verticalAlignment = Alignment.Top
//                )
//                {
//                    Text(
//                        "${selectedDay}",
//                        modifier = modifier,
//                        style = TextStyle(
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 50.sp, // TODO - Choose design, this or the one bellow.
//                            color = colors.onSurface,
////                        fontSize = 40.sp,
////                        color = colors.primary,
//                        )
//                    )
//                }
//                Text(
//                    "No hay más eventos hoy",
//                    modifier = GlanceModifier.defaultWeight(),
//                    style = TextStyle(color = colors.onSurface)
//                )
//            }
//        }
//        else {
//            Text(
//                "${selectedWeekDayName.uppercase()} ${selectedDay}",
//                style = TextStyle(
//                    fontWeight = FontWeight.Bold,
//                    color = colors.primary,
//                ),
//                modifier = GlanceModifier.padding(bottom = 2.dp, start = 24.dp)
//            )
//
//            // EVENTSS!!!!
//            Column(modifier = GlanceModifier.defaultWeight()) {
//                for (event in events)
//                    EventView(
//                        Color(150, 218, 181), // Get the color dynamically somehow
//                        title = event.title,
//                        timeRange = event.timeRange
//                    )
//            }
//        }
//    }
//}
//
//@Composable
//fun EventView(color: Color, title: String, timeRange: TimeRange) {
//    Row (
//        modifier = GlanceModifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp),
//        verticalAlignment = Alignment.CenterVertically
//    )
//    {
//        EventIndicator(color, modifier = GlanceModifier.defaultWeight()) // Vertical line with event color
////        Spacer(modifier = GlanceModifier.width(-.dp))
//        Box(modifier = GlanceModifier
//            .background(color.copy(alpha = 0.2f))
//            .cornerRadius(6.dp)
//            .defaultWeight()
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//        ) {
//            Column {
//                Text(
//                    title,
//                    style = TextStyle(fontWeight = FontWeight.Bold, color = colors.onSurface),
//                )// Title
//                Text(
//                    timeRange.toString(),
//                    style = TextStyle(color = colors.onSurface),
//                )// Time start - Time end
//            }
//        }
//    }
//}
//
//@Composable
//fun EventIndicator(color: Color, modifier: GlanceModifier = GlanceModifier) {
//    Row(
//        modifier = modifier
//            .width(Dp(16f)),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalAlignment = Alignment.CenterVertically
//
//    ) {
//        Box(
//            modifier = modifier
//                .defaultWeight()
//                .background(color)
//                .cornerRadius(Dp(16f))
//                .width(Dp(6f))
//
//        ) {
//            Text("")
//        }
//    }
//}
//
//@OptIn(ExperimentalGlancePreviewApi::class)
//@Preview
//@Composable
//fun EventViewPreview() {
//    Row(modifier = GlanceModifier.width(200.dp).height(200.dp)) {
//        EventsView(
//            events = examplesCalendarEvents, 17, "JUEVES"
//        )
//    }
//}
//
//@OptIn(ExperimentalGlancePreviewApi::class)
//@Preview
//@Composable
//fun EventViewEmptyPreview() {
//    Row(modifier = GlanceModifier.width(200.dp).height(200.dp)) {
//        EventsView(
//            listOf(), 19, "JUEVES"
//        )
//    }
//}
//
//@OptIn(ExperimentalGlancePreviewApi::class)
//@Preview
//@Composable
//fun EventMonthPreview() {
//    val(a, b, c) = getCurrentMonthValues()
//    Row(modifier = GlanceModifier.width(400.dp)) {
//        MonthAndEventsView(a, b, c, examplesCalendarEvents,"JUEVES")
//    }
//}
//
//@OptIn(ExperimentalGlancePreviewApi::class)
//@Preview
//@Composable
//fun EventMonthEmptyPreview() {
//    val(a, b, c) = getCurrentMonthValues()
//    Row(modifier = GlanceModifier.width(400.dp)) {
//        MonthAndEventsView(a, b, c, listOf(),"JUEVES")
//    }
//}
//
//val examplesCalendarEvents = listOf(
//    CalendarEvent("Ask dad sumthing",
//        TimeRange(LocalTime.now().minusHours(1), LocalTime.now())),
//    CalendarEvent("Go to mums",
//        TimeRange(LocalTime.now(), LocalTime.now().plusHours(1))),
//    CalendarEvent("Dumb thing",
//        TimeRange(LocalTime.now().plusHours(1), LocalTime.now().plusHours(2))),
//)
//
//
//
