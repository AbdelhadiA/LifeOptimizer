package com.bignerdranch.android.lifeoptimizer

import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.lifeoptimizer.R

class CalendarActivity : AppCompatActivity() {
    // Define the variable of CalendarView type
    // and TextView type;
    var calendar: CalendarView? = null
    var date_view: TextView? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        calendar = findViewById(R.id.calendar) as CalendarView?
        date_view = findViewById(R.id.date_view) as TextView?

        // Add Listener in calendar
        calendar
            ?.setOnDateChangeListener(
                object : CalendarView.OnDateChangeListener {
                    // In this Listener have one method
                    // and in this method we will
                    // get the value of DAYS, MONTH, YEARS
                    override fun onSelectedDayChange(
                        @NonNull view: CalendarView,
                        year: Int,
                        month: Int,
                        dayOfMonth: Int
                    ) {

                        // Store the value of date with
                        // format in String type Variable
                        // Add 1 in month because month
                        // index is start with 0
                        val Date = (dayOfMonth.toString() + "-"
                                + (month + 1) + "-" + year)

                        // set this date in TextView for Display
                        date_view?.setText(Date)
                    }
                })
    }
}
