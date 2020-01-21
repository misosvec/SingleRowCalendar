package com.michalsvec.singlerowcalendar

import java.util.*

interface CalendarViewHolder {
    fun chooseCalendarView(position: Int, isSelected: Boolean) :Int
    fun bindDataToCalendarView(holder: SingleRowCalendarAdapter.CalendarViewHolder, date: Date, position: Int, isSelected: Boolean)
}