import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun getDayAbbreviation(date: Date): String =
        SimpleDateFormat("EE", Locale.getDefault()).format(date)

    fun getDayNumber(date: Date): String =
        SimpleDateFormat("dd", Locale.getDefault()).format(date)
}