package com.remcode.coloursforyou.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.remcode.coloursforyou.data.models.Colour
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class ColourDatabaseTest {
    private lateinit var colourDao: ColourDao
    private lateinit var db: ColourDatabase

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ColourDatabase::class.java).build()
        colourDao = db.colourDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeColourAndReadInList()  {
        val colour = Colour("#FFFFFF", "White")
        colourDao.insertColour(colour)

        val colourList = colourDao.getColours()
        Assert.assertEquals(colour, colourList.value?.get(0))
    }
}