package com.ecustudent.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bignerdranch.android.lifeoptimizer.R
import java.util.*
import java.time.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Example of how to add an Event to the database
        var test = Event(1,"test",Date(""),"12:00","13:00",false,false,false)
        val eventRepository = EventRepository.get()
        eventRepository.insert(test)
    }
}