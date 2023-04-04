package com.cs388group6.packer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getALL(): Flow<List<UserEntity>>

    @Insert
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}