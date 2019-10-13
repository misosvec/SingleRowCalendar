package com.michalsvec.singlerowcalendar.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.SingleRowCalendar
import com.michalsvec.singlerowcalendar.SingleRowCalendarAdapter


class CalendarDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as SingleRowCalendarAdapter.CalendarViewHolder).getItemDetails()
        }
        return null
    }
}