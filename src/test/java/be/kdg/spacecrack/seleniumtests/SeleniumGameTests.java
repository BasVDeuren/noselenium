package be.kdg.spacecrack.seleniumtests;/* Git $Id$
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

public class SeleniumGameTests extends SeleniumBaseTestCase {

    @Test
    public void testNavigateToCreateGamePage() throws Exception {
         navigateToNewGame();
    }

    protected void navigateToNewGame() throws InterruptedException {
        login();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement newgame = driver.findElement(By.name("createGame"));
        Thread.sleep(500);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("createGame")));
        newgame.click();

        WebElement username = driver.findElement(By.name("btnUserNameRadio"));
        wait.until(ExpectedConditions.visibilityOf(username));
        username.click();

        WebElement btnFind = driver.findElement(By.name("btnFind"));
        wait.until(ExpectedConditions.visibilityOf(btnFind));

        WebElement txtGameName = driver.findElement(By.name("gameName"));
        WebElement txtSearchString = driver.findElement(By.name("txtSearchString"));
        txtGameName.sendKeys("TestGame123");

        txtSearchString.sendKeys("op");

        wait.until(ExpectedConditions.elementToBeClickable(By.name("btnFind")));
        btnFind.click();

        WebElement btnInvite = driver.findElement(By.name("btnInvite"));
        wait.until(ExpectedConditions.visibilityOf(btnInvite));
    }
}
