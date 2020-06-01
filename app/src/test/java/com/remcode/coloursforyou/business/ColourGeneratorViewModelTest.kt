package com.remcode.coloursforyou.business

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remcode.coloursforyou.business.ColourGeneratorViewModel.*
import com.remcode.coloursforyou.data.models.Colour
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.testUtils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.*


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ColourGeneratorViewModelTest {

    private val COLOUR: String = "#FFFFFF"
    private val WORD: String = "aWord"

    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @JvmField
    @Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var mockObserver: Observer<Command>

    @Mock
    lateinit var repository: MainRepository

    lateinit var SUT: ColourGeneratorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        SUT = ColourGeneratorViewModel(application, repository, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun getRandomWord_callsMethodInRepository() = coroutineTestRule.testDispatcher.runBlockingTest {
        // When
        SUT.getRandomWord(COLOUR)
        // Then
        verify(repository).getRandomWord()
    }

    @Test
    fun insert_callsMethodInRepositoryWithRightParams() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        `when`(repository.getRandomWord()).thenReturn(listOf(WORD))
        val ac : ArgumentCaptor<Colour> = ArgumentCaptor.forClass(Colour::class.java)
        // When
        SUT.getRandomWord(COLOUR)
        // Then
        verify(repository).insertColour(ac.capture())
        assertEquals(ac.value.name, WORD)
        assertEquals(ac.value.hexColour, COLOUR)
    }

    @Test
    fun getRandomWord_success_updatesLiveData() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        val mockWordListObserver = mock(Observer::class.java) as Observer<List<String>>
        `when`(repository.getRandomWord()).thenReturn(listOf(WORD))
        SUT.word.observeForever(mockWordListObserver)
        // When
        SUT.getRandomWord(COLOUR)
        // Then
        verify(mockWordListObserver, times(1)).onChanged(listOf(WORD))
        assertEquals(SUT.word.value, listOf(WORD))
    }

    @Test
    fun should_updateLoadingLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        `when`(repository.getRandomWord()).thenReturn(listOf(WORD))
        val mockLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        SUT.loading.observeForever(mockLoadingObserver)
        // When
        SUT.getRandomWord(COLOUR)
        // Then
        val inOrder = inOrder(mockLoadingObserver)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(false)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(true)
    }

    @Test
    fun should_updatePlaySplatFxLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // Given
        `when`(repository.getRandomWord()).thenReturn(listOf(WORD))
        SUT.command.observeForever(mockObserver)
        // When
        SUT.getRandomWord(COLOUR)
    }

    @Test
    fun getRandomHexColour_returnColourString() {
        val colour = SUT.getRandomHexColour()
        assertEquals(colour.count(), 7)
    }
}