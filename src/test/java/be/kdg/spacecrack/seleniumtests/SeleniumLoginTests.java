package be.kdg.spacecrack.seleniumtests;/* Git $Id$
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class SeleniumLoginTests {
    private WebDriver driver;
    private String baseUrl="http://localhost:8080/#/";
    private WebElement uname;

    @Before
    public void setUp() throws Exception {

        String path = this.getClass().getResource("/chromedriver.exe").getPath();
        path = path.replaceAll("%20", " ");
        System.setProperty("webdriver.chrome.driver", path);
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);


    }

    @Test
    public void Login_validCredentials_spacecrackhome() throws Exception {

        driver.get(baseUrl);
        WebElement uname = driver.findElement(By.name("uname"));
        WebElement password = driver.findElement(By.name("password"));
        uname.sendKeys("test");
        password.sendKeys("test");
        WebElement loginbutton = driver.findElement(By.name("login"));
        loginbutton.click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("newgame")));
        assertEquals("http://localhost:8080/#/spacecrack/home", driver.getCurrentUrl());


    }

    @Test
    public void Login_invalidCredentials_errormessage() throws Exception {

        driver.get(baseUrl);
        WebElement uname = driver.findElement(By.name("uname"));
        WebElement password = driver.findElement(By.name("password"));
        uname.sendKeys("invalidUser");
        password.sendKeys("invalidPassword");
        WebElement loginbutton = driver.findElement(By.name("login"));
        loginbutton.click();
        WebDriverWait wait = new WebDriverWait(driver, 1);
        WebElement loginfailedDiv = driver.findElement(By.name("loginfailed"));
        wait.until(ExpectedConditions.visibilityOf(loginfailedDiv));
        assertEquals("http://localhost:8080/#/", driver.getCurrentUrl());

    }

    @After
    public void tearDown() throws Exception {
        driver.close();

    }
}
