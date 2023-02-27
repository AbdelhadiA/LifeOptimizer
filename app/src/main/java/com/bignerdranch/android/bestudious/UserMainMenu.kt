package com.bignerdranch.android.lifeoptimizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.util.*

private const val TAG = "UserMainMenu"
class UserMainMenu : AppCompatActivity(), TaskListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_main_menu)


        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val fragments = TaskListFragment.newInstance()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragments)
                .commit()


        }


    }

    override fun onTaskSelected(taskId: UUID) {
        Log.d(TAG, "UserMainMenu.OnTaskSelected: $taskId")
        val fragment = TaskFragment.newInstance(taskId)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
             .addToBackStack(null)
             .commit()
    }

}