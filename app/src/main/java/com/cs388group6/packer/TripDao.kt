package com.cs388group6.packer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips")
    fun getALL(): Flow<List<TripEntity>>

    @Insert
    fun insert(trip: TripEntity)

    @Delete
    fun delete(trip: TripEntity)
}