package com.michalsvec.singlerowcalendar

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.selection.CalendarDetailsLookup
import com.michalsvec.singlerowcalendar.selection.CalendarKeyProvider
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import java.util.*

/**
 * @author Michal Švec
 * @since v1.0.0
 */

class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {


    /**
     * we can disable long press to start selection when we select this key at first
     */
    private val GHOST_ITEM_KEY = -9

    private lateinit var selectionTracker: SelectionTracker<Long>
    private lateinit var calendarChangesObserver: CalendarChangesObserver
    private lateinit var calendarViewManager: CalendarViewManager
    private lateinit var calendarSelectionManager: CalendarSelectionManager
    private lateinit var setcalendarSelectionManager: CalendarSelectionManager
    val dateList: MutableList<Date> = mutableListOf()
    var previousMonthNumber = ""
    var previousYear = ""
    var previousWeek = ""
    var multiSelection: Boolean
    var deselection: Boolean
    var longPress: Boolean
    var pastDaysCount: Int
    var futureDaysCount: Int
    var includeCurrentDate: Boolean
    var initialPositionIndex: Int
    private var scrollPosition = 0


    init {
        // this remove blinking when clicking items
        itemAnimator = null

        // get attributes from xml declaration
        context.theme.obtainStyledAttributes(attrs, R.styleable.SingleRowCalendar, 0, 0).apply {

            try {
                pastDaysCount = getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)
                futureDaysCount = getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)
                includeCurrentDate =
                    getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)
                initialPositionIndex =
                    getInt(R.styleable.SingleRowCalendar_initialPositionIndex, pastDaysCount)
                multiSelection = getBoolean(R.styleable.SingleRowCalendar_multiSelection, false)
                deselection = getBoolean(R.styleable.SingleRowCalendar_deselection, true)
                longPress = getBoolean(R.styleable.SingleRowCalendar_longPress, false)
            } finally {
                recycle()
            }
        }
    }

    fun init() {

        this.apply {

            if (dateList.isNullOrEmpty()) {
                dateList.apply {
                    clear()
                    addAll(
                        DateUtils.getDates(
                            pastDaysCount,
                            futureDaysCount,
                            includeCurrentDate
                        )
                    )
                }
            }


            // set layout manager for RecyclerView
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            // scroll to a user selected position
            (this.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                initialPositionIndex,
                0
            )

            setHasFixedSize(true)

            adapter = SingleRowCalendarAdapter(dateList, calendarViewManager)

            initSelection()

            SingleRowCalendarAdapter.selectionTracker = selectionTracker


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    calendarChangesObserver.whenCalendarScrolled(dx, dy)

                    scrollPosition =
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                    val lastVisibleItem = if (dx > 0)
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    else
                        (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

                    if (previousMonthNumber != DateUtils.getMonthNumber(dateList[lastVisibleItem])
                        || previousYear != DateUtils.getYear(dateList[lastVisibleItem])
                        || previousWeek != DateUtils.getNumberOfWeek(dateList[lastVisibleItem])
                    ) {

                        calendarChangesObserver.whenWeekMonthYearChanged(
                            DateUtils.getNumberOfWeek(dateList[scrollPosition]),
                            DateUtils.getMonthNumber(dateList[lastVisibleItem]),
                            DateUtils.getMonthName(dateList[lastVisibleItem]),
                            DateUtils.getYear(dateList[lastVisibleItem]),
                            dateList[lastVisibleItem]
                        )
                    }

                    previousMonthNumber = DateUtils.getMonthNumber(dateList[lastVisibleItem])
                    previousYear = DateUtils.getYear(dateList[lastVisibleItem])
                    previousWeek = DateUtils.getNumberOfWeek(dateList[scrollPosition])
                }
            })
        }
    }


    /**
     * Init the selection in SingleRowCalendar
     */
    private fun initSelection() {

        // managing selection in whole SingleRowCalendar
        val selectionPredicate = object : SelectionTracker.SelectionPredicate<Long>() {

            override fun canSelectMultiple(): Boolean = multiSelection

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean =
                if (position != GHOST_ITEM_KEY)
                    if (calendarSelectionManager.canBeItemSelected(position, dateList[position]))
                        !(!nextState && !deselection) // if item is selected and deselection is
                    else
                        false // user cant select disabled items
                else
                    true // select ghost item


            override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean =
                if (key.toInt() != GHOST_ITEM_KEY)
                    if (calendarSelectionManager.canBeItemSelected(key.toInt(), dateList[key.toInt()]))
                        !(!nextState && !deselection) // if item is selected and deselection is
                    else
                        false // user cant select disabled items
                else
                    true // select ghost item

        }


        val selectionObserver = object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {

                if (key.toInt() != GHOST_ITEM_KEY && key.toInt() < dateList.size)
                    calendarChangesObserver.whenSelectionChanged(
                        selected,
                        key.toInt(),
                        dateList[key.toInt()]
                    )

                if (selectionTracker.selection.size() == 0)
                    disableLongPress()

                super.onItemStateChanged(key, selected)

            }

            override fun onSelectionRefresh() {
                calendarChangesObserver.whenSelectionRefreshed()
                super.onSelectionRefresh()
            }

            override fun onSelectionRestored() {
                calendarChangesObserver.whenSelectionRestored()
                super.onSelectionRestored()
            }

        }

        selectionTracker = SelectionTracker.Builder(
            "singleRowCalendarSelectionTracker",
            this,
            CalendarKeyProvider(this),
            CalendarDetailsLookup(this),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(selectionPredicate).build()


        if (!longPress)
            disableLongPress()


        selectionTracker.addObserver(selectionObserver)


    }

    /**
     * CalendarViewManager initialization
     */
    fun setCalendarViewManager(calendarViewManager: CalendarViewManager) {
        this.calendarViewManager = calendarViewManager
    }

    /**
     * CalendarChangesObserver initialization
     */
    fun setCalendarChangesObserver(calendarChangesObserver: CalendarChangesObserver) {
        this.calendarChangesObserver = calendarChangesObserver
    }

    /**
     * CalendarSelectionManager initialization
     */
    fun setCalendarSelectionManager(calendarSelectionManager: CalendarSelectionManager) {
        this.calendarSelectionManager = calendarSelectionManager
    }

    /**
     * Disables long press to start selection
     */
    private fun disableLongPress() =
        selectionTracker.select(GHOST_ITEM_KEY.toLong())


    /**
     * Clears both primary and provisional selections.
     * @return true if primary selection changed.
     */
    fun clearSelection(): Boolean {
        val selection = selectionTracker.clearSelection()
        if (!longPress)
            disableLongPress()
        return selection
    }

    /**
     * Attempts to select an item.
     * @return true if the item was selected. False if the item could not be selected, or was was already selected.
     */
    fun select(position: Int) = selectionTracker.select(position.toLong())

    /**
     * Sets the selected state of the specified items if permitted after consulting SelectionPredicate
     * @param positionList - positions you wish to be selected/deselected in SingleRowCalendar
     * @param selected - true = selected, false = deselected
     */
    fun setItemsSelected(positionList: List<Int>, selected: Boolean) {
        val longList = positionList.map { it.toLong() }
        selectionTracker.setItemsSelected(longList, selected)
    }

    /**
     * Attempts to deselect an item
     * @return true if the item was deselected. False if the item could not be deselected, or was was already un-selected.
     */
    fun deselect(position: Int) = selectionTracker.deselect(position.toLong())


    /**
     * Check if particular item is selected
     * @return true if the item specified by its id is selected
     */
    fun isSelected(position: Int) = selectionTracker.isSelected(position.toLong())


    /**
     * Check if SingleRowCalendar has any item selected
     * @return true if SingleRowCalednar has any item selected else returns false
     *///TODO CHECK GHOST ITEM
    fun hasSelection(): Boolean = getSelectedIndexes().isNotEmpty()


    /**
     * Restores selection from previously saved state. Call this method from Activity#onCreate.
     * @param state – bundle instance supplied to onCreate
     */
    fun onRestoreInstanceState(state: Bundle) = selectionTracker.onRestoreInstanceState(state)

    /**
     * Preserves selection, if any. Call this method from Activity#onSaveInstanceState
     * @param state – bundle instance supplied to onSaveInstanceState
     */
    fun onSaveInstanceState(state: Bundle) = selectionTracker.onSaveInstanceState(state)


    fun getSelectedDates(): List<Date> {
        val selectionList: MutableList<Date> = mutableListOf()
        selectionTracker.selection.forEach {
            if (it.toInt() != GHOST_ITEM_KEY && it.toInt() < dateList.size)
                selectionList.add(dateList[it.toInt()])
        }
        return selectionList
    }

    /**
     * @return list of selected positions
     */
    fun getSelectedIndexes(): List<Int> {
        val selectionList: MutableList<Int> = mutableListOf()
        selectionTracker.selection.forEach {
            if (it.toInt() != GHOST_ITEM_KEY && it.toInt() < dateList.size)
                selectionList.add(it.toInt())
        }
        return selectionList
    }

    /**
     * This function replace old list of dates with new one, then dates are dispatched to adapter using DiffCallback
     * @param newDateList - new dates, which we want to use in calendar
     */
    fun changeDates(newDateList: List<Date>) {
        val diffCallback = DateDiffCallback(dateList, newDateList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dateList.clear()
        dateList.addAll(newDateList)
        if (adapter != null)
            diffResult.dispatchUpdatesTo(adapter!!)
        if (scrollPosition > dateList.size - 1)
            scrollPosition = dateList.size - 1
        scrollToPosition(scrollPosition)
        calendarChangesObserver.whenWeekMonthYearChanged(
            DateUtils.getNumberOfWeek(dateList[scrollPosition]),
            DateUtils.getMonthNumber(dateList[scrollPosition]),
            DateUtils.getMonthName(dateList[scrollPosition]),
            DateUtils.getYear(dateList[scrollPosition]),
            dateList[scrollPosition]
        )
    }
}