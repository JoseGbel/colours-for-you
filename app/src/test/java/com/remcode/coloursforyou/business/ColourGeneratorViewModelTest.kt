package com.remcode.coloursforyou.business

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.testUtils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class ColourGeneratorViewModelTest {

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
    lateinit var repository: MainRepository

    lateinit var mainViewModel: ColourGeneratorViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mainViewModel = ColourGeneratorViewModel(application, repository, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun should_updateWordLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // given
        Mockito.`when`(repository.getRandomWord()).thenReturn(listOf("aWord"))

        val mockWordListObserver = mock(Observer::class.java) as Observer<List<String>>
        mainViewModel.word.observeForever(mockWordListObserver)

        // when
        mainViewModel.getRandomWord("#FFFFFF")

        // then
        verify(mockWordListObserver, times(1)).onChanged(listOf("aWord"))
        Assert.assertEquals(mainViewModel.word.value, listOf("aWord"))
    }

    @Test
    fun should_updateLoadingLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // given
        Mockito.`when`(repository.getRandomWord()).thenReturn(listOf("aWord"))

        val mockLoadingObserver = mock(Observer::class.java) as Observer<Boolean>
        mainViewModel.loading.observeForever(mockLoadingObserver)

        // when
        mainViewModel.getRandomWord("#FFFFFF")

        // then
        val inOrder = inOrder(mockLoadingObserver)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(false)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(true)
        inOrder.verify(mockLoadingObserver, times(1)).onChanged(false)
    }

    @Test
    fun should_updatePlaySplatFxLiveData_when_getRandomWord() = coroutineTestRule.testDispatcher.runBlockingTest {
        // given
        Mockito.`when`(repository.getRandomWord()).thenReturn(listOf("aWord"))

        val mockSplatFxObserver = mock(Observer::class.java) as Observer<Boolean>
        mainViewModel.playSplatFx.observeForever(mockSplatFxObserver)

        // when
        mainViewModel.getRandomWord("#FFFFFF")

        // then
        verify(mockSplatFxObserver, times(2)).onChanged(false)
        Assert.assertEquals(mainViewModel.playSplatFx.value, true)
//        inOrder.verify(mockSplatFxObserver, times(1)).onChanged(true)
    }

    @Test
    fun should_returnColourString_when_getRandomColour() {
        val colour = mainViewModel.getRandomHexColour()

        Assert.assertNotNull(colour)
    }
}