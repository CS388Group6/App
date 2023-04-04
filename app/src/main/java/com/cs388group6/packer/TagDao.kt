package com.cs388group6.packer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun getALL(): Flow<List<TagEntity>>

    @Insert
    fun insert(tag: TagEntity)

    @Delete
    fun delete(tag: TagEntity)
}