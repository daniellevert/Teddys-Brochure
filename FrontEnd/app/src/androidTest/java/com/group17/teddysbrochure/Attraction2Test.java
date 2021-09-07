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
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Attraction2Test {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    // Navigates to the Explore page
    @Before
    public void instantiateExploreFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
        onView(withId(R.id.nav_explore)).perform(click()); // travel to explore fragment
        Thread.sleep(1000);
        onView(withId(R.id.attractionButton2)).perform(click()); // open Attraction page 1
        Thread.sleep(1000);
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionTitleDisplayed() {
        onView(withId(R.id.viewAttractionTitle)).check(matches(isDisplayed()));
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionTitleContent() {
        onView(withText("Telescope Peak")).check(matches(isDisplayed()));
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionBodyDisplayed() {
        onView(withId(R.id.viewAttractionBody)).check(matches(isDisplayed()));
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionBodyContent() {
        onView(withText("Telescope Peak Trail is a 12 mile moderately trafficked out and back trail located near Death Valley, California that features a great forest setting and is rated as difficult. The trail is primarily used for hiking and is best used from June until October.")).check(matches(isDisplayed()));
    }


}
