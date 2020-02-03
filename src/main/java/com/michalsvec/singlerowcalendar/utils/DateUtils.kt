package com.michalsvec.singlerowcalendar.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Michal Švec - misosvec01@gmail.com
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
     * @param date - which we want month three letters abbreviation from
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
     * @param date - which we want week number from
     * @return number of week in year, for example 1, 15, 50, etc...
     */
    fun getNumberOfWeek(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.WEEK_OF_YEAR].toString()
    }

    /**
     * @param count - future days count from now which we want to load
     * @return list of future dates with specified length
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
     * @return list of past dates with specified length
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

    /**
     * Simple way to get dates, just using days count
     * @param pastDays - count of past days, which we want to get
     * @param futureDays - count of future days, which we want to get
     * @param includeCurrentDate - if true then list will contain current date else won't
     * @return list of dates
     */
    fun getDates(pastDays: Int, futureDays: Int, includeCurrentDate: Boolean): List<Date> {
        val futureList =
            getFutureDates(
                futureDays
            )
        val cal = Calendar.getInstance(Locale.getDefault())
        val pastList = getPastDates(
            pastDays
        ).reversed()
        return if (includeCurrentDate) pastList + cal.time + futureList else pastList + futureList
    }

}