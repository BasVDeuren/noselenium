package be.kdg.spacecrack.seleniumtests;/* Git $Id
 *
 * Project Application Development
 * Karel de Grote-Hogeschool
 * 2013-2014
 *
 */

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumRegisterTest extends SeleniumBaseTestCase {
    @Test
    public void RegisterUser() throws Exception {
        driver.get(baseUrl);
        WebElement lnkRegister = driver.findElement(By.name("lnkRegister"));
        lnkRegister.click();
        WebElement txtEmail = driver.findElement(By.name("email"));
        WebElement txtUsername = driver.findElement(By.id("username"));
        WebElement txtPassword = driver.findElement(By.id("password"));
        WebElement txtPasswordRepeated = driver.findElement(By.id("password2"));
        WebElement btnRegister = driver.findElement(By.name("btnRegister"));
        txtEmail.sendKeys("emailSeleniumTest@email.be");
        txtUsername.sendKeys("usernameSeleniumTest");
        txtPassword.sendKeys("passwordSeleniumTest");
        txtPasswordRepeated.sendKeys("passwordSeleniumTest");

        btnRegister.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);

        WebElement btnNewGame = driver.findElement(By.name("createGame"));

        wait.until(ExpectedConditions.visibilityOf(btnNewGame));
    }


    @After
    public void tearDown2() throws Exception {
        Session session = sessionFactory.getCurrentSession();

                @SuppressWarnings("JpaQlInspection") Query q = session.createQuery("delete from User u where u.email = :email");
                q.setParameter("email", "emailSeleniumTest@email.be");
                q.executeUpdate();
    }
}
