package com.cs388group6.packer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getALL(): Flow<List<ItemEntity>>

    @Insert
    fun insert(item: ItemEntity)

    @Delete
    fun delete(item: ItemEntity)
}