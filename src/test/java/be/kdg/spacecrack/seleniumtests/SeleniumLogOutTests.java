package be.kdg.spacecrack.seleniumtests;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * Created by Arno on 18/02/14.
 */
public class SeleniumLogOutTests extends SeleniumBaseTestCase {

    @Test
    public void LogOutOK(){
        login();

        WebElement btnAction = driver.findElement(By.name("btnAction"));
        btnAction.click();
        WebElement btnLogout = driver.findElement(By.name("btnLogout"));
        btnLogout.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("login")));

        assertEquals("http://localhost:8080/#/", driver.getCurrentUrl());
        //assertEquals(false, btnAction.isDisplayed());

    }
}
