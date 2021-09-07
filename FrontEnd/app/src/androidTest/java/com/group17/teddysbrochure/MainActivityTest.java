package com.group17.teddysbrochure;

import android.widget.ListView;

import androidx.test.espresso.action.TypeTextAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void beginMainActivity() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
    }

    @Test
    public void testDisplayBottomNavigation() {
        onView(withId(R.id.nav_home)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_maps)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_explore)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_reviews)).check(matches(isDisplayed()));
        onView(withId(R.id.nav_history)).check(matches(isDisplayed()));
    }

    @Test
    public void testHomeClickBottomNav() {
        onView(withId(R.id.nav_home)).perform(click());
        onView(withId(R.id.home_page)).check(matches(isDisplayed()));
    }

    @Test
    public void testMapsClickBottomNav() {
        onView(withId(R.id.nav_maps)).perform(click());
        onView(withId(R.id.fullMapsComponent)).check(matches(isDisplayed()));
    }

    @Test
    public void testExploreClickBottomNav() {
        onView(withId(R.id.nav_explore)).perform(click());
        onView(withId(R.id.explore_page)).check(matches(isDisplayed()));
    }

    @Test
    public void testReviewsClickBottomNav() {
        onView(withId(R.id.nav_reviews)).perform(click());
        onView(withId(R.id.reviews_page)).check(matches(isDisplayed()));
    }

    @Test
    public void testHistoryClickBottomNav() {
        onView(withId(R.id.nav_history)).perform(click());
        onView(withId(R.id.history_page)).check(matches(isDisplayed()));
    }

    @Test
    public void testOpenSearch() {
        onView(withId(R.id.search_icon)).perform(click());
        onView(withId(R.id.searchParkWrapper)).check(matches(isDisplayed()));
    }

//    TODO: @Test
//    public void testSearchTyping() {
//        onView(withId(R.id.search_icon)).perform(click());
//        onView(withId(R.id.searchParkWrapper)).perform(click());
//    }

    @Test
    public void testDisplayParksSearch() {
        onView(withId(R.id.search_icon)).perform(click());
        onView(withId(R.id.my_list)).check(matches(isDisplayed()));
    }

//   TODO: @Test
//    public void testSwitchParks() {
//
//        onView(withId(R.id.search_icon)).perform(click());
//        onView(withId(R.id.my_list)).perform(click());
//    }


}
