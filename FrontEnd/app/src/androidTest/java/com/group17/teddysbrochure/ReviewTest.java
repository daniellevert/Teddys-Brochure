package com.group17.teddysbrochure;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.EspressoKey;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.group17.teddysbrochure.ui.reviews.ReviewsFragment;
import com.group17.teddysbrochure.ui.util.CustomUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ReviewTest {

    @Rule
    public ActivityTestRule <MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void instantiateReviewFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
        Thread.sleep(1000);
        onView(withId(R.id.nav_reviews)).perform(click()); // travel to review fragment
        Thread.sleep(1000);
    }

    @Test
    public void testReviewButton() {
        onView(withId(R.id.createReviewButton)).check(matches(isDisplayed()));
    }

    @Test
    public void listViewDisplay() {
        onView(withId(R.id.ReviewList)).check(matches(isDisplayed()));
    }

    @Test
    public void testForPopup() {
        onView(withId(R.id.createReviewButton)).perform(click());
        onView(withText("Create Review Form")).check(matches(isDisplayed()));
    }

    private String getSaltString() {
        String Salt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * Salt.length());
            salt.append(Salt.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    @Test
    public void autoSubmit() throws InterruptedException {
        onView(withId(R.id.createReviewButton)).perform(click());

        String title = getSaltString();

        onView(withId(R.id.reviewTitleEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.reviewerEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.reviewEditText)).check(matches(isDisplayed()));
        onView(withId(R.id.starEditText)).check(matches(isDisplayed()));

        onView(withId(R.id.reviewTitleEditText)).perform(typeText(title));
        onView(withId(R.id.reviewerEditText)).perform(typeText("Reviewer Text"));
        onView(withId(R.id.reviewEditText)).perform(typeText("Review Text"));
        onView(withId(R.id.starEditText)).perform(typeText("3"));

        onView(withText("OK")).perform(click());

        Thread.sleep(1000);
        onView(withId(R.id.nav_reviews)).perform(click()); // travel to review fragment
        Thread.sleep(1000);

        onView(withId(R.id.ReviewList)).perform(swipeUp());
        Thread.sleep(1000);

        onView(withText(title)).check(matches(isDisplayed()));
    }

}
