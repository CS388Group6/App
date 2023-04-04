package com.cs388group6.packer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "bags", foreignKeys = [ForeignKey(entity = TripEntity::class, parentColumns = ["id"], childColumns = ["tripID"], onDelete = CASCADE)])
data class BagEntity (
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tripID") val tripID: Int,
    @ColumnInfo(name = "bagName") val bagName: String?
)