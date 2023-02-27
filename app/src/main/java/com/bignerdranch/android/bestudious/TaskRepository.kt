package com.bignerdranch.android.lifeoptimizer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.lifeoptimizer.to.TaskDB
import com.bignerdranch.android.lifeoptimizer.to.migration_1_2
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "task-database"
class TaskRepository private constructor(context: Context) {

    private val database : TaskDB = Room.databaseBuilder(
        context.applicationContext,
        TaskDB::class.java,
        DATABASE_NAME
    ).addMigrations(migration_1_2).build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks(): LiveData<List<Task>> = taskDao.getTasks()
    fun getTask(id: UUID): LiveData<Task?> = taskDao.getTask(id)

    fun updateTask(task: Task) {
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }
    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?:
            throw IllegalStateException("TaskRepository must be initialized")
        }
    }

}