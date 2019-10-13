package com.michalsvec.singlerowcalendar

import android.content.Context
import android.provider.CalendarContract
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    fun init() {
        this.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SingleRowCalendarAdapter(loadFutureAndPastDays(10, 10))
        }
    }

    private fun getFutureDays(numberOfDays: Int) : MutableList<Date>{
        val futureDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        futureDateList.add(cal.time)
        for (i in 0..numberOfDays) {
            cal.add(Calendar.DATE, 1)
            futureDateList.add(cal.time)
        }
        return futureDateList
    }

    private fun getPastDays(numberOfDays: Int): MutableList<Date> {
        val pastDateList = mutableListOf<Date>()
        val cal = Calendar.getInstance(Locale.getDefault())
        pastDateList.add(cal.time)
        for (i in 0..numberOfDays) {
            cal.add(Calendar.DATE, -1)
            pastDateList.add(cal.time)
        }
        return pastDateList
    }

    fun loadFutureAndPastDays(pastDays: Int, futureDays:Int): List<Date> {
        val futureList = getFutureDays(futureDays)
        val pastList = getPastDays(pastDays).reversed()
        val list = pastList + futureList
        return list

    }




}