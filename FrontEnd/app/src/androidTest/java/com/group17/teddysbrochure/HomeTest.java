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
    public class HomeTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void instantiateHomeFragment() throws InterruptedException {
        mainActivity.getActivity().getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testCallButton() {
        onView(withId(R.id.callButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisitButton() {
        onView(withId(R.id.visitButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testParkHistoryButton() {
        onView(withId(R.id.parkHistoryBubble)).check(matches(isDisplayed()));
    }

    @Test
    public void testAttractionsButton(){
        onView(withId(R.id.exploreBubble)).check(matches(isDisplayed()));
    }

    @Test
    public void testAttractionImage(){
        onView(withId(R.id.attractionImage)).check(matches(isDisplayed()));
    }

    @Test
    public void testReviewButton(){
        onView(withId(R.id.reviewBubble)).check(matches(isDisplayed()));
    }

    @Test
    public void testMainImage(){
        onView(withId(R.id.mainImage)).check(matches(isDisplayed()));
    }

    @Test
    public void testParkHistoryPreview(){
        onView(withId(R.id.parkHistoryPreview)).check(matches(isDisplayed()));
    }

    @Test
    public void testParkHistoryPreviewContent(){
        onView(withText("Four Native American cultures are known to have lived in the area during the last 10,000 years.")).check(matches(isDisplayed()));
    }




}
