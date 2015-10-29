package com.nard;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;


/**
 * Created by Aronson1 on 10/25/15.
 */
public class LoginTest {

    /*
    As a user
    I want to be able to log in
    So I can log in to my account.
     */
    static
    {
       Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.WARNING);
    }

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

        userField.sendKeys(Constants.REDDIT_VALID_ACCT);
        passwordField.sendKeys(Constants.REDDIT_INVALID_PASS);
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

        userField.sendKeys(Constants.REDDIT_INVALID_ACCT);
        passwordField.sendKeys(Constants.REDDIT_INVALID_PASS);
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

        userField.sendKeys(Constants.REDDIT_VALID_ACCT);
        passwordField.sendKeys(Constants.REDDIT_VALID_PASS);
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
