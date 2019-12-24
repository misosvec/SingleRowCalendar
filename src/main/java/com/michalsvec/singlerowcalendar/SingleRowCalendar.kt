package com.michalsvec.singlerowcalendar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.selection.CalendarDetailsLookup
import com.michalsvec.singlerowcalendar.selection.CalendarKeyProvider
import java.util.*


class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {



    private val GHOST_ITEM_KEY = -999999999999999299

    private lateinit var selectionTracker: SelectionTracker<Long>
    private var calendarChangesObserver: CalendarChangesObserver? = null
    private var previousMonthNumber = ""
    private var previousYear = ""
    private var multiSelection: Boolean = false //default value
    private var disableUnselection: Boolean = true // default value
    private var enableLongPress: Boolean = false // default val
    private val dateList: MutableList<Date> = mutableListOf()


    init {
        itemAnimator = null // this remove blinking when clicking items

        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SingleRowCalendar, 0, 0
        ).apply {

            try {
                val itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                val dateTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_dateTextViewId, 0)
                val dayTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_dayTextViewId, 0)

                val monthTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_monthTextViewId, 0)


                val selectedItemLayoutId =
                    getResourceId(R.styleable.SingleRowCalendar_selectedItemLayoutId, 0)

                val pastDaysCount =
                    getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)


                val futureDaysCount =
                    getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)

                val includeCurrentDate =
                    getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)


                val initialPositionIndex =
                    getInt(R.styleable.SingleRowCalendar_initialPositionIndex, pastDaysCount)

                val dayNameFormat =
                    getInt(R.styleable.SingleRowCalendar_dayNameFormat, 3)

                multiSelection =
                    getBoolean(R.styleable.SingleRowCalendar_multiSelection, false)

                disableUnselection =
                    getBoolean(R.styleable.SingleRowCalendar_disableUnselection, true)

                enableLongPress =
                    getBoolean(R.styleable.SingleRowCalendar_enableLongPress, false)


                val weekendItemLayoutId = getResourceId(R.styleable.SingleRowCalendar_weekendItemLayoutId, 0)

                val weekendSelectedItemLayoutId = getResourceId(R.styleable.SingleRowCalendar_weekendSelectedItemLayoutId, 0)

                if (itemLayoutId != 0 && dateTextViewId != 0 && dayTextViewId != 0) {
                    init(
                        itemLayoutId,
                        dateTextViewId,
                        dayTextViewId,
                        monthTextViewId,
                        selectedItemLayoutId,
                        futureDaysCount,
                        pastDaysCount,
                        includeCurrentDate,
                        initialPositionIndex,
                        dayNameFormat,
                        weekendItemLayoutId,
                        weekendSelectedItemLayoutId
                    )

                }
            } finally {
                recycle()
            }
        }


    }


    private fun init(
        itemLayoutId: Int,
        dateTextViewId: Int,
        dayTextViewId: Int,
        monthTextViewId: Int,
        selectedItemLayoutId: Int,
        futureDaysCount: Int,
        pastDaysCount: Int,
        includeCurrentDate: Boolean,
        initialScrollPosition: Int,
        dayNameFormat: Int,
        weekendItemLayoutId: Int,
        weekendSelectedLayoutId: Int
    ) {

        this.apply {

            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            dateList.clear()
            dateList.addAll(loadDates(pastDaysCount, futureDaysCount, includeCurrentDate))

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            (this.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(initialScrollPosition, 0)

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
                weekendSelectedLayoutId
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

        if(!enableLongPress)
            disableLongPress()

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>(){
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                calendarChangesObserver?.whenSelectionChanged(selected, key.toInt(), dateList[key.toInt()])
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

    private fun disableLongPress() = selectionTracker.select(GHOST_ITEM_KEY)


    fun clearSelection() {
        selectionTracker.clearSelection()
        if (!enableLongPress)
            disableLongPress()
    }

    //WORKS
    fun select(position: Int) = selectionTracker.select(position.toLong())

    //WORKS
    fun setItemsSelected(positionList: List<Int>, selected: Boolean){
        val longList = positionList.map { it.toLong()}
        selectionTracker.setItemsSelected(longList, selected)
    }

    //WORKS
    fun deselect(position: Int) = selectionTracker.deselect(position.toLong())

    //WORKS
    fun isSelected(position: Int) = selectionTracker.isSelected(position.toLong())

    //WORKS
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
    fun getSelectedIndexes() : List<Int>{
        val selectionList: MutableList<Int> = mutableListOf()
        selectionTracker.selection.forEach {
            if (it != GHOST_ITEM_KEY && it.toInt() < dateList.size)
                selectionList.add(it.toInt())
        }
        return selectionList
    }






}