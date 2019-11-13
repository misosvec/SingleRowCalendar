package com.michalsvec.singlerowcalendar

interface MonthAndYearListener {
    fun whenMonthAndYearChange(monthNumber: String, monthName : String, year: String)
}