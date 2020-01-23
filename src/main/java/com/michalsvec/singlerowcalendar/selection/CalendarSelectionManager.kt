package com.michalsvec.singlerowcalendar.selection

import java.util.*

interface CalendarSelectionManager {
    fun canBeItemSelected(position : Int, date: Date) : Boolean
}