package com.michalsvec.singlerowcalendar

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.selection.CalendarDetailsLookup
import com.michalsvec.singlerowcalendar.selection.CalendarKeyProvider
import java.util.*


class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {


    /**
     * we can disable long press to select when we select this key
     */
    private val GHOST_ITEM_KEY = -999999999999999299

    private lateinit var selectionTracker: SelectionTracker<Long>
    private var calendarChangesObserver: CalendarChangesObserver? = null
    private val dateList: MutableList<Date> = mutableListOf()
    private var previousMonthNumber = ""
    private var previousYear = ""
    private var multiSelection: Boolean
    private var disableUnselection: Boolean
    private var enableLongPress: Boolean
    private var itemLayoutId: Int
    private var dateTextViewId: Int
    private var dayTextViewId: Int
    private var monthTextViewId: Int
    private var selectedItemLayoutId: Int
    private var pastDaysCount: Int
    private var futureDaysCount: Int
    private var includeCurrentDate: Boolean
    private var initialPositionIndex: Int
    private var dayNameFormat: Int
    private var weekendItemLayoutId: Int
    private var weekendSelectedItemLayoutId: Int

    init {
        itemAnimator = null // this remove blinking when clicking items

        context.theme.obtainStyledAttributes(attrs, R.styleable.SingleRowCalendar, 0, 0).apply {

            try {
                itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                dateTextViewId = getResourceId(R.styleable.SingleRowCalendar_dateTextViewId, 0)
                dayTextViewId = getResourceId(R.styleable.SingleRowCalendar_dayTextViewId, 0)
                monthTextViewId = getResourceId(R.styleable.SingleRowCalendar_monthTextViewId, 0)
                selectedItemLayoutId = getResourceId(R.styleable.SingleRowCalendar_selectedItemLayoutId, 0)
                pastDaysCount = getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)
                futureDaysCount = getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)
                includeCurrentDate = getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)
                initialPositionIndex = getInt(R.styleable.SingleRowCalendar_initialPositionIndex, pastDaysCount)
                dayNameFormat = getInt(R.styleable.SingleRowCalendar_dayNameFormat, 3)
                multiSelection = getBoolean(R.styleable.SingleRowCalendar_multiSelection, false)
                disableUnselection = getBoolean(R.styleable.SingleRowCalendar_disableUnselection, true)
                enableLongPress = getBoolean(R.styleable.SingleRowCalendar_enableLongPress, false)
                weekendItemLayoutId = getResourceId(R.styleable.SingleRowCalendar_weekendItemLayoutId, 0)
                weekendSelectedItemLayoutId = getResourceId(R.styleable.SingleRowCalendar_weekendSelectedItemLayoutId, 0)

                if (itemLayoutId != 0 && dateTextViewId != 0 && dayTextViewId != 0) {
                    init()

                }
            } finally {
                recycle()
            }
        }


    }


    private fun init() {

        this.apply {

            dateList.clear()
            dateList.addAll(loadDates(pastDaysCount, futureDaysCount, includeCurrentDate))

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            (this.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                initialPositionIndex,
                0
            )

            setHasFixedSize(true)
            val singleRowCalendarAdapter = SingleRowCalendarAdapter(
                dateList,
                itemLayoutId,
                dateTextViewId,
                dayTextViewId,
                monthTextViewId,
                selectedItemLayoutId,
                dayNameFormat,
                weekendItemLayoutId,
                weekendSelectedItemLayoutId
            )
            adapter = singleRowCalendarAdapter
            initSelection()

            SingleRowCalendarAdapter.selectionTracker = selectionTracker


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val lastVisibleItem = if (dx > 0)
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    else
                        (layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()


                    if (previousMonthNumber != DateHelper.getMonthNumber(dateList[lastVisibleItem]) ||
                        previousYear != DateHelper.getYear(dateList[lastVisibleItem])
                    )
                        calendarChangesObserver?.whenMonthAndYearChanged(
                            DateHelper.getMonthNumber(dateList[lastVisibleItem]),
                            DateHelper.getMonthName(dateList[lastVisibleItem]),
                            DateHelper.getYear(dateList[lastVisibleItem])
                        )

                    previousMonthNumber = DateHelper.getMonthNumber(dateList[lastVisibleItem])
                    previousYear = DateHelper.getYear(dateList[lastVisibleItem])
                }
            })
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
        ).withSelectionPredicate(object : SelectionTracker.SelectionPredicate<Long>() {
            override fun canSelectMultiple(): Boolean {
                return multiSelection
            }

            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean =
                if (disableUnselection)
                    if (nextState)
                        disableUnselection
                    else
                        false
                else
                    true


            override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean =
                if (disableUnselection)
                    if (nextState)
                        disableUnselection
                    else
                        false
                else
                    true

        }).build()


        if (!enableLongPress)
            disableLongPress()



        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {

                if (key != GHOST_ITEM_KEY)
                    calendarChangesObserver?.whenSelectionChanged(
                        selected,
                        key.toInt(),
                        dateList[key.toInt()]
                    )

                if (selectionTracker.selection.size() == 0)
                    disableLongPress()

                super.onItemStateChanged(key, selected)

            }

            override fun onSelectionRefresh() {
                calendarChangesObserver?.whenSelectionRefreshed()
                super.onSelectionRefresh()
            }

            override fun onSelectionRestored() {
                calendarChangesObserver?.whenSelectionRestored()
                super.onSelectionRestored()
            }

        })


    }


    fun setCalendarChangesObserver(CalendarChangesObserver: CalendarChangesObserver) {
        this.calendarChangesObserver = CalendarChangesObserver
    }

    private fun disableLongPress() =
        selectionTracker.select(GHOST_ITEM_KEY)


    fun clearSelection() {
        selectionTracker.clearSelection()
        if (!enableLongPress)
            disableLongPress()
    }

    //WORKS
    fun select(position: Int) = selectionTracker.select(position.toLong())

    //WORKS
    fun setItemsSelected(positionList: List<Int>, selected: Boolean) {
        val longList = positionList.map { it.toLong() }
        selectionTracker.setItemsSelected(longList, selected)
    }

    //WORKS
    fun deselect(position: Int) = selectionTracker.deselect(position.toLong())

    //WORKS
    fun isSelected(position: Int) = selectionTracker.isSelected(position.toLong())

    //WORKS //TODO CHECK GHOST ITEM
    fun hasSelection(): Boolean = getSelectedIndexes().isNotEmpty()

    //WORKS
    fun onRestoreInstanceState(state: Bundle) = selectionTracker.onRestoreInstanceState(state)

    //WORKS
    fun onSaveInstanceState(state: Bundle) = selectionTracker.onSaveInstanceState(state)

    //WORKS
    fun getSelectedDates(): List<Date> {
        val selectionList: MutableList<Date> = mutableListOf()
        selectionTracker.selection.forEach {
            if (it != GHOST_ITEM_KEY && it.toInt() < dateList.size)
                selectionList.add(dateList[it.toInt()])
        }
        return selectionList
    }

    //WORKS
    fun getSelectedIndexes(): List<Int> {
        val selectionList: MutableList<Int> = mutableListOf()
        selectionTracker.selection.forEach {
            if (it != GHOST_ITEM_KEY && it.toInt() < dateList.size)
                selectionList.add(it.toInt())
        }
        return selectionList
    }


}