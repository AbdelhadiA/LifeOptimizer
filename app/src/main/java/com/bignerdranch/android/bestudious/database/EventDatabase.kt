package database

import com.ecustudent.roomdatabase.Event
import androidx.room.*

    @Database(entities = [Event::class], version=1)
    @TypeConverters(CrimeTypeConverters::class)
    abstract class EventDatabase : RoomDatabase(){
        abstract fun EventDAO(): EventDAO
    }


