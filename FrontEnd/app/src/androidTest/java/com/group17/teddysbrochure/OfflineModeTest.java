package com.group17.teddysbrochure;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class OfflineModeTest {

    static CustomUtil util;
    static String parkFile = "nationalParks.json";
    static String weatherFile = "weather.json";
    static String reviewFile = "review.json";
    static String attractionFile = "attraction.json";
    static String historyFile = "parkHistory.json";

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void OfflineModeTest() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        util = CustomUtil.getInstance(mainActivity.getActivity().getApplicationContext());
        util.writeToInternalStorage(parkFile, "");
        util.writeToInternalStorage(weatherFile, "");
        util.writeToInternalStorage(reviewFile, "");
        util.writeToInternalStorage(attractionFile, "");
        util.writeToInternalStorage(historyFile, "");
    }

    @Test
    public void testHome() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.nav_home)).perform(click()); // travel to review fragment
        Thread.sleep(1000);

        String park = util.readFromInternalStorage(parkFile);
        String weather = util.readFromInternalStorage(weatherFile);

        assertEquals(false, park.trim().isEmpty());
        assertEquals(false, weather.trim().isEmpty());
    }

    @Test
    public void testReview() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.nav_reviews)).perform(click()); // travel to review fragment
        Thread.sleep(1000);

        String review = util.readFromInternalStorage(reviewFile);
        assertEquals(false, review.trim().isEmpty());
    }

    @Test
    public void testAttraction() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.nav_explore)).perform(click()); // travel to review fragment
        Thread.sleep(1000);

        String attract = util.readFromInternalStorage(attractionFile);
        assertEquals(false, attract.trim().isEmpty());
    }

    @Test
    public void testHistory() throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.nav_history)).perform(click()); // travel to review fragment
        Thread.sleep(1000);

        String hisotry = util.readFromInternalStorage(historyFile);
        assertEquals(false, hisotry.trim().isEmpty());
    }
}
