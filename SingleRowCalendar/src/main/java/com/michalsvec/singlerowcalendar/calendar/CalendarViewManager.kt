package com.michalsvec.singlerowcalendar.calendar

import java.util.*

/**
 * Callback used for binding views to SingleRowCalendar
 * @author Michal Švec - misosvec01@gmail.com
 * @since v1.0.0
 */

interface CalendarViewManager {

    /**
     * @param position of specific view in the SingleRowCalendar
     * @param date of specific view in the SingleRowCalendar
     * @param isSelected returns true if item in the SingleRowCalendar is selected else returns false
     * @return a resource id for SingleRowCalendar itemView
     */
    fun setCalendarViewResourceId(position: Int, date: Date, isSelected: Boolean): Int

    /**
     * @param holder is CalendarViewHolder used in SingleRowCalendar
     * @param date value of SingleRowCalendar item
     * @param position of specific view in the SingleRowCalendar
     * @param isSelected returns true if item in the SingleRowCalendar is selected else returns false
     */
    fun bindDataToCalendarView(holder: SingleRowCalendarAdapter.CalendarViewHolder, date: Date, position: Int, isSelected: Boolean)

}