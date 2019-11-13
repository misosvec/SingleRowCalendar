package com.michalsvec.singlerowcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SingleRowCalendarAdapter(
    private val dateList: List<Date>,
    private val itemLayoutId: Int,
    private val textViewDateId: Int,
    private val textViewDayId: Int,
    private val selectedItemLayoutId: Int,
    private val dayNameFormat: Int
) :

    RecyclerView.Adapter<SingleRowCalendarAdapter.CalendarViewHolder>() {


    val ITEM = 3
    val SELECTED_ITEM = 5


    companion object {
        lateinit var selectionTracker: SelectionTracker<Long>
    }

    override fun getItemViewType(position: Int): Int =
        if (selectionTracker.isSelected(position.toLong()))
            SELECTED_ITEM
        else
            ITEM


    init {
        setHasStableIds(true)
    }

    inner class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder =
        if (viewType == ITEM)
            CalendarViewHolder(LayoutInflater.from(parent.context)
                .inflate(itemLayoutId, parent, false))
        else
            CalendarViewHolder(LayoutInflater.from(parent.context)
                .inflate(selectedItemLayoutId, parent, false))



    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(textViewDayId)?.text =
            when(dayNameFormat){
                1 -> DateHelper.getDay1LetterAbbreviation(dateList[position])
                3 -> DateHelper.getDay3LettersAbbreviation(dateList[position])
                0 -> DateHelper.getDayName(dateList[position])
                else -> DateHelper.getDay3LettersAbbreviation(dateList[position])
            }

        holder.itemView.findViewById<TextView>(textViewDateId)?.text =
            DateHelper.getDayNumber(dateList[position])

    }

    override fun getItemCount() = dateList.size

    override fun getItemId(position: Int): Long = position.toLong()

}