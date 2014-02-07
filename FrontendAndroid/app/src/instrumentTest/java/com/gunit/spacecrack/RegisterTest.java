package com.gunit.spacecrack;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by Dimi on 5/02/14.
 */
public class RegisterTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

    private Solo solo;

    public RegisterTest() {
        super(RegisterActivity.class);
    }

    @Override
    public void setUp() {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testActivityStart() {
        solo.assertCurrentActivity("Current activity should be RegisterActivity", RegisterActivity.class);
    }

    public void testRegisterSucces() {
        solo.assertCurrentActivity("Current activity should be RegisterActivity", RegisterActivity.class);
        createAccount("test", "test", "test", "test@gmail.com");
        solo.clickOnButton(0);
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Current activity should be LoginActivity", LoginActivity.class);
    }

    public void testRegisterFailed() {
        solo.assertCurrentActivity("Current activity should be RegisterActivity", RegisterActivity.class);
        solo.clickOnButton(0);
        solo.waitForActivity(LoginActivity.class);
        solo.assertCurrentActivity("Current activity should be RegisterActivity", RegisterActivity.class);
    }

    public void testMatchingPasswords() {
        assertTrue(getActivity().checkPasswords("test", "test"));
    }

    private void createAccount(String username, String password, String confirmPassword, String email) {
        solo.enterText(0, username);
        solo.enterText(1, password);
        solo.enterText(2, confirmPassword);
        solo.enterText(3, email);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }
}
