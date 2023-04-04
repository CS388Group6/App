package com.cs388group6.packer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags", foreignKeys = [androidx.room.ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userID"], onDelete = androidx.room.ForeignKey.CASCADE)])
data class TagEntity (
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userID") val userID: Int,
    @ColumnInfo(name = "name") val name: String?
)