package com.michalsvec.singlerowcalendar

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DiffUtil
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
    val dateList: MutableList<Date> = mutableListOf()
    var firstSpecialItemPositionList: MutableList<Int> = mutableListOf()
    var secondSpecialItemPositionList: MutableList<Int> = mutableListOf()
    val thirdSpecialItemPositionList: MutableList<Int> = mutableListOf()
    var previousMonthNumber = ""
    var previousYear = ""
    var multiSelection: Boolean
    var disableUnselection: Boolean
    var enableLongPress: Boolean
    var itemLayoutId: Int
    var dateTextViewId: Int
    var dayTextViewId: Int
    var monthTextViewId: Int
    var selectedItemLayoutId: Int
    var pastDaysCount: Int
    var futureDaysCount: Int
    var includeCurrentDate: Boolean
    var initialPositionIndex: Int
    var dayNameFormat: Int
    var weekendItemLayoutId: Int
    var weekendSelectedItemLayoutId: Int
    var firstSpecialItemLayoutId: Int
    var firstSelectedSpecialItemLayoutId: Int
    var secondSpecialItemLayoutId: Int
    var secondSelectedSpecialItemLayoutId: Int
    var thirdSpecialItemLayoutId: Int
    var thirdSelectedSpecialItemLayoutId: Int
    var scrollPosition = 0

    init {
        itemAnimator = null // this remove blinking when clicking items

        context.theme.obtainStyledAttributes(attrs, R.styleable.SingleRowCalendar, 0, 0).apply {

            try {
                itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                dateTextViewId = getResourceId(R.styleable.SingleRowCalendar_dateTextViewId, 0)
                dayTextViewId = getResourceId(R.styleable.SingleRowCalendar_dayTextViewId, 0)
                monthTextViewId = getResourceId(R.styleable.SingleRowCalendar_monthTextViewId, 0)
                selectedItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_selectedItemLayoutId, 0)
                pastDaysCount = getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)
                futureDaysCount = getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)
                includeCurrentDate =
                    getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)
                initialPositionIndex =
                    getInt(R.styleable.SingleRowCalendar_initialPositionIndex, pastDaysCount)
                dayNameFormat = getInt(R.styleable.SingleRowCalendar_dayNameFormat, 3)
                multiSelection = getBoolean(R.styleable.SingleRowCalendar_multiSelection, false)
                disableUnselection =
                    getBoolean(R.styleable.SingleRowCalendar_disableUnselection, true)
                enableLongPress = getBoolean(R.styleable.SingleRowCalendar_enableLongPress, false)
                weekendItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_weekendItemLayoutId, 0)
                weekendSelectedItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_weekendSelectedItemLayoutId, 0)
                firstSpecialItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_firstSpecialItemLayoutId, 0)
                firstSelectedSpecialItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_firstSelectedSpecialItemLayoutId, 0)
                secondSpecialItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_secondSpecialItemLayoutId, 0)
                secondSelectedSpecialItemLayoutId = getResourceId(
                    R.styleable.SingleRowCalendar_secondSelectedSpecialItemLayoutId, 0
                )
                thirdSpecialItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_thirdSpecialItemLayoutId, 0)
                thirdSelectedSpecialItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_thirdSelectedSpecialItemLayoutId, 0)

            } finally {
                recycle()
            }
        }


    }

    fun init() {

        this.apply {

            if (dateList.isNullOrEmpty()) {
                dateList.clear()
                dateList.addAll(loadDates(pastDaysCount, futureDaysCount, includeCurrentDate))
            }


            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            (this.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                initialPositionIndex,
                0
            )

            setHasFixedSize(true)

            val singleRowCalendarAdapter = SingleRowCalendarAdapter(
                dateList,
                firstSpecialItemPositionList,
                secondSpecialItemPositionList,
                thirdSpecialItemPositionList,
                itemLayoutId,
                dateTextViewId,
                dayTextViewId,
                monthTextViewId,
                selectedItemLayoutId,
                dayNameFormat,
                weekendItemLayoutId,
                weekendSelectedItemLayoutId,
                firstSpecialItemLayoutId,
                firstSelectedSpecialItemLayoutId,
                secondSpecialItemLayoutId,
                secondSelectedSpecialItemLayoutId,
                thirdSpecialItemLayoutId,
                thirdSelectedSpecialItemLayoutId
            )

            adapter = singleRowCalendarAdapter
            initSelection()

            SingleRowCalendarAdapter.selectionTracker = selectionTracker


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    scrollPosition =
                        (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

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

                // todo druha cat podmienky
                if (key != GHOST_ITEM_KEY && key.toInt() < dateList.size)
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
        calendarChangesObserver?.whenMonthAndYearChanged(
            DateHelper.getMonthNumber(dateList[scrollPosition]),
            DateHelper.getMonthName(dateList[scrollPosition]),
            DateHelper.getYear(dateList[scrollPosition])
        )
    }
}