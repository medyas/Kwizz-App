package ml.medyas.kwizzapp;


import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.text.format.DateUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ml.medyas.kwizzapp.activities.LoginActivity;
import ml.medyas.kwizzapp.activities.MainActivity;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {


    /*@Rule
    public ActivityTestRule<LoginActivity> mActivityRule
            = new ActivityTestRule<>(LoginActivity.class);*/

    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule
            = new IntentsTestRule<>(LoginActivity.class);



    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityRule.getActivity().getIdlingResource();
        // Make sure Espresso does not time out
        IdlingPolicies.setMasterPolicyTimeout(DateUtils.SECOND_IN_MILLIS * 75 * 2, TimeUnit.MILLISECONDS);
        IdlingPolicies.setIdlingResourceTimeout(DateUtils.SECOND_IN_MILLIS * 75 * 2, TimeUnit.MILLISECONDS);

        // Now we wait
        IdlingRegistry.getInstance().register(mIdlingResource);

    }


    @Test
    public void useAppContext() {
/*
        onView(withId(R.id.button_spalsh_create_account)).perform(click());

        onView(withId(R.id.register_username)).perform(typeText("medyas"), closeSoftKeyboard());
        onView(withId(R.id.register_email)).perform(typeText("medyassinesabri@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.register_password)).perform(typeText("50532193"), closeSoftKeyboard());
        onView(withId(R.id.register_pass_confirm)).perform(typeText("50532193"), closeSoftKeyboard());

        onView(withId(R.id.button_register)).perform(click());
*/

        onView(withId(R.id.login_email)).perform(typeText("medyassinesabri@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login_password)).perform(typeText("50532193"), closeSoftKeyboard());

        onView(withId(R.id.button_login)).perform(click());

        intended(hasComponent(MainActivity.class.getName()));

        //intended(toPackage("ml.medyas.kwizzapp.activities"));

    }

    @After
    public void unregisterIdlingResources() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

}
