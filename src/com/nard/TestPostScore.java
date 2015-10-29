package com.nard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Matt on 10/29/2015.
 */
public class TestPostScore
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
      FancyPrinting.clearPrintingIndent();
      FancyPrinting.print("Opening Firefox...");
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
      FancyPrinting.print("Closing Firefox...");
      if (webDriver != null)
      {
         webDriver.quit();
      }
   }

   private static List<WebElement> getScoreListForPost(WebElement post)
   {
      WebElement midcol = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      return midcol.findElements(By.xpath(".//div[starts-with(@class,'score')]"));
   }

   private static WebElement getLikesScoreForPost(WebElement post)
   {
      List<WebElement> scoreList  = getScoreListForPost(post);
      WebElement       scoreLikes = null;
      if (scoreList.size() > 0)
      {
         for (WebElement score : scoreList)
         {
            String scoreClass = score.getAttribute("class");
            if (scoreClass.contains("likes") && !scoreClass.contains("dislikes"))
            {
               scoreLikes = score;
               break;
            }
         }
      }
      return scoreLikes;
   }

   private static WebElement getDislikesScoreForPost(WebElement post)
   {
      List<WebElement> scoreList     = getScoreListForPost(post);
      WebElement       scoreDislikes = null;
      if (scoreList.size() > 0)
      {
         for (WebElement score : scoreList)
         {
            String scoreClass = score.getAttribute("class");
            if (scoreClass.contains("dislikes"))
            {
               scoreDislikes = score;
               break;
            }
         }
      }
      return scoreDislikes;
   }

   private static WebElement getUnvotedScoreForPost(WebElement post)
   {
      List<WebElement> scoreList    = getScoreListForPost(post);
      WebElement       scoreUnvoted = null;
      if (scoreList.size() > 0)
      {
         for (WebElement score : scoreList)
         {
            String scoreClass = score.getAttribute("class");
            if (scoreClass.contains("unvoted"))
            {
               scoreUnvoted = score;
               break;
            }
         }
      }
      return scoreUnvoted;
   }

   private static void assertScoreIsOrange(WebElement element)
   {
      assertEquals("Visible score is orange", "rgba(255, 139, 96, 1)", element.getCssValue("color"));
   }

   private static void assertScoreIsGray(WebElement scoreElement)
   {
      assertEquals("Visible score is gray", "rgba(198, 198, 198, 1)", scoreElement.getCssValue("color"));
   }

   private static void assertScoreIsBlue(WebElement scoreElement)
   {
      assertEquals("Visible score is blue", "rgba(148, 148, 255, 1)", scoreElement.getCssValue("color"));
   }

   /*
    * scenario 1:
    * Given a user is currently logged in
    * when the upvote button is pressed on a post that the user has not voted on already
    * then the net upvote count is increased by 1 and is colored orange.
    */
   @Test
   public void testPostScore_unvotedToUpvote() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // first check that unvoted score is visible and gray to start
      WebElement unvotedScore  = getUnvotedScoreForPost(post);
      assertTrue("Unvoted score is visible score", unvotedScore.isDisplayed());
      assertScoreIsGray(unvotedScore);
      // also get the unvoted score count
      int unvotedCount = Integer.parseInt(unvotedScore.getText());
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      SeleniumUtils.getUpvoteArrowForPost(post).click();
      // then check that the likes score is now being displayed, is orange, and the count is 1 greater than the unvoted
      // score
      WebElement likesScore = getLikesScoreForPost(post);
      assertFalse("Unvoted score is NOT visible score", unvotedScore.isDisplayed());
      assertTrue("Likes score is visible score", likesScore.isDisplayed());
      assertScoreIsOrange(likesScore);
      assertEquals("Score increased by 1", unvotedCount + 1, Integer.parseInt(likesScore.getText()));
   }

   /*
    * scenario 2:
    * Given a user is currently logged in
    * when the downvote button is pressed on a post that the user has not voted on already
    * then the net upvote count is decreased by 1 and is colored blue.
    */
   @Test
   public void testPostScore_unvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostNotVotedOn(post);
      // first check that unvoted score is visible and gray to start
      WebElement unvotedScore  = getUnvotedScoreForPost(post);
      assertTrue("Unvoted score is visible score", unvotedScore.isDisplayed());
      assertScoreIsGray(unvotedScore);
      // also get the unvoted score count
      int unvotedCount = Integer.parseInt(unvotedScore.getText());
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      SeleniumUtils.getDownvoteArrowForPost(post).click();
      // then check that the dislikes score is now being displayed, is blue, and the count is 1 less than the unvoted
      // score
      WebElement dislikesScore = getDislikesScoreForPost(post);
      assertFalse("Unvoted score is NOT visible score", unvotedScore.isDisplayed());
      assertTrue("Dislikes score is visible score", dislikesScore.isDisplayed());
      assertScoreIsBlue(dislikesScore);
      assertEquals("Score decreased by 1", unvotedCount - 1, Integer.parseInt(dislikesScore.getText()));
   }

   /*
    * scenario 3:
    * Given a user is currently logged in and has upvoted a post
    * when the upvote button is pressed on this post (the one which was previously upvoted)
    * then the net upvote count is decreased by 1 and is colored gray.
    */
   @Test
   public void testPostScore_upvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostUpvoted(post);
      // first check that likes score is visible and orange to start
      WebElement likesScore  = getLikesScoreForPost(post);
      assertTrue("Likes score is visible score", likesScore.isDisplayed());
      assertScoreIsOrange(likesScore);
      // also get the likes score count
      int likesCount = Integer.parseInt(likesScore.getText());
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      SeleniumUtils.getUpvoteArrowForPost(post).click();
      // then check that the unvoted score is now being displayed, is gray, and the count is 1 less than the likes
      // score
      WebElement unvotedScore = getUnvotedScoreForPost(post);
      assertFalse("Likes score is NOT visible score", likesScore.isDisplayed());
      assertTrue("Unvoted score is visible score", unvotedScore.isDisplayed());
      assertScoreIsGray(unvotedScore);
      assertEquals("Score decreased by 1", likesCount - 1, Integer.parseInt(unvotedScore.getText()));
   }

   /*
    * scenario 4:
    * Given a user is currently logged in and has downvoted a post
    * when the downvote button is pressed on this post (the one which was previously downvoted)
    * then the net upvote count is increased by 1 and is colored gray.
    */
   @Test
   public void testPostScore_downvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostDownvoted(post);
      // first check that dislikes score is visible and blue to start
      WebElement dislikesScore  = getDislikesScoreForPost(post);
      assertTrue("Dislikes score is visible score", dislikesScore.isDisplayed());
      assertScoreIsBlue(dislikesScore);
      // also get the dislikes score count
      int dislikesCount = Integer.parseInt(dislikesScore.getText());
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      SeleniumUtils.getDownvoteArrowForPost(post).click();
      // then check that the unvoted score is now being displayed, is gray, and the count is 1 greater than the dislikes
      // score
      WebElement unvotedScore = getUnvotedScoreForPost(post);
      assertFalse("Dislikes score is NOT visible score", dislikesScore.isDisplayed());
      assertTrue("Unvoted score is visible score", unvotedScore.isDisplayed());
      assertScoreIsGray(unvotedScore);
      assertEquals("Score increased by 1", dislikesCount + 1, Integer.parseInt(unvotedScore.getText()));
   }

   /*
    * scenario 5:
    * Given a user is currently logged in and has upvoted a post
    * when the downvote button is pressed on this post (the one which was previously upvoted)
    * then the net upvote count is decreased by 2 and is colored blue.
    */
   @Test
   public void testPostScore_upvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostUpvoted(post);
      // first check that likes score is visible and orange to start
      WebElement likesScore  = getLikesScoreForPost(post);
      assertTrue("Likes score is visible score", likesScore.isDisplayed());
      assertScoreIsOrange(likesScore);
      // also get the likes score count
      int likesCount = Integer.parseInt(likesScore.getText());
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      SeleniumUtils.getDownvoteArrowForPost(post).click();
      // then check that the dislikes score is now being displayed, is blue, and the count is 2 less than the likes
      // score
      WebElement dislikesScore = getDislikesScoreForPost(post);
      assertFalse("Likes score is NOT visible score", likesScore.isDisplayed());
      assertTrue("Dislikes score is visible score", dislikesScore.isDisplayed());
      assertScoreIsBlue(dislikesScore);
      assertEquals("Score decreased by 2", likesCount - 2, Integer.parseInt(dislikesScore.getText()));
   }

   /*
    * scenario 6:
    * Given a user is currently logged in and has downvoted a post
    * when the upvote button is pressed on this post (the one which was previously downvoted)
    * then the net upvote count is increased by 2 and is colored orange.
    */
   @Test
   public void testPostScore_downvotedToUpvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      SeleniumUtils.ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = SeleniumUtils.getTopPost(webDriver);
      // makes sure that the vote is not voted on
      SeleniumUtils.ensurePostDownvoted(post);
      // first check that dislikes score is visible and blue to start
      WebElement dislikesScore  = getDislikesScoreForPost(post);
      assertTrue("Dislikes score is visible score", dislikesScore.isDisplayed());
      assertScoreIsBlue(dislikesScore);
      // also get the dislikes score count
      int dislikesCount = Integer.parseInt(dislikesScore.getText());
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      SeleniumUtils.getUpvoteArrowForPost(post).click();
      // then check that the likes score is now being displayed, is orange, and the count is 2 greater than the dislikes
      // score
      WebElement likesScore = getLikesScoreForPost(post);
      assertFalse("Dislikes score is NOT visible score", dislikesScore.isDisplayed());
      assertTrue("Likes score is visible score", likesScore.isDisplayed());
      assertScoreIsOrange(likesScore);
      assertEquals("Score increased by 2", dislikesCount + 2, Integer.parseInt(likesScore.getText()));
   }
}
