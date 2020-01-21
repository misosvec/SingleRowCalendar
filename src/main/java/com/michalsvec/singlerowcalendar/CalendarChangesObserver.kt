package com.michalsvec.singlerowcalendar

import java.util.*

/**
 * @author Michal Švec
 * @since v1.0.0
 */

interface CalendarChangesObserver {

    /**
     * @param monthNumber number of month from the changed date
     * @param monthName name of month from the changed date
     * @param year from the changed date
     * @param date changed date
     */
    fun whenMonthAndYearChanged(monthNumber: String, monthName: String, year: String, date: Date) {}

    /**
     * @param isSelected returns true if item in the SingleRowCalendar is selected else returns false
     * @param position of specific view in the SingleRowCalendar
     * @param date value of SingleRowCalendar item where selection changed
     */
    fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {}

    /**
     * @param dx x-axis position of SingleRowCalendar
     * @param dy y-axis position of SingleRowCalendar
     */
    fun whenCalendarScrolled(dx: Int, dy: Int) {}


    fun whenSelectionRestored() {}

    //TODO DOCS
    fun whenSelectionRefreshed() {}
}