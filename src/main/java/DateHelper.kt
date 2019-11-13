import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    /**
     * @return date abbreviation, for example Fri, Thu, Mon, etc...
     */
    fun getDayFullName(date: Date): String =
        SimpleDateFormat("EEEE", Locale.getDefault()).format(date)


    fun get3LettersDayAbbreviation(date: Date): String =
        SimpleDateFormat("EE", Locale.getDefault()).format(date)

    fun get1LetterDayAbbreviation(date: Date): String =
        SimpleDateFormat("EEEEE", Locale.getDefault()).format(date)



    /**
     * @return number of day in month, for example 15, 16, 17, etc...
     */
    fun getDayNumber(date: Date): String =
        SimpleDateFormat("dd", Locale.getDefault()).format(date)


    fun getFutureDates(numberOfDays: Int): MutableList<Date> {
        val futureDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        for (i in 0 until numberOfDays) {
            cal.add(Calendar.DATE, 1)
            futureDateList.add(cal.time)
        }
        return futureDateList
    }

    fun getPastDates(numberOfDays: Int): MutableList<Date> {
        val pastDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        for (i in 0 until numberOfDays) {
            cal.add(Calendar.DATE, -1)
            pastDateList.add(cal.time)
        }
        return pastDateList
    }
}