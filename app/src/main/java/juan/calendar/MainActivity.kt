package juan.calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import juan.calendar.ui.theme.CalendarTheme


// TODO - https://developer.android.com/guide/topics/providers/calendar-provider?hl=es-419
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Hey Juan, keep calm, its going to work eventually. As all good things do")
                }
            }
        }
    }
}



object AppConstants {
    const val LAST_UPDATED_WIDGETS_TIME_KEY = "last_updated_time"
    // Other constants
}