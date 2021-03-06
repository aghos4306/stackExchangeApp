package com.aghogho.stackexchangeapp.view

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aghogho.stackexchangeapp.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class QuestionsListFragmentTest {

    @Before
    fun setup() {
        launchFragmentInContainer(Bundle(), androidx.appcompat.R.style.Theme_AppCompat) {
            QuestionsListFragment()
        }
    }

    @Test
    fun test_isSearchEditTextVisible() {
        onView(
            withId(R.id.searchET)
        ).check(matches(isDisplayed()))
    }

    @Test
    fun test_isTrendingQuestionTextViewCorrect() {
        onView(
            withId(R.id.search_key_text)
        ).check(matches(isDisplayed()))
    }

    @Test
    fun test_isRecyclerViewQuestionsTrendingVisible() {
        onView(
            withId(R.id.questions_trending_rv)
        ).check(matches(isDisplayed()))
    }

}