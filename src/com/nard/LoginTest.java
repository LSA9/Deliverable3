import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Created by Aronson1 on 10/25/15.
 */
public class LoginTest {

    /*
    As a user
    I want to be able to log in
    So I can log in to my account.
     */


    static WebDriver driver = new HtmlUnitDriver();


    @Before
    public void setUp() throws Exception{
        driver.get("https://www.reddit.com/");
    }


    /*
    Given a correct username
    And incorrect password
    when the user is trying to login to his/her account
    then the user will be alerted that the login crudentials are inccorect
     */
    @Test
    public void correctUsernameIncorrectPasswordTest(){

        WebElement userField = driver.findElement(By.name("user"));
        WebElement passwordField = driver.findElement(By.name("passwd"));
        WebElement submitbutton = driver.findElement(By.className("submit"));

        userField.sendKeys("qarocks123");
        passwordField.sendKeys("123abc");
        submitbutton.click();

        WebDriverWait w = new WebDriverWait(driver, 10);

        assertTrue(submitbutton.isDisplayed());
    }

    /*Given a incorrect username
    And a correct password
    when the user is trying to login to his/her account
    then the user will be alerted that the login crudentials are inccorect
    */
    @Test
    public void incorrectUsernameIncorrectPasswordTest(){
        WebElement userField = driver.findElement(By.name("user"));
        WebElement passwordField = driver.findElement(By.name("passwd"));
        WebElement submitbutton = driver.findElement(By.className("submit"));

        userField.sendKeys("qadoesntrock123");
        passwordField.sendKeys("123abc");
        submitbutton.click();

        WebDriverWait w = new WebDriverWait(driver, 10);

        assertTrue(submitbutton.isDisplayed());
    }

    /*
   Given a correct username
   And a correct password
   when the user is trying to login to his/her account
   then the user will be logged into his/her account
    */
    @Test
    public void correctUsernameCorrectPasswordTest(){
        WebElement userField = driver.findElement(By.name("user"));
        WebElement passwordField = driver.findElement(By.name("passwd"));
        WebElement submitbutton = driver.findElement(By.className("btn"));

        userField.sendKeys("qarocks123");
        passwordField.sendKeys("abc123");
        submitbutton.click();

        WebDriverWait w = new WebDriverWait(driver, 10);

        WebElement mail = driver.findElement(By.id("mail"));

        assertTrue(mail.isDisplayed());
    }

    /*
    Given a blank username
    And correct password field
    when the user is trying to login to his/her account
    then the user will be alerted that the login crudentials are inccorect
     */
    @Test
    public void noUsernameNoPasswordTest(){
        WebElement submitbutton = driver.findElement(By.className("submit"));

        submitbutton.click();

        WebDriverWait w = new WebDriverWait(driver, 10);

        assertTrue(submitbutton.isDisplayed());
    }

}