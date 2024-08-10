package com.example.mobileapplication

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.mobileapplication.ui.activity.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginInstrumentTest {
    @get:Rule
    val testRule = ActivityScenarioRule(LoginActivity::class.java)
    @Test
    fun checkSum() {
        onView(withId(R.id.EmailAddressLogin)).perform(
            typeText("binju@gmail.com")
        )

        onView(withId(R.id.PasswordLogin)).perform(
            typeText("password")
        )

        closeSoftKeyboard()

        Thread.sleep(1500)

        onView(withId(R.id.loginbtn)).perform(
            click()
        )

        Thread.sleep(1500)
        onView(withId(R.id.lblStatic)).check(matches(withText("Welcome to Furry World")))
    }
}