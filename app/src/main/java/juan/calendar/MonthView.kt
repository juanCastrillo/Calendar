package juan.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import juan.calendar.widgets.getCurrentMonthValues

@Preview
@Composable
fun MonthViewPreview() {
    val (a,b,c) = getCurrentMonthValues()
    MonthView(a,b,c)
}

@Composable
fun MonthView(selectedDay: Int, monthName: String, calendarMatrix:Array<IntArray>, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(Dp(16f)),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp),
//                .background(colors.primary),
            horizontalArrangement = Arrangement.Start
        ) {
            MonthText(
                monthName = monthName.toUpperCase(Locale.current),
//                modifier = Modifier.defaultWeight(),
                )
        }
        WeekDaysRow(
//            modifier = Modifier.defaultWeight()
        ) // L M X ...
        for (week in calendarMatrix) {
            WeekNumbersRow(
                weekNumbers = week,
                selectedDay = selectedDay,)
        }
    }
}

@Composable
fun MonthText(monthName: String, modifier: Modifier = Modifier) {
    Text(
        monthName.toUpperCase(Locale.current),
        modifier = modifier,
        style = TextStyle(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    )
}

/**
 * numberStart: First number to draw
 * dayStart: Position to start drawing
 * selectedDay: Number of the day to select
 */
@Composable
fun WeekNumbersRow(weekNumbers:IntArray, selectedDay: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (nDay in weekNumbers) {
            DayChar(
                day = if (nDay == 0) " " else nDay.toString(),
                selected = nDay == selectedDay,
//                modifier = Modifier.defaultWeight()
            )
        }
    }
}

// Muestra la fila con la cabecera de los dias,
// TODO - de momento está solo en español
@Composable
fun WeekDaysRow(modifier: Modifier = Modifier) {
    Row (modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        for (firstLetterDay in listOf("L", "M", "X", "J", "V", "S", "D")) {
            DayChar(day = firstLetterDay, bold = true,)
        }
    }
}

@Composable
fun DayChar(day: String, selected: Boolean = false, bold: Boolean = false, modifier: Modifier = Modifier) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (selected)
            Box(
                modifier = Modifier
//                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {}
        Text(
            day, modifier = Modifier.padding(2.dp),
            style = TextStyle(
                fontWeight = if (bold) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
                color = MaterialTheme.colorScheme.onSurface
            ),

            )
    }
}