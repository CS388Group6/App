package com.cs388group6.packer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BagDao {
    @Query("SELECT * FROM bags")
    fun getALL(): Flow<List<BagEntity>>

    @Insert
    fun insert(bag: BagEntity)

    @Delete
    fun delete(bag: BagEntity)
}