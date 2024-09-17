//package juan.calendar
//
//import android.content.Context
//import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.glance.GlanceId
//import androidx.glance.GlanceModifier
//import androidx.glance.Image
//import androidx.glance.ImageProvider
//import androidx.glance.appwidget.GlanceAppWidget
//import androidx.glance.appwidget.GlanceAppWidgetReceiver
//import androidx.glance.appwidget.provideContent
//import androidx.glance.background
//import androidx.glance.layout.Alignment
//import androidx.glance.layout.Box
//import androidx.glance.layout.Column
//import androidx.glance.layout.Row
//import androidx.glance.layout.fillMaxSize
//import androidx.glance.layout.height
//import androidx.glance.layout.padding
//import androidx.glance.layout.width
//import androidx.glance.text.FontWeight
//import androidx.glance.text.Text
//import androidx.glance.text.TextStyle
//import androidx.glance.unit.ColorProvider
//import java.time.LocalDate
//
//// TODO - Use dynamic theme color
//// TODO - Set sizes dynamically depending on widget size
//class MonthViewWidgetReceiver : GlanceAppWidgetReceiver() {
//
//    // Let MyAppWidgetReceiver know which GlanceAppWidget to use
//    override val glanceAppWidget: GlanceAppWidget = MonthViewWidget()
//}
//
///**
// * Implementation of App Widget functionality.
// */
//
//class MonthViewWidget : GlanceAppWidget() {
//    override suspend fun provideGlance(context: Context, id: GlanceId) {
//        val today = LocalDate.now()
//
//        // Which weekday is the 1st of the current month (What weekday the current month starts at)
//
//        val firstWeekday = today.withDayOfMonth(1).dayOfWeek.value // Monday:1, Sunday:7
//        val numberOfDays = today.lengthOfMonth() // Number of days of the current month
//        val numberOfDaysPrev = today.minusMonths(1).lengthOfMonth()
//        val currentDay = today.dayOfMonth
//        // DayOfWeek.MONDAY
//        // Display the current date.
//
//
//
//        Log.d("DATESSSSS", "Current date: $today, firstWeekDay: $firstWeekday")
//        provideContent {
//            CalendarView(firstWeekday, currentDay, numberOfDays, numberOfDaysPrev)
//        }
//    }
//
//    @Composable
//    fun CalendarView(firstWeekDay: Int, currentDay: Int, nDays: Int, prevnDays: Int) {
//        Column(
//            modifier = GlanceModifier.fillMaxSize().background(Color.Black),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            WeekDaysRow() // L M X ...
//            WeekNumbersRow(1, firstWeekDay-1, currentDay, nDays)
//            // X X X X X X 1
//            /*
//            1, 7-1, Y
//            2 = 7 - 6 + 1
//             */
//            val firstDay = 7 - firstWeekDay + 2
//            WeekNumbersRow(firstDay, 0, currentDay, nDays)
//            WeekNumbersRow(firstDay+7*1, 0, currentDay, nDays)
//            WeekNumbersRow(firstDay+7*2, 0, currentDay, nDays)
//            WeekNumbersRow(firstDay+7*3, 0, currentDay, nDays)
//            WeekNumbersRow(firstDay+7*4, 0, currentDay, nDays)
//        }
//    }
//
//    /**
//     * numberStart: First number to draw
//     * dayStart: Position to start drawing
//     * selectedDay: Number of the day to select
//     */
//    @Composable
//    fun WeekNumbersRow(numberStart: Int, dayStart: Int, selectedDay: Int, maxDay:Int) {
//        Row {
////            for (nday in 0 until dayStart) {
////                DayChar(day = " ")
////            }
////            for (nday in dayStart until (7-dayStart)) {
////                val n = numberStart + nday
////                DayChar(day = n.toString(), n == selectedDay)
////            }
//            var currentDayNumber = numberStart
//            for (nday in 0 until 7) {
//                if (nday < dayStart || currentDayNumber > maxDay)
//                    DayChar(day = " ")
//                else {
//                    DayChar(day = currentDayNumber.toString(), currentDayNumber == selectedDay)
//                    currentDayNumber += 1
//                }
//            }
//        }
//    }
//
//    // Muestra la fila con la cabecera de los dias,
//// TODO - de momento está solo en español
//    @Composable
//    fun WeekDaysRow() {
//        Row {
//
//            DayChar(day = "L", bold = true)
//            DayChar(day = "M", bold = true)
//            DayChar(day = "X", bold = true)
//            DayChar(day = "J", bold = true)
//            DayChar(day = "V", bold = true)
//            DayChar(day = "S", bold = true)
//            DayChar(day = "D", bold = true)
//
//        }
//    }
//
//    @Composable
//    fun DayChar(day: String, selected: Boolean = false, bold: Boolean = false,) {
//
//        Box(modifier = GlanceModifier.width(25.dp).height(25.dp),
//            contentAlignment = Alignment(
//                horizontal = Alignment.CenterHorizontally,
//                vertical = Alignment.CenterVertically
//            )
//        ) {
//            if (selected)
//                Image(provider = ImageProvider(R.drawable.selected), contentDescription = "selected")
//            Text(day, modifier = GlanceModifier.padding(2.dp),
//                style = TextStyle(
//                    fontWeight = if (bold) {FontWeight.Bold} else {FontWeight.Normal},
//                    color = ColorProvider(Color.White)
//                ),
//
//            )
//        }
//
//    }
//
//}
//
