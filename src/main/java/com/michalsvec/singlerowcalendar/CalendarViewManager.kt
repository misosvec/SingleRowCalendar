package com.michalsvec.singlerowcalendar

import java.util.*

/**
 * @author Michal Švec
 * @since v1.0.0
 */

interface CalendarViewManager {

    /**
     * @param position of specific view in the SingleRowCalendar
     * @param isSelected returns true if item in the SingleRowCalendar is selected else returns false
     * @return a resource id for SingleRowCalendar itemView
     */
    fun setCalendarViewResourceId(position: Int, isSelected: Boolean): Int

    /**
     * @param holder is CalendarViewHolder used in SingleRowCalendar
     * @param date value of SingleRowCalendar item
     * @param position of specific view in the SingleRowCalendar
     * @param isSelected returns true if item in the SingleRowCalendar is selected else returns false
     */
    fun bindDataToCalendarView(holder: SingleRowCalendarAdapter.CalendarViewHolder, date: Date, position: Int, isSelected: Boolean)

}