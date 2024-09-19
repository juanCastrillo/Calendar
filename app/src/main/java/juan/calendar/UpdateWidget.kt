package juan.calendar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import juan.calendar.widgets.MonthAndEventsWidget
import juan.calendar.widgets.MonthWidget
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

        val widget2 = MonthAndEventsWidget()
        val glanceIds2 = manager.getGlanceIds(widget.javaClass)
        glanceIds2.forEach { glanceId ->
            widget2.update(context, glanceId)
        }
    }
}

// ContentObserver
class CalendarContentObserver(handler: Handler, private val onCalendarChange: () -> Unit) :
    ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        Log.d("EVENT_CalendarContentObserver", "Calendar content change received")
        onCalendarChange()
    }
}