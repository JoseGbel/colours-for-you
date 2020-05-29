package com.remcode.coloursforyou.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "colours")
data class Colour(
    @ColumnInfo val hexColour: String,
    @ColumnInfo val name: String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var colourId: Long = 0
}