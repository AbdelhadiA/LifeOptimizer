package com.bignerdranch.android.lifeoptimizer


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MenuActivity : AppCompatActivity() {

    private lateinit var todobutton: Button
    private lateinit var calendarButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        todobutton = findViewById(R.id.new_todo_button)
        calendarButton = findViewById(R.id.calendar_button)


        todobutton.setOnClickListener {
            val intent = Intent(this, UserMainMenu::class.java)
            startActivity(intent)
        }

        calendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }


    }
}
