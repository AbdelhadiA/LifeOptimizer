package com.ecustudent.roomdatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Room
import database.EventDatabase

private const val DATABASE_NAME = "event-database"

class EventRepository private constructor(context: Context) {
    private val database : EventDatabase = Room.databaseBuilder(
        context.applicationContext,
        EventDatabase::class.java,
        DATABASE_NAME
    ).build()
    private val EventDAO = database.EventDAO()

    fun getEvents(): LiveData<List<Event>> = EventDAO.getEvents()
    fun getEvents(id: Int): LiveData<Event?> = EventDAO.getEvent(id)
    fun insert(event: Event) = EventDAO.insertEvent(event)

    companion object {
        private var INSTANCE: EventRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = EventRepository(context)
            }
        }
        fun get(): EventRepository {
            return INSTANCE ?:
            throw IllegalStateException("EventRepository must be initialized")
        }
    }
}