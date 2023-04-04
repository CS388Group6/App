package com.cs388group6.packer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips", foreignKeys = [ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userID"], onDelete = CASCADE)])
data class TripEntity (
        @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "userID") val userID: Int,
        @ColumnInfo(name = "destination") val destination : String?,
        @ColumnInfo(name = "startDate") val startDate: Date,
        @ColumnInfo(name = "endDate") val endDate: Date,
        @ColumnInfo(name = "name") val name: String?
)