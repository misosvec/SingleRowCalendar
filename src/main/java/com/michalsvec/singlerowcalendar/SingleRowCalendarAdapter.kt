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
    private val dateTextViewId: Int,
    private val dayTextViewId: Int,
    private val monthTextViewId: Int,
    private val dayNameFormat: Int,
    private var calendarViewType: CalendarViewType

) :

    RecyclerView.Adapter<SingleRowCalendarAdapter.CalendarViewHolder>() {


    companion object {
        lateinit var selectionTracker: SelectionTracker<Long>
    }


    /**
     * This function is responsible for choosing right type of itemView, for example selectedItemView, weekendItemView, etc.
     */
    override fun getItemViewType(position: Int): Int {
        return if (selectionTracker.isSelected(position.toLong())){
            // when item is selected,position will have negative value + 1
            val selectedPosition = position + 1
            return -selectedPosition
        } else
            position
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
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CalendarViewHolder {

        val viewId =  if(position < 0)
            // when position is negative, item is selected and then we have to take position back to original state
            calendarViewType.calendarViewId(
                (position * -1) -1,
                true
            )
        else
            calendarViewType.calendarViewId(
                position,
                false
            )


        val itemView = LayoutInflater.from(parent.context).inflate(viewId, parent, false)

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