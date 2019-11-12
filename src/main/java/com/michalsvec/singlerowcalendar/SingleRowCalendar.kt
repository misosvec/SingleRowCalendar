package com.michalsvec.singlerowcalendar

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.michalsvec.singlerowcalendar.selection.CalendarDetailsLookup
import com.michalsvec.singlerowcalendar.selection.CalendarKeyProvider
import java.util.*

class SingleRowCalendar(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private lateinit var selectionTracker: SelectionTracker<Long>


    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SingleRowCalendar,
            0, 0
        ).apply {

            try {
                val itemLayoutId = getResourceId(R.styleable.SingleRowCalendar_itemLayoutId, 0)
                val itemDateTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDateTextViewId, 0)
                val itemDayTextViewId =
                    getResourceId(R.styleable.SingleRowCalendar_itemDayTextViewId, 0)

                val pastDaysCount =
                    getInt(R.styleable.SingleRowCalendar_pastDaysCount, 0)


                val futureDaysCount =
                    getInt(R.styleable.SingleRowCalendar_futureDaysCount, 30)

                val includeCurrentDate =
                getBoolean(R.styleable.SingleRowCalendar_includeCurrentDate, true)



                if (itemLayoutId != 0 && itemDateTextViewId != 0 && itemDayTextViewId != 0) {
                    init(
                        itemLayoutId = itemLayoutId,
                        itemDateTextViewId = itemDateTextViewId,
                        itemDayTextViewId = itemDayTextViewId,
                        futureDaysCount = futureDaysCount,
                         pastDaysCount = pastDaysCount,
                        includeCurrentDate = includeCurrentDate
                    )
                }
            } finally {
                recycle()
            }
        }
    }


    private fun init(itemLayoutId: Int, itemDateTextViewId: Int, itemDayTextViewId: Int, futureDaysCount: Int, pastDaysCount: Int,includeCurrentDate: Boolean) {
            this.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SingleRowCalendarAdapter(
                loadDates(pastDaysCount, futureDaysCount, includeCurrentDate),
                itemLayoutId,
                itemDateTextViewId,
                itemDayTextViewId
            )
                initSelection()
                initSelectionObserver()
        }
    }



    private fun loadDates(pastDays: Int, futureDays: Int, includeCurrentDate: Boolean): List<Date> {
        val futureList = DateHelper.getFutureDates(futureDays)
        val cal = Calendar.getInstance(Locale.getDefault())
        val pastList = DateHelper.getPastDates(pastDays).reversed()
        val list = if(includeCurrentDate) pastList + cal.time +  futureList else pastList  +  futureList
        Log.d("ddd", "past  lenght ${pastList.size} a future ${futureList.size}")
        return list

    }


    private fun initSelection(){
        selectionTracker = SelectionTracker.Builder(
            "singleRowCalendarSelectionTracker",
            this,
            CalendarKeyProvider(this),
            CalendarDetailsLookup(this),
            StorageStrategy.createLongStorage()
        ).build()
//            .withSelectionPredicate(predictate).build()
//        selectionObserver()
    }


    private fun initSelectionObserver() =
        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {
               Log.d("ddd", "selected value is $selected")
                super.onItemStateChanged(key, selected)
            }
        })
//
//    private fun setupRecyclerView(it: RideList) {
//        binding.ride = it.Rides[0]
//        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        binding.rvRidesReservation.layoutManager = layoutManager
//        binding.rvRidesReservation.setHasFixedSize(true)
//        adapter = ReservationRecyclerViewAdapter(it.Rides, this)
//        binding.rvRidesReservation.itemAnimator = null
//        binding.rvRidesReservation.layoutAnimation = null
//        binding.rvRidesReservation.adapter = adapter
//
//
//        val predictate = object : SelectionTracker.SelectionPredicate<Ride>() {
//            override fun canSelectMultiple(): Boolean =
//                adapter.isClickable
//
//
//            override fun canSetStateForKey(key: Ride, nextState: Boolean): Boolean =
//                adapter.isClickable
//
//
//            override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean =
//                adapter.isClickable
//
//        }
//        tracker = SelectionTracker.Builder(
//            "reservationSelectionTracker",
//            binding.rvRidesReservation,
//            RideKeyProvider(it.Rides),
//            RideDetailsLookup(binding.rvRidesReservation),
//            StorageStrategy.createParcelableStorage(Ride::class.java)
//        ).withSelectionPredicate(predictate).build()
//        selectionObserver()
//
//        for (i in it.Rides.indices) {
//            if (it.Rides[i].ID == rideId) {
//                binding.rvRidesReservation.layoutManager!!.scrollToPosition(i)
//                tracker.select(it.Rides[i])
//            }
//        }
//    }





}