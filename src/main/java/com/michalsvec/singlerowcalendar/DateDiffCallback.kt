package com.michalsvec.singlerowcalendar

import androidx.recyclerview.widget.DiffUtil
import java.util.*

class DateDiffCallback(private val oldDateList: List<Date>, private val newDateList: List<Date>) :
    DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCal = Calendar.getInstance()
        val newCal = Calendar.getInstance()
        oldCal.time = oldDateList[oldItemPosition]
        newCal.time = newDateList[newItemPosition]

        return oldCal.get(Calendar.YEAR) == newCal.get(Calendar.YEAR) &&
                oldCal.get(Calendar.MONTH) == newCal.get(Calendar.MONTH) &&
                oldCal.get(Calendar.DAY_OF_MONTH) == newCal.get(Calendar.DAY_OF_MONTH)
    }


    override fun getOldListSize(): Int = oldDateList.size

    override fun getNewListSize(): Int = newDateList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCal = Calendar.getInstance()
        val newCal = Calendar.getInstance()
        oldCal.time = oldDateList[oldItemPosition]
        newCal.time = newDateList[newItemPosition]

        return oldCal.get(Calendar.YEAR) == newCal.get(Calendar.YEAR) &&
                oldCal.get(Calendar.MONTH) == newCal.get(Calendar.MONTH) &&
                oldCal.get(Calendar.DAY_OF_MONTH) == newCal.get(Calendar.DAY_OF_MONTH)
    }
}