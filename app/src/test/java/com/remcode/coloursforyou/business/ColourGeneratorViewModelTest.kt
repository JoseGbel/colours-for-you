package com.remcode.coloursforyou.business

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remcode.TestData
import com.remcode.coloursforyou.business.ColourGeneratorViewModel.*
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.testUtils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.*
import org.mockito.internal.hamcrest.HamcrestArgumentMatcher


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ColourGeneratorViewModelTest {

    private val HEXCOLOUR: String = TestData.HEXCOLOUR
    private val COLOUR: Colour = TestData.COLOUR
    private val COLOURNAME = TestData.COLOURNAME
    private val COLOURNAMES = TestData.COLOURNAMES

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @JvmField
    @Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var applicationMock: Application

    @Mock
    lateinit var repositoryMock: MainRepository

    lateinit var SUT: ColourGeneratorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        SUT = ColourGeneratorViewModel(applicationMock, repositoryMock, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun getRandomWord_callsMethodInRepository() = coroutineTestRule.testDispatcher.runBlockingTest {
        // When
        SUT.fetchNewColour(HEXCOLOUR)
        // Then
        verify(repositoryMock).getRandomWord()
    }

    @Test
    fun insert_callsMethodInRepositoryWithRightParams() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        `when`(repositoryMock.getRandomWord()).thenReturn(COLOURNAMES)
        val ac : KArgumentCaptor<Colour> = argumentCaptor()
        // When
        SUT.insert(COLOUR)
        // Then
        verify(repositoryMock).insertColour(ac.capture())
        assertEquals(ac.firstValue.name, COLOURNAME)
        assertEquals(ac.firstValue.hexColour, HEXCOLOUR)
    }

    @Test
    fun getRandomWord_success_updatesLiveData() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        val mockWordListObserver = mock(Observer::class.java) as Observer<List<String>>
        `when`(repositoryMock.getRandomWord()).thenReturn(COLOURNAMES)
        SUT.wordLiveData.observeForever(mockWordListObserver)
        // When
        SUT.fetchNewColour(HEXCOLOUR)
        // Then
        verify(mockWordListObserver, times(1)).onChanged(COLOURNAMES)
        assertEquals(SUT.wordLiveData.value, COLOURNAMES)
    }

    @Test
    fun getRandomColour_success_updatesLoadingLiveData() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        `when`(repositoryMock.getRandomWord()).thenReturn(COLOURNAMES)
        val mockLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        SUT.loadingLiveData.observeForever(mockLoadingObserver)
        // When
        SUT.fetchNewColour(HEXCOLOUR)
        // Then
        val inOrder = inOrder(mockLoadingObserver)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(false)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(true)
    }

    @Test
    fun getRandomColour_success_updatePlaySplatFxLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        val ac = argumentCaptor<Command>()
        `when`(repositoryMock.getRandomWord()).thenReturn(COLOURNAMES)
        val observerMock = mock(Observer::class.java) as Observer<Command>
        SUT.command.observeForever(observerMock)
        // When
        SUT.fetchNewColour(HEXCOLOUR)
        // Then
        verify(observerMock).onChanged(ac.capture())
        assertThat(ac.allValues[0], instanceOf(Command.PlaySoundEffect::class.java))
    }

    @Test
    fun getRandomHexColour_returnColourString() {
        val colour = SUT.generateRandomHexColour()
        assertEquals(colour.count(), 7)
    }
}