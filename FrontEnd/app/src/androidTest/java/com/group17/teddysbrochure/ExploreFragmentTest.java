package com.group17.teddysbrochure;

import androidx.test.core.app.ActivityScenario;
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
public class ExploreFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    // Navigates to the Explore page
    @Before
    public void instantiateExploreFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
        onView(withId(R.id.nav_explore)).perform(click()); // travel to explore fragment
        Thread.sleep(1000);
    }

    // Tests that first attraction bubble is displayed
    @Test
    public void attractionBubble1Display() {
        onView(withId(R.id.attractionButton1)).check(matches(isDisplayed()));
        onView(withId(R.id.attractionTitle1)).check(matches(isDisplayed()));
        onView(withId(R.id.attractionImage1)).check(matches(isDisplayed()));
    }

    // Tests that second attraction bubble is displayed
    @Test
    public void attractionBubble2Display() {
        onView(withId(R.id.attractionButton2)).check(matches(isDisplayed()));
        onView(withId(R.id.attractionTitle2)).check(matches(isDisplayed()));
        onView(withId(R.id.attractionImage2)).check(matches(isDisplayed()));
    }

    // Tests that first attraction page is launched upon attraction bubble click
    @Test
    public void testLaunchAttractionPage1() {
        onView(withId(R.id.attractionButton1)).perform(click());
        onView(withId(R.id.attractionsActivity1)).check(matches(isDisplayed()));
    }

    // Tests that second attraction page is launched upon attraction bubble click
    @Test
    public void testLaunchAttractionPage2() {
        onView(withId(R.id.attractionButton2)).perform(click());
        onView(withId(R.id.attractionsActivity2)).check(matches(isDisplayed()));
    }

    // Tests that the text is displayed for the park overview
    @Test
    public void parkOverviewDisplay() {
        onView(withId(R.id.parkOverviewTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.parkOverviewBody)).check(matches(isDisplayed()));
    }

}
