package com.cs388group6.packer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "items", foreignKeys = [ForeignKey(entity = TagEntity::class, parentColumns = ["id"], childColumns = ["tagID"], onDelete = CASCADE),
ForeignKey(entity = BagEntity::class, parentColumns = ["id"], childColumns = ["bagID"], onDelete = CASCADE)])
data class ItemEntity(
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "tagID") val tagID: Int,
    @ColumnInfo(name = "bagID") val bagID: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "quantity") val quantity: Int
)
