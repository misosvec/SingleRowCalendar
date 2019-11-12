package com.michalsvec.singlerowcalendar

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {




    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SingleRowCalendar,
            0, 0
        ).apply {

            try {
                val itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                val itemDateTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDateTextViewId, 0)
                val itemDayTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDayTextViewId, 0)

                if (itemLayoutId != 0 && itemDateTextViewId != 0 && itemDayTextViewId != 0) {
                    init(
                        itemLayoutId = itemLayoutId,
                        itemDateTextViewId = itemDateTextViewId,
                        itemDayTextViewId = itemDayTextViewId
                    )
                }
            } finally {
                recycle()
            }
        }
    }


    fun init(itemLayoutId: Int, itemDateTextViewId: Int, itemDayTextViewId: Int) {


        this.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SingleRowCalendarAdapter(
                loadFutureAndPastDays(10, 10),
                itemLayoutId,
                itemDateTextViewId,
                itemDayTextViewId
            )
        }
    }

    private fun getFutureDays(numberOfDays: Int): MutableList<Date> {
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

    fun loadFutureAndPastDays(pastDays: Int, futureDays: Int): List<Date> {
        val futureList = getFutureDays(futureDays)
        val pastList = getPastDays(pastDays).reversed()
        val list = pastList + futureList
        return list

    }





}