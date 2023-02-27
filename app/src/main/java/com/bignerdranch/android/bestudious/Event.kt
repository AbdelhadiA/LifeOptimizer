package com.ecustudent.roomdatabase

import androidx.room.*
import java.util.*
import java.time.*

@Entity
    data class Event(@PrimaryKey(autoGenerate = true) val id: Int,
                     var eventName: String = "",
                     var eventDate: Date = Date(),
                     var startTime: String = "",
                     var endTime: String = "",
                     var repeatDaily: Boolean = false,
                    var repeatWeekly: Boolean = false,
                     var repeatMonthly: Boolean = false
)

