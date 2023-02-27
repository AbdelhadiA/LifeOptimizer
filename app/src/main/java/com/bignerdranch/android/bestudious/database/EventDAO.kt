package database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ecustudent.roomdatabase.Event

@Dao
interface EventDAO {
    @Query("SELECT * FROM Event")
    fun getEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM Event WHERE id=(:id)")
    fun getEvent(id: Int): LiveData<Event?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(event: Event)

}