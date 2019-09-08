package it.passwordmanager.simonederozeris.passwordmanager;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;


public class EspressoTest {
    @Rule
    public ActivityTestRule<CheckPwdActivity> mActivityRule = new ActivityTestRule<>(CheckPwdActivity.class);


    @Test
    public void pressSaveButton() {
        onView(withId(R.id.editText1)).perform(typeText("1"));
    }
}