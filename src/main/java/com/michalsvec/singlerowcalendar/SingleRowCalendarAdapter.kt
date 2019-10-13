package com.michalsvec.singlerowcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*

class SingleRowCalendarAdapter(private val dateList: List<Date>) :
    RecyclerView.Adapter<SingleRowCalendarAdapter.CalendarViewHolder>() {

    init {
        setHasStableIds(true)
    }

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_item, parent, false)

        return CalendarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.itemView.tv_day_abbreviation_calendar_item.text = dateList[position].toString()

    }

    override fun getItemCount() = dateList.size

    override fun getItemId(position: Int): Long = position.toLong()

}