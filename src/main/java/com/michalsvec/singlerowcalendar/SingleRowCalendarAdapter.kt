package com.michalsvec.singlerowcalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @author
 * @param
 * @
 *
 */

class SingleRowCalendarAdapter(
    private val dateList: List<Date>,
    private val firstSpecialViewPositionList: List<Int>,
    private val secondSpecialViewPositionList: List<Int>,
    private val thirdSpecialViewPositionList: List<Int>,
    private val fourthSpecialViewPositionList: List<Int>,
    private val fifthSpecialViewPositionList: List<Int>,
    private val viewId: Int,
    private val dateTextViewId: Int,
    private val dayTextViewId: Int,
    private val monthTextViewId: Int,
    private val selectedViewId: Int,
    private val dayNameFormat: Int,
    private val weekendView: Int,
    private val weekendSelectedViewId: Int,
    private val firstSpecialViewId: Int,
    private var firstSelectedSpecialViewId: Int,
    private var secondSpecialViewId: Int,
    private var secondSelectedSpecialViewId: Int,
    private var thirdSpecialViewId: Int,
    private var thirdSelectedSpecialViewId: Int,
    private var fourthSpecialViewId: Int,
    private var fourthSelectedSpecialViewId: Int,
    private var fifthSpecialViewId: Int,
    private var fifthSelectedSpecialViewId: Int

) :

    RecyclerView.Adapter<SingleRowCalendarAdapter.CalendarViewHolder>() {

    private val ITEM = 3
    private val SELECTED_ITEM = 5
    private val WEEKEND_ITEM = 8
    private val SELECTED_WEEKEND_ITEM = 11
    private val FIRST_SPECIAL_ITEM = 14
    private val FIRST_SELECTED_SPECIAL_ITEM = 17
    private val SECOND_SPECIAL_ITEM = 21
    private val SECOND_SELECTED_SPECIAL_ITEM = 24
    private val THIRD_SPECIAL_ITEM = 27
    private val THIRD_SELECTED_SPECIAL_ITEM = 30
    private val FOURTH_SPECIAL_ITEM = 33
    private val FOURTH_SELECTED_SPECIAL_ITEM = 36
    private val FIFTH_SPECIAL_ITEM = 39
    private val FIFTH_SELECTED_SPECIAL_ITEM = 42

    companion object {
        lateinit var selectionTracker: SelectionTracker<Long>
    }


    /**
     * This function is responsible for choosing right type of itemView, for example selectedItemView, weekendItemView, etc.
     */
    override fun getItemViewType(position: Int): Int {

        val cal = Calendar.getInstance()
        cal.time = dateList[position]

        return if(!firstSpecialViewPositionList.isNullOrEmpty() && firstSpecialViewPositionList.contains(position))
            if(selectionTracker.isSelected(position.toLong()))
                FIRST_SELECTED_SPECIAL_ITEM
            else
                FIRST_SPECIAL_ITEM
        else if(!secondSpecialViewPositionList.isNullOrEmpty() && secondSpecialViewPositionList.contains(position))
            if(selectionTracker.isSelected(position.toLong()))
                SECOND_SELECTED_SPECIAL_ITEM
            else
                SECOND_SPECIAL_ITEM
        else if(!thirdSpecialViewPositionList.isNullOrEmpty() && thirdSpecialViewPositionList.contains(position))
            if(selectionTracker.isSelected(position.toLong()))
                THIRD_SELECTED_SPECIAL_ITEM
            else
                THIRD_SPECIAL_ITEM
        else if(!fourthSpecialViewPositionList.isNullOrEmpty() && fourthSpecialViewPositionList.contains(position))
            if(selectionTracker.isSelected(position.toLong()))
                FOURTH_SELECTED_SPECIAL_ITEM
            else
                FOURTH_SPECIAL_ITEM
        else if(!fifthSpecialViewPositionList.isNullOrEmpty() && fifthSpecialViewPositionList.contains(position))
            if(selectionTracker.isSelected(position.toLong()))
                FIFTH_SELECTED_SPECIAL_ITEM
            else
                FIFTH_SPECIAL_ITEM
        else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            if (selectedViewId != 0 && selectionTracker.isSelected(position.toLong()))
                SELECTED_WEEKEND_ITEM
        else if (weekendView != 0)
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

    /**
     * This function is responsible for inflating right itemView layouts
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = when (viewType) {
            ITEM -> LayoutInflater.from(parent.context)
                .inflate(viewId, parent, false)
            WEEKEND_ITEM -> LayoutInflater.from(parent.context)
                .inflate(weekendView, parent, false)
            SELECTED_WEEKEND_ITEM -> LayoutInflater.from(parent.context)
                .inflate(weekendSelectedViewId, parent, false)
            FIRST_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(firstSpecialViewId, parent, false)
            FIRST_SELECTED_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(firstSelectedSpecialViewId, parent, false)
            SECOND_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(secondSpecialViewId, parent, false)
            SECOND_SELECTED_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(secondSelectedSpecialViewId, parent, false)
            THIRD_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(thirdSpecialViewId, parent, false)
            THIRD_SELECTED_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(thirdSelectedSpecialViewId, parent, false)
            FOURTH_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(fourthSpecialViewId, parent, false)
            FOURTH_SELECTED_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(fourthSelectedSpecialViewId, parent, false)
            FIFTH_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(fifthSpecialViewId, parent, false)
            FIFTH_SELECTED_SPECIAL_ITEM -> LayoutInflater.from(parent.context)
                .inflate(fifthSelectedSpecialViewId, parent, false)
            else -> LayoutInflater.from(parent.context)
                .inflate(selectedViewId, parent, false)
        }

        return CalendarViewHolder(itemView)
    }


    /**
     * This function is responsible for binding data to itemView
     */
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


    /**
     *
     */
    override fun getItemCount() = dateList.size


    override fun getItemId(position: Int): Long = position.toLong()
}