package com.michalsvec.singlerowcalendar

import java.util.*

interface CalendarChangesObserver {
    fun whenMonthAndYearChanged(monthNumber: String, monthName : String, year: String){}
    fun whenSelectionChanged(isSelected: Boolean, index : Int, date : Date){}
    fun whenSelectionRestored(){}
    fun whenSelectionRefreshed(){}
}