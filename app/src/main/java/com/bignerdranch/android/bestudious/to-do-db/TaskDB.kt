package com.bignerdranch.android.lifeoptimizer.to

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.lifeoptimizer.Task



@Database(entities = [Task::class],version=2)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDB : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}

val migration_1_2=object: Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE TASK ADD COLUMN helper TEXT NOT NULL DEFAULT ''"
        )
    }
}