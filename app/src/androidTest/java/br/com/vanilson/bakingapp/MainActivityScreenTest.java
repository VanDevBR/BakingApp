package br.com.vanilson.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityScreenTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void recipeList() {
        onView(withId(R.id.rv_recipes))
                .check(matches(isDisplayed()));
    }

    @Test
    public void recipeClick() {
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.master_list_fragment))
                .check(matches(isDisplayed()));
    }

    @Test
    public void stepScreen() {
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.master_list_fragment))
                .check(matches(isDisplayed()));

        onView(withId(R.id.rv_detail_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if(!mActivityTestRule.getActivity().getResources().getBoolean(R.bool.isTablet)){
            onView(withId(R.id.step_title)).check(matches(isDisplayed()));
        }

    }

}
