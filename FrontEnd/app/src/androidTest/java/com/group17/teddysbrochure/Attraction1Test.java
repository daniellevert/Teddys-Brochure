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
public class Attraction1Test {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    // Navigates to the Explore page
    @Before
    public void instantiateExploreFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
        onView(withId(R.id.nav_explore)).perform(click()); // travel to explore fragment
        Thread.sleep(1000);
        onView(withId(R.id.attractionButton1)).perform(click()); // open Attraction page 1
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
        onView(withText("Journigan's Mill")).check(matches(isDisplayed()));
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionBodyDisplayed() {
        onView(withId(R.id.viewAttractionBody)).check(matches(isDisplayed()));
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionBodyContent() {
        onView(withText("Journigan’s Mill was built in the 1930’s at a site in Emigrant Canyon that was near several springs. Water is Death Valley’s most precious resource, and mills were usually located where there was access to springs nearby. During the course of the mills roughly twenty years of operation, water would be piped to the site from up to four different springs.")).check(matches(isDisplayed()));
    }


}
