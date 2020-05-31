package com.remcode.coloursforyou.data.repository

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.remcode.coloursforyou.data.local.ColourDao
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.network.ColourNamesService
import com.remcode.testUtils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainRepositoryImplTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val HEXCOLOUR: String = "#FFFFFF"
    private val NAME: String = "aName"

    lateinit var SUT : MainRepositoryImpl

    @Mock
    lateinit var colourDao: ColourDao

    @Mock
    lateinit var colourNameService: ColourNamesService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        SUT = MainRepositoryImpl(colourNameService, colourDao)
    }

    @Test
    fun deleteColours_callsMethodInTheDaoObject() = coroutineTestRule.testDispatcher.runBlockingTest {
        SUT.deleteColours()
        verify(colourDao).deleteAllColours()
        verifyNoMoreInteractions(colourNameService)
    }

    @Test
    fun getRandomWord_callsMethodInTheRemoteApi() = coroutineTestRule.testDispatcher.runBlockingTest {
        SUT.getRandomWord()
        verify(colourNameService).getRandomWord()
        verifyNoMoreInteractions(colourDao)
    }


    @Test
    fun insertColour_callsMethodInTheRemoteApi() = coroutineTestRule.testDispatcher.runBlockingTest {
        val ac  = argumentCaptor<Colour>()
        val captor = argumentCaptor<() -> Unit>()
        val colour = Colour(HEXCOLOUR, NAME)
        SUT.insertColour(colour)
        verify(colourDao).insertColour(ac.capture())
        verifyNoMoreInteractions(colourDao)
        assertEquals(ac.firstValue, HEXCOLOUR)
        assertEquals(ac.secondValue, NAME)
    }
}