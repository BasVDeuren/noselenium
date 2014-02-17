package be.kdg.spacecrack.seleniumtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public abstract class SeleniumBaseTestCase  {
    protected WebDriver driver;
    protected String baseUrl="http://localhost:8080/#/";

    @Before
    public void setUp() throws Exception {

        String path = this.getClass().getResource("/chromedriver.exe").getPath();
        path = path.replaceAll("%20", " ");
        System.setProperty("webdriver.chrome.driver", path);
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);


    }
    protected void login() {
        driver.get(baseUrl);
        WebElement uname = driver.findElement(By.name("uname"));
        WebElement password = driver.findElement(By.name("password"));
        uname.sendKeys("test");
        password.sendKeys("test");
        WebElement loginbutton = driver.findElement(By.name("login"));
        loginbutton.click();
        WebDriverWait wait = new WebDriverWait(driver, 4);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("newgame")));
    }

    @After
    public void tearDown() throws Exception {
        driver.close();

    }
}
