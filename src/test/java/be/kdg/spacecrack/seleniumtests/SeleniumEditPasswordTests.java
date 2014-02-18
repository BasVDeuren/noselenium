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
public class SeleniumEditPasswordTests extends SeleniumBaseTestCase {
    @Test
    public void ChangePassword_Oké() throws Exception {
        login();
        EditProfile();

        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement passwordTextBox = driver.findElement(By.id("new_password"));
        wait.until(ExpectedConditions.visibilityOf(passwordTextBox));
        WebElement password2TextBox = driver.findElement(By.id("password2"));

        passwordTextBox.sendKeys("test");
        password2TextBox.sendKeys("test");
        WebElement saveButton = driver.findElement(By.id("save"));
        saveButton.click();

        WebElement profileSuccesMsg = driver.findElement(By.id("profileSuccesMsg"));

        wait.until(ExpectedConditions.visibilityOf(profileSuccesMsg));


        assertEquals(true,profileSuccesMsg.isDisplayed());
    }

    @Test
    public void ChangePassword_Fail() throws Exception {
        login();
        EditProfile();

        WebDriverWait wait = new WebDriverWait(driver,10);
        WebElement passwordTextBox = driver.findElement(By.id("new_password"));
        wait.until(ExpectedConditions.visibilityOf(passwordTextBox));
        WebElement password2TextBox = driver.findElement(By.id("password2"));

        passwordTextBox.sendKeys("gelukt");
        password2TextBox.sendKeys("nietgelukt");

        WebElement saveButton = driver.findElement(By.id("save"));

        assertEquals(false,saveButton.isEnabled());
    }
}
