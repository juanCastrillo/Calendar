package juan.calendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.GlanceAppWidgetManager
import juan.calendar.widgets.MonthWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class DayChangedBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
            // The day has changed!
            println("The day has changed to ${LocalDate.now()}")
            GlobalScope.launch {
                updateMonthViewWidget(context)
            }
        }
    }

    private suspend fun updateMonthViewWidget(context: Context) {
        val manager = GlanceAppWidgetManager(context)
        val widget = MonthWidget()
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            widget.update(context, glanceId)
        }
    }
}