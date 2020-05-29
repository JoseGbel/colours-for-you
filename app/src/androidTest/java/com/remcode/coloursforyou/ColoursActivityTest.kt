package com.remcode.coloursforyou

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ColoursActivityTest {

    @Test
    fun visibilityTest() {
        onView(withId(R.id.splat)).check(matches(isDisplayed()))
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnFabGetsANewColour() {
        onView(withId(R.id.fab)).perform(click())
    }
}