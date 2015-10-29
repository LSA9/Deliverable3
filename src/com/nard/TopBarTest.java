package com.nard;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Aronson1 on 10/26/15.
 */
public class TopBarTest {

    /*
    As a user
    I want a subreddit navigation bar
    So I can easily navigate other subreddits
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
    Given the user is on the home page
    When the user clicks on the all button
    Then the user should be redirected to a page consisting of all of Reddit's subreddits
     */
    @Test
    public void topBarRedirectToSubredditTest(){
        WebElement linkToAll = driver.findElement(By.linkText("all"));

        linkToAll.click();

        assertEquals(driver.getCurrentUrl(), Constants.REDDIT_BASE_URL + "r/all");
    }

    /*
    Given the user is on the home page
    When the user clicks on the dropdown list on the top bar and then clicks on the 'ASKREDDIT' link in the list
    Then the user should be redirected to the AskReddit subreddit
     */
    @Test
    public void dropDownSelectionTest(){

        WebElement dropDown = driver.findElement(By.className("dropdown"));
        dropDown.click();

        WebElement linkToAskreddit = driver.findElement(By.linkText("AskReddit"));
        linkToAskreddit.click();

        assertEquals(driver.getCurrentUrl(), Constants.REDDIT_BASE_URL + "r/AskReddit/");
    }

    /*
    Given the user is on the all page
    When the user clicks to the front button on the top bar
    Then the user should be redirected to Reddit's home page
     */
    @Test
    public void returnToHomeFromTopBarTest(){
        WebElement linkToAll = driver.findElement(By.linkText("all"));
        linkToAll.click();

        WebElement linkToFront = driver.findElement(By.linkText("front"));
        linkToFront.click();

        assertEquals(driver.getCurrentUrl(), Constants.REDDIT_BASE_URL);
    }

    /*
    Given the user is on the home page
    When the user clicks on the random button on the top bar
    Then the user should be redirected to a random subreddit
     */
    @Test
    public void randomButtonTest(){
        WebElement linkToRandom = driver.findElement(By.linkText("random"));
        linkToRandom.click();

        assertTrue(driver.getCurrentUrl().contains(Constants.REDDIT_BASE_URL) && !driver.getCurrentUrl().equals(Constants.REDDIT_BASE_URL));
    }


}
