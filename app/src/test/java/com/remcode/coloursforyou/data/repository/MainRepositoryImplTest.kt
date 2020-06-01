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
import org.mockito.*

@ExperimentalCoroutinesApi
class MainRepositoryImplTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val HEXCOLOUR: String = "#FFFFFF"
    private val NAME: String = "aName"
    private val COLOUR = Colour(HEXCOLOUR, NAME)

    @Mock
    lateinit var colourDaoMock: ColourDao
    @Mock
    lateinit var colourNameServiceMock: ColourNamesService

    private lateinit var SUT : MainRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        SUT = MainRepositoryImpl(colourNameServiceMock, colourDaoMock)
    }

    @Test
    fun deleteColours_callsMethodInTheDaoObject() = coroutineTestRule.testDispatcher.runBlockingTest {
        SUT.deleteColours()
        verify(colourDaoMock).deleteAllColours()
        verifyNoMoreInteractions(colourNameServiceMock)
    }

    @Test
    fun getRandomWord_callsMethodCorrectlyInTheRemoteApi() = coroutineTestRule.testDispatcher.runBlockingTest {
        SUT.getRandomWord()
        verify(colourNameServiceMock).getRandomWord()
    }

    @Test
    fun insertColour_passRightArgumentToColourDao() = coroutineTestRule.testDispatcher.runBlockingTest {
        val colourCaptor = argumentCaptor<Colour>()
        SUT.insertColour(COLOUR)
        verify(colourDaoMock).insertColour(colourCaptor.capture())
        verifyNoMoreInteractions(colourNameServiceMock)
        assertEquals(colourCaptor.firstValue, COLOUR)
    }
}