package com.remcode.coloursforyou

import android.view.Gravity
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.remcode.coloursforyou.presentation.MainActivity
import com.remcode.coloursforyou.utils.EspressoIdlingResource
import org.junit.*

class ColourGeneratorActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun startVisibilityTest() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
        onView(withId(R.id.splat)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.colour_name)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.paint_and_brush_layout)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
    }

    @Test
    fun clickOnFabGetsANewColour() {
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.splat)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.colour_name)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.paint_and_brush_layout)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun openNavigation_clickOnMyColoursItem_openFragment_closeApp() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())

        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_my_colours))

        onView(withId(R.id.my_colours_fragment_layout)).check(matches(isDisplayed()))

        pressBackUnconditionally()

        Assert.assertTrue(activityRule.activity.isFinishing)
    }
}