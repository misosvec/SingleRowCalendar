package com.michalsvec.singlerowcalendar

import android.content.res.XmlResourceParser
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.calendar_item.view.*
import java.util.*

class SingleRowCalendarAdapter(private val dateList: List<Date>, private val layoutId: Int, private val textViewDateId: Int, private val textViewDayId: Int) :

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
            .inflate(layoutId, parent, false)

        return CalendarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(textViewDayId).text = DateHelper.getDayAbbreviation(dateList[position])
        holder.itemView.findViewById<TextView>(textViewDateId).text = DateHelper.getDayNumber(dateList[position])
    }

    override fun getItemCount() = dateList.size

    override fun getItemId(position: Int): Long = position.toLong()

}