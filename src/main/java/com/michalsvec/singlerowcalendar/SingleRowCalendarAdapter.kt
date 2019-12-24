package com.michalsvec.singlerowcalendar

import android.util.Log
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
    private val dateTextViewId: Int,
    private val dayTextViewId: Int,
    private val monthTextViewId: Int,
    private val selectedItemLayoutId: Int,
    private val dayNameFormat: Int,
    private val weekendItemLayout: Int,
    private val weekendSelectedItemLayout: Int
) :

    RecyclerView.Adapter<SingleRowCalendarAdapter.CalendarViewHolder>() {

    private val ITEM = 3
    private val SELECTED_ITEM = 5
    private val WEEKEND_ITEM = 8
    private val SELECTED_WEEKEND_ITEM = 11


    companion object {
        lateinit var selectionTracker: SelectionTracker<Long>
    }

    override fun getItemViewType(position: Int): Int {
        val cal = Calendar.getInstance()
        cal.time = dateList[position]
        return if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            if (selectedItemLayoutId != 0 && selectionTracker.isSelected(position.toLong()))
                SELECTED_WEEKEND_ITEM
            else if (weekendItemLayout != 0)
                WEEKEND_ITEM
            else if (selectionTracker.isSelected(position.toLong()))
                SELECTED_ITEM
            else
                ITEM
        else if (selectionTracker.isSelected(position.toLong()))
            SELECTED_ITEM
        else
            ITEM
    }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = when (viewType) {
            ITEM -> LayoutInflater.from(parent.context)
                .inflate(itemLayoutId, parent, false)
            WEEKEND_ITEM -> LayoutInflater.from(parent.context)
                .inflate(weekendItemLayout, parent, false)
            SELECTED_WEEKEND_ITEM -> LayoutInflater.from(parent.context)
                .inflate(weekendSelectedItemLayout, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(selectedItemLayoutId, parent, false)
        }

        return CalendarViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {

        holder.itemView.findViewById<TextView>(dayTextViewId)?.text =
            when (dayNameFormat) {
                1 -> DateHelper.getDay1LetterName(dateList[position])
                3 -> DateHelper.getDay3LettersName(dateList[position])
                0 -> DateHelper.getDayName(dateList[position])
                else -> DateHelper.getDay3LettersName(dateList[position])
            }

        holder.itemView.findViewById<TextView>(dateTextViewId)?.text =
            DateHelper.getDayNumber(dateList[position])

        holder.itemView.findViewById<TextView>(monthTextViewId)?.text =
            DateHelper.getMonth3LettersName(dateList[position])

    }

    override fun getItemCount() = dateList.size

    override fun getItemId(position: Int): Long = position.toLong()
}