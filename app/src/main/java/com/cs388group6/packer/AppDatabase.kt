package com.cs388group6.packer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BagEntity::class, ItemEntity::class, UserEntity::class, TagEntity::class, TripEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun bagDao(): BagDao
    abstract fun itemDao(): ItemDao
    abstract fun userDao(): UserDao
    abstract fun tagDao(): TagDao
    abstract fun tripDao(): TripDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "packer-db"
            ).build()
    }
}
