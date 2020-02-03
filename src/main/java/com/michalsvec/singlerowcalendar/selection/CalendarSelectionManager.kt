package com.michalsvec.singlerowcalendar.selection

import java.util.*

/**
 * @author Michal Švec - misosvec01@gmail.com
 * @since v1.0.0
 */

interface CalendarSelectionManager {

    /**
     * Using this function we can enable or disable selection for particular item
     * @param position of specific item in a calendar
     * @param date specific date used in calendar on this position
     * @return true when item can be selected, else returns false
     */
    fun canBeItemSelected(position : Int, date: Date) : Boolean
}