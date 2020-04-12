package com.example.catfact

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun verifyComponentsAreVisible() {
        onView(withId(R.id.more_facts_button))
            .check(matches(isDisplayed()))

        onView(withId(R.id.cats_ids_list))
            .check(matches(isDisplayed()))

        onView(withId(R.id.cats_ids_list))
            .perform(click())

        onView(withId(R.id.details_text))
            .check(matches(isDisplayed()))

        onView(withId((R.id.date_text)))
            .check(matches(isDisplayed()))
    }
}