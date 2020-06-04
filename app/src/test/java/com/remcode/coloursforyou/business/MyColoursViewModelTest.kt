package com.remcode.coloursforyou.business

import android.app.Application
import com.nhaarman.mockitokotlin2.verify
import com.remcode.coloursforyou.data.local.ColourDao
import com.remcode.coloursforyou.data.repository.MainRepository
import com.remcode.testUtils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MyColoursViewModelTest {

    @ExperimentalCoroutinesApi
    @JvmField
    @Rule
    val coroutineTestRule = CoroutineTestRule()

    @Mock
    lateinit var application : Application

    @Mock
    lateinit var repository : MainRepository

    lateinit var SUT : MyColoursViewModel

    @Before
    fun setUp() {
        SUT = MyColoursViewModel(application, repository, coroutineTestRule.testDispatcherProvider)
    }

    @Test
    fun deleteAll_successfullyCallsMethodInRepository() = coroutineTestRule.testDispatcher.runBlockingTest {
        // When
        SUT.deleteAll()
        // Then
        verify(repository).deleteColours()
    }
}