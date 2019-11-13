package com.michalsvec.singlerowcalendar

import DateHelper
import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.selection.CalendarDetailsLookup
import com.michalsvec.singlerowcalendar.selection.CalendarKeyProvider
import java.util.*

class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {


    lateinit var selectionTracker: SelectionTracker<Long>

    init {

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SingleRowCalendar, 0, 0
        ).apply {

            try {
                val itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                val itemDateTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDateTextViewId, 0)
                val itemDayTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDayTextViewId, 0)

                val selectedItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_selectedItemLayoutId, 0)

                val pastDaysCount =
                    getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)


                val futureDaysCount =
                    getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)

                val includeCurrentDate =
                    getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)


                val initialPositionIndex =
                    getInt(R.styleable.SingleRowCalendar_initialPositionIndex, pastDaysCount)

                val dayNameFormat =
                    getInt(R.styleable.SingleRowCalendar_dayNameFormat, 3)




                if (itemLayoutId != 0 && itemDateTextViewId != 0 && itemDayTextViewId != 0) {
                    init(
                        itemLayoutId,
                        itemDateTextViewId,
                        itemDayTextViewId,
                        selectedItemLayoutId,
                        futureDaysCount,
                        pastDaysCount,
                        includeCurrentDate,
                        initialPositionIndex,
                        dayNameFormat
                    )
                }
            } finally {
                recycle()
            }
        }
    }


    private fun init(
        itemLayoutId: Int,
        itemDateTextViewId: Int,
        itemDayTextViewId: Int,
        selectedItemLayoutId: Int,
        futureDaysCount: Int,
        pastDaysCount: Int,
        includeCurrentDate: Boolean,
        initialScrollPosition: Int,
        dayNameFormat: Int
    ) {

        this.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager?.scrollToPosition(initialScrollPosition)
            setHasFixedSize(true)
            val singleRowCalendarAdapter = SingleRowCalendarAdapter(
                loadDates(pastDaysCount, futureDaysCount, includeCurrentDate),
                itemLayoutId,
                itemDateTextViewId,
                itemDayTextViewId,
                selectedItemLayoutId,
                dayNameFormat
            )
            adapter = singleRowCalendarAdapter
            initSelection()

            SingleRowCalendarAdapter.selectionTracker = selectionTracker
        }
    }


    private fun loadDates(pastDays: Int, futureDays: Int, includeCurrentDate: Boolean): List<Date> {
        val futureList = DateHelper.getFutureDates(futureDays)
        val cal = Calendar.getInstance(Locale.getDefault())
        val pastList = DateHelper.getPastDates(pastDays).reversed()
        return if (includeCurrentDate) pastList + cal.time + futureList else pastList + futureList
    }


    private fun initSelection() {
        selectionTracker = SelectionTracker.Builder(
            "singleRowCalendarSelectionTracker",
            this,
            CalendarKeyProvider(this),
            CalendarDetailsLookup(this),
            StorageStrategy.createLongStorage()
        ).build()
    }
}