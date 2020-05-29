package com.remcode.coloursforyou.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.remcode.coloursforyou.data.models.Colour

@Dao
interface ColourDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertColour(colour: Colour)

    @Query("delete from colours")
    fun deleteAllColours()

    @Query("select * from colours")
    fun getColours() : LiveData<List<Colour>>
}
