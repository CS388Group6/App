package com.cs388group6.packer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userName") val userName: String?,
    @ColumnInfo(name = "firstName") val firstName: String?,
    @ColumnInfo(name = "lastName") val lastName: String?
)