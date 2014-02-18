package be.kdg.spacecrack.seleniumtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

public class SeleniumEditProfileTests extends SeleniumBaseTestCase {



    @Test
    public void navigateToEditProfile_loggedIn_ok() throws Exception {
        login();
        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement btnAction = driver.findElement(By.name("btnAction"));
        btnAction.click();
        WebElement lnkEditProfile = driver.findElement(By.name("lnkEditProfile"));
        lnkEditProfile.click();

        WebElement passwordTextBox = driver.findElement(By.id("new_password"));
        wait.until(ExpectedConditions.visibilityOf(passwordTextBox));
        assertEquals("http://localhost:8080/#/spacecrack/editProfile", driver.getCurrentUrl());
    }


}
