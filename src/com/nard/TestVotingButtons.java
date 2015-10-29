package com.nard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestVotingButtons
{
   private static WebDriver webDriver;

   /**
    * Creates the FirefoxDriver and sets the implicit timeout to 10 seconds.
    * <p>
    * Since this is annotated with @BeforeClass, this method is called once before a test run of this
    * class (rather than @Before, which runs once per method), which is done to improve performance.
    */
   @BeforeClass
   public static void openBrowser()
   {
      webDriver = new FirefoxDriver();
      webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
   }

   /**
    * Closes the FirefoxDriver.
    * <p>
    * Since this is annotated with @AfterClass, this method is called once after a test run of this
    * class (rather than @After, which runs once per method), which is done to improve performance.
    */
   @AfterClass
   public static void closeBrowser()
   {
      if (webDriver != null)
      {
         webDriver.quit();
      }
   }

   private static void assertDownvoteArrowIsGray(WebElement downvoteArrow)
   {
      assertTrue("Downvote arrow uses expected sprite sheet",
                 downvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue("Downvote arrow is gray",
                 downvoteArrow.getCssValue("background-position").equals("-86px -843px"));
   }

   private static void assertDownvoteArrowIsBlue(WebElement downvoteArrow)
   {
      assertTrue("Downvote arrow uses expected sprite sheet",
                 downvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue("Downvote arrow is blue",
                 downvoteArrow.getCssValue("background-position").equals("0px -865px"));
   }

   private static void assertUpvoteArrowIsGray(WebElement upvoteArrow)
   {
      assertTrue("Upvote arrow uses expected sprite sheet",
                 upvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue("Upvote arrow is gray",
                 upvoteArrow.getCssValue("background-position").equals("-21px -865px"));
   }

   private static void assertUpvoteArrowIsOrange(WebElement upvoteArrow)
   {
      assertTrue("Upvote arrow uses expected sprite sheet",
                 upvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue("Upvote arrow is orange",
                 upvoteArrow.getCssValue("background-position").equals("-42px -865px"));
   }

   /*
    * scenario 1:
    * Given a user is currently not logged in
    * when the upvote button is pressed on a post that the user has not voted on already
    * then a dialog pops up alerting the user that they must be logged in to upvote.
    */
   @Test
   public void test_upvoteRequiresLogin() throws Exception
   {
      // make sure that we are logged out of reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedOut(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // get the upvote arrow for susequent actions and checks
      WebElement upvoteArrow = SeleniumUtils.getUpvoteArrowForPost(post);
      // then click the upvote arrow
      upvoteArrow.click();
      // make sure we can get the login prompt dialog that should have appeared
      WebElement loginPopupDialog = (new WebDriverWait(webDriver, 5))
            .until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-content")));
      // get dialog header text
      String headerText = loginPopupDialog.findElement(By.id("cover-msg")).getText().trim();
      // ensure that the header text tells the user that they must be logged in to upvote
      assertEquals("Login dialog header tells user to login in order to upvote",
                   "You need to be logged in to upvote things.", headerText);
   }

   /*
    * scenario 2:
    * Given a user is currently not logged in
    * when the downvote button is pressed on a post that the user has not voted on already
    * then a dialog pops up alerting the user that they must be logged in to downvote.
    */
   @Test
   public void testVotingButtons_downvoteRequiresLogin() throws Exception
   {
      // make sure that we are logged out of reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedOut(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // get the downvote arrow for susequent actions and checks
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // then click the downvote arrow
      downvoteArrow.click();
      // get the popup dialog element
      WebElement loginPopupDialog = (new WebDriverWait(webDriver, 5))
            .until(ExpectedConditions.visibilityOfElementLocated(By.className("modal-content")));
      // get dialog header text
      String headerText = loginPopupDialog.findElement(By.id("cover-msg")).getText().trim();
      // ensure that the header text tells the user that they must be logged in to downvote
      assertEquals("Login dialog header tells user to login in order to downvote",
                   "You need to be logged in to downvote things.", headerText);
   }

   /*
    * scenario 3:
    * Given a user is currently logged in
    * when the upvote button is pressed on a post that the user has not voted on already
    * then the upvote button becomes orange and the downvote button remains gray.
    */
   @Test
   public void testVotingButtons_unvotedToUpvote() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that it both arrows are gray to start
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
      // then click the upvote arrow
      upvoteArrow.click();
      // the check that the upvote arrow is now orange and the downvote arrow is still gray
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
   }

   /*
    * scenario 4:
    * Given a user is currently logged in
    * when the downvote button is pressed on a post that the user has not voted on already
    * then the downvote button becomes blue and the upvote button remains gray.
    */
   @Test
   public void testVotingButtons_unvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that it both arrows are gray to start
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
      // then click the downvote arrow
      downvoteArrow.click();
      // the check that the upvote arrow is still gray and the downvote arrow is now blue
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
   }

   /*
    * scenario 5:
    * Given a user is currently logged in and has upvoted a post
    * when the upvote button is pressed on this post (the one which was previously upvoted)
    * then the upvote button becomes gray and the downvote button remains gray.
    */
   @Test
   public void testVotingButtons_upvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is upvoted
      SeleniumUtils.ensurePostUpvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that the upvote arrow is orange and the downvote arrow is gray
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
      // then click the upvote arrow
      upvoteArrow.click();
      // the check that the upvote arrow is now gray and the downvote arrow is still gray
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
   }

   /*
    * scenario 6:
    * Given a user is currently logged in and has downvoted a post
    * when the downvote button is pressed on this post (the one which was previously downvoted)
    * then the downvote button becomes gray and the upvote button remains gray.
    */
   @Test
   public void testVotingButtons_downvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is upvoted
      SeleniumUtils.ensurePostDownvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that the upvote arrow is gray and the downvote arrow is blue
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
      // then click the downvote arrow
      downvoteArrow.click();
      // the check that the upvote arrow is still gray and the downvote arrow is now gray
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
   }

   /*
    * scenario 7:
    * Given a user is currently logged in and has upvoted a post
    * when the downvote button is pressed on this post (the one which was previously upvoted)
    * then the upvote button becomes gray and the downvote button becomes blue.
    */
   @Test
   public void testVotingButtons_upvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is upvoted
      SeleniumUtils.ensurePostUpvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that the upvote arrow is orange and the downvote arrow is gray
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
      // then click the downvote arrow
      downvoteArrow.click();
      // the check that the upvote arrow is now gray and the downvote arrow is now blue
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
   }

   /*
    * scenario 8:
    * Given a user is currently logged in and has downvoted a post
    * when the upvote button is pressed on this post (the one which was previously downvoted)
    * then the downvote button becomes gray and the upvote button becomes orange.
    */
   @Test
   public void testVotingButtons_downvotedToUpvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is upvoted
      SeleniumUtils.ensurePostDownvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = SeleniumUtils.getUpvoteArrowForPost(post);
      WebElement downvoteArrow = SeleniumUtils.getDownvoteArrowForPost(post);
      // first check that the upvote arrow is gray and the downvote arrow is blue
      assertUpvoteArrowIsGray(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
      // then click the upvote arrow
      upvoteArrow.click();
      // the check that the upvote arrow is now orange and the downvote arrow is now gray
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGray(downvoteArrow);
   }
}
