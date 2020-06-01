package com.remcode

import com.remcode.coloursforyou.business.ColourGeneratorViewModelTest
import com.remcode.coloursforyou.business.MyColoursViewModelTest
import com.remcode.coloursforyou.data.repository.MainRepositoryImplTest
import com.remcode.coloursforyou.utils.ExtensionsTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Suite::class)
@Suite.SuiteClasses(
    ColourGeneratorViewModelTest::class,
    MyColoursViewModelTest::class,
    MainRepositoryImplTest::class,
    ExtensionsTest::class
)
class TestSuite