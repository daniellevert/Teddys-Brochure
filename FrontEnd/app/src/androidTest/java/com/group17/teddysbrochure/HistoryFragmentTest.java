package com.group17.teddysbrochure;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class HistoryFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    // Navigates to the Park History page
    @Before
    public void instantiateHistoryFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
        onView(withId(R.id.nav_history)).perform(click()); // travel to explore fragment
        Thread.sleep(1000);
    }

    // Tests that an image is displayed on the Park History page
    @Test
    public void historyImageDisplay() {
        onView(withId(R.id.parkHistoryImage)).check(matches(isDisplayed()));
    }

    // Tests that history text is displayed
    @Test
    public void historyTextDisplay() {
        onView(withId(R.id.parkHistoryTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.parkHistoryBody)).check(matches(isDisplayed()));
    }
}
