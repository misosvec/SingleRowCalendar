package com.michalsvec.singlerowcalendar

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Michal Švec
 * @since v1.0.0
 */

object DateUtils {

    /**
     * @param date - which we want full name from
     * @return full day name, for example Friday, Thursday, Monday, etc...
     */
    fun getDayName(date: Date): String =
        SimpleDateFormat("EEEE", Locale.getDefault()).format(date)

    /**
     * @param date - which we want 3 letters abbreviation from
     * @return day abbreviation, for example Fri, Thu, Mon, etc...
     */
    fun getDay3LettersName(date: Date): String =
        SimpleDateFormat("EE", Locale.getDefault()).format(date)

    /**
     * @param date - which we want 1 letter abbreviation from
     * @return day abbreviation, for example F, T, M, S, etc...
     */
    fun getDay1LetterName(date: Date): String =
        SimpleDateFormat("EEEEE", Locale.getDefault()).format(date)

    /**
     * @param date - which we want month number from
     * @return month number, for example 1, 3, 12, 9, etc...
     */
    fun getMonthNumber(date: Date): String =
        SimpleDateFormat("MM", Locale.getDefault()).format(date)

    /**
     * @param date - which we want month name from
     * @return month name, for example December, September, January, etc...
     */
    fun getMonthName(date: Date): String =
        SimpleDateFormat("MMMM", Locale.getDefault()).format(date)

    /**
     * @param date - which we want month 3 letters abbreviation from
     * @return month abbreviation, for example Jan, Feb, Dec, etc...
     */
    fun getMonth3LettersName(date: Date): String =
        SimpleDateFormat("MMM", Locale.getDefault()).format(date)

    /**
     * @param date - which we want year from
     * @return year, for example 2010, 2019, 2020, 2034...
     */
    fun getYear(date: Date): String =
        SimpleDateFormat("yyyy", Locale.getDefault()).format(date)

    /**
     * @param date - which we want day number from
     * @return number of day in month, for example 15, 16, 17, etc...
     */
    fun getDayNumber(date: Date): String =
        SimpleDateFormat("dd", Locale.getDefault()).format(date)


    /**
     * @param count - future days count from now which we want to load
     * @return list of future dates witch specified length
     */
    fun getFutureDates(count: Int): MutableList<Date> {
        val futureDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        for (i in 0 until count) {
            cal.add(Calendar.DATE, 1)
            futureDateList.add(cal.time)
        }
        return futureDateList
    }

    /**
     * @param count - past days count from now which we want to load
     * @return list of past dates witch specified length
     */
    fun getPastDates(count: Int): MutableList<Date> {
        val pastDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        for (i in 0 until count) {
            cal.add(Calendar.DATE, -1)
            pastDateList.add(cal.time)
        }
        return pastDateList
    }
}