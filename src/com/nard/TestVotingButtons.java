package com.nard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
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
      try
      {
         Thread.sleep(10000);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }

      FancyPrinting.print("Closing Firefox...");
      if (webDriver != null)
      {
         webDriver.quit();
      }
   }

   private static WebElement getTopPost(WebDriver driver)
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Getting top post");
      ensureOnRedditBasePage(driver);
      WebElement siteTable = driver.findElement(By.cssSelector(".sitetable"));
      assertEquals(siteTable.getAttribute("id"), "siteTable");
      List<WebElement> postList = siteTable.findElements(By.xpath(".//div[starts-with(@id,'thing')]"));
      WebElement       post     = null;
      if (postList.size() > 0)
      {
         for (WebElement we : postList)
         {
            WebElement rankElement = we.findElement(By.className("rank"));
            if (rankElement.getText().trim().equals("1"))
            {
               post = we;
               break;
            }
         }
         FancyPrinting.print("Found top post");
      }
      else
      {
         post = null;
         FancyPrinting.print("Did not find top post");
      }
      FancyPrinting.decreasePrintingIndent();
      return post;
   }

   private static void ensurePostUpvoted(WebElement post)
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that post is upvoted");
      WebElement midcol      = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      String     midColClass = midcol.getAttribute("class");
      if (midColClass.contains("dislikes") || midColClass.contains("unvoted"))
      {
         FancyPrinting.print("Post downvoted or unvoted, clicking upvote button to bring post to upvoted");
         /* click like button to bring post to upvoted */
         // first get like (up arrow) webelement
         WebElement upvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
         // send click to upvote arrow
         upvoteArrow.click();
         FancyPrinting.print("Post now upvoted");
      }

      assertUpvoteArrowIsOrange(getUpvoteArrowForPost(post));
      assertDownvoteArrowIsGrey(getDownvoteArrowForPost(post));

      FancyPrinting.decreasePrintingIndent();
   }

   private static void ensurePostDownvoted(WebElement post)
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that post is downvoted");
      WebElement midcol      = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      String     midColClass = midcol.getAttribute("class");
      if ((midColClass.contains("likes") && !midColClass.contains("dislikes")) || midColClass.contains("unvoted"))
      {
         FancyPrinting.print("Post upvoted or unvoted, clicking downvote button to bring post to downvoted");
         /* click downvote arrow to make post downvoted */
         // first get dislike (down arrow) webelement
         WebElement downvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
         // send click to downvote arrow
         downvoteArrow.click();
         FancyPrinting.print("Post now downvoted");
      }

      assertUpvoteArrowIsGrey(getUpvoteArrowForPost(post));
      assertDownvoteArrowIsBlue(getDownvoteArrowForPost(post));

      FancyPrinting.decreasePrintingIndent();
   }

   private static void ensurePostNotVotedOn(WebElement post) throws Exception
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that post is not voted on");
      WebElement midcol      = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      String     midColClass = midcol.getAttribute("class");
      if (midColClass.contains("dislikes"))
      {
         FancyPrinting.print("Post downvoted, clicking downvote button to return post to unvoted");
         /* click dislike button to return post to unvoted */
         // first get dislike (down arrow) webelement
         WebElement downvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
         // send click to downvote arrow
         downvoteArrow.click();
         FancyPrinting.print("Post now unvoted");
      }
      else
      {
         if (midColClass.contains("likes"))
         {
            FancyPrinting.print("Post upvoted, clicking upvote button to return post to unvoted");
         /* click like button to return post to unvoted */
            // first get like (up arrow) webelement
            WebElement upvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
            // send click to upvote arrow
            upvoteArrow.click();
            Thread.sleep(50);
            FancyPrinting.print("Post now unvoted");
         }
         else
         {
            FancyPrinting.print("Post not voted on");
         }
      }

      assertUpvoteArrowIsGrey(getUpvoteArrowForPost(post));
      assertDownvoteArrowIsGrey(getDownvoteArrowForPost(post));

      FancyPrinting.decreasePrintingIndent();
   }

   private static WebElement getUpvoteArrowForPost(WebElement post)
   {
      WebElement midcol = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      return midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
   }

   private static WebElement getDownvoteArrowForPost(WebElement post)
   {
      WebElement midcol = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      return midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
   }

   private static void ensureOnRedditBasePage(WebDriver driver) throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is at the reddit base page (" + Constants.REDDIT_BASE_URL + ")");
      if (driver.getCurrentUrl().equals(Constants.REDDIT_BASE_URL))
      {
         FancyPrinting.print("Already at reddit base page");
      }
      else
      {
         FancyPrinting.print("Not at reddit base page, now attempting to load page");
         driver.get(Constants.REDDIT_BASE_URL);
         FancyPrinting.print("Waiting for reddit base page to load");
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(By.id("siteTable")));
         FancyPrinting.print("Successfully loaded reddit base page");
      }
      ensureNoModalDialogOpen(driver);
      FancyPrinting.decreasePrintingIndent();
   }

   private static void ensureNoModalDialogOpen(WebDriver driver)
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that there is no modal dialog covering the screen");
      WebElement body = driver.findElement(By.xpath("//body"));
      if (body.getAttribute("class").contains("modal-open"))
      {
         FancyPrinting.print("Modal dialog is open, now closing");
//         body.findElement(By.xpath(".//div[contains(@class,'login-modal')]")).click();
         driver.get(Constants.REDDIT_BASE_URL);
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(By.id("siteTable")));
      }
      else
      {
         FancyPrinting.print("Modal dialog not open");
      }
      FancyPrinting.decreasePrintingIndent();
   }

   private static void ensureLoggedOut(WebDriver driver) throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged out");
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (SeleniumUtils.elementIsPresent(driver, logoutLinkLocator))
      {
         FancyPrinting.print("User is logged in, now logging the user out");
         WebElement logoutLinkElement = driver.findElement(logoutLinkLocator);
         FancyPrinting.print("Now clicking logout link");
         logoutLinkElement.click();
         // wait for logout to complete by waiting for login form to appear
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(By.id("login_login-main")));
         FancyPrinting.print("Successfully logged out");
      }
      else
      {
         FancyPrinting.print("User is already logged out");
      }
      FancyPrinting.decreasePrintingIndent();
   }

   private static void ensureLoggedIn(WebDriver driver) throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged in");
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (SeleniumUtils.elementIsPresent(driver, logoutLinkLocator))
      {
         FancyPrinting.print("User is already logged in");
      }
      else
      {
         FancyPrinting.print("User is not logged in, now logging the user in");
         // get the necessary web elements
         WebElement usernameField = driver.findElement(new ByChained(By.id("login_login-main"), By.name("user")));
         WebElement passwordField = driver.findElement(new ByChained(By.id("login_login-main"), By.name("passwd")));
         WebElement loginButton = driver.findElement(new ByChained(By.id("login_login-main"), By.className("btn")));

         FancyPrinting.print("Inputting user data");
         usernameField.sendKeys(Constants.REDDIT_ACCT_NAME);
         passwordField.sendKeys(Constants.REDDIT_PASSWORD);

         FancyPrinting.print("Submitting login info");
         loginButton.click();

         // wait for login to complete by waiting for logout link to appear
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(logoutLinkLocator));
         FancyPrinting.print("Successfully logged in");
      }
      FancyPrinting.decreasePrintingIndent();
   }


   private static void assertUpvoteArrowIsOrange(WebElement upvoteArrow)
   {
      assertTrue(upvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue(upvoteArrow.getCssValue("background-position").equals("-42px -865px"));
      FancyPrinting.print("Upvote arrow is orange");
   }

   private static void assertUpvoteArrowIsGrey(WebElement upvoteArrow)
   {
      assertTrue(upvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue(upvoteArrow.getCssValue("background-position").equals("-21px -865px"));
      FancyPrinting.print("Upvote arrow is grey");
   }


   private static void assertDownvoteArrowIsBlue(WebElement downvoteArrow)
   {
      assertTrue(downvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue(downvoteArrow.getCssValue("background-position").equals("0px -865px"));
      FancyPrinting.print("Downvote arrow is blue");
   }

   private static void assertDownvoteArrowIsGrey(WebElement downvoteArrow)
   {
      assertTrue(downvoteArrow.getCssValue("background-image").contains("sprite-reddit.etL6mAFJrLc.png"));
      assertTrue(downvoteArrow.getCssValue("background-position").equals("-86px -843px"));
      FancyPrinting.print("Downvote arrow is grey");
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
      ensureLoggedOut(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is not voted on
      ensurePostNotVotedOn(post);
      // get the upvote arrow for susequent actions and checks
      WebElement upvoteArrow = getUpvoteArrowForPost(post);
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      upvoteArrow.click();
      // get the popup dialog element
      WebElement loginPopupDialog = webDriver.findElement(By.className("modal-content"));
      // ensure that the dialog is visible
      assertTrue(loginPopupDialog.isDisplayed());
      FancyPrinting.print("Confirmed that login prompt dialog appeared");
      // get dialog header text
      String headerText = loginPopupDialog.findElement(By.id("cover-msg")).getText().trim();
      // ensure that the header text tells the user that they must be logged in to upvote
      assertEquals(headerText, "You need to be logged in to upvote things.");
      FancyPrinting.print("Confirmed that login prompt header tells user to login in order to upvote");
   }

   /*
    * scenario 2:
    * Given a user is currently not logged in
    * when the downvote button is pressed on a post that the user has not voted on already
    * then a dialog pops up alerting the user that they must be logged in to downvote.
    */
   @Test
   public void test_downvoteRequiresLogin() throws Exception
   {
      // make sure that we are logged out of reddit (also navigates to the reddit base page)
      ensureLoggedOut(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is not voted on
      ensurePostNotVotedOn(post);
      // get the downvote arrow for susequent actions and checks
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      downvoteArrow.click();
      // get the popup dialog element
      WebElement loginPopupDialog = webDriver.findElement(By.className("modal-content"));
      // ensure that the dialog is visible
      assertTrue(loginPopupDialog.isDisplayed());
      FancyPrinting.print("Confirmed that login prompt dialog appeared");
      // get dialog header text
      String headerText = loginPopupDialog.findElement(By.id("cover-msg")).getText().trim();
      // ensure that the header text tells the user that they must be logged in to downvote
      assertEquals("You need to be logged in to downvote things.", headerText);
      FancyPrinting.print("Confirmed that login prompt header tells user to login in order to downvote");
   }

   /*
    * scenario 3:
    * Given a user is currently logged in
    * when the upvote button is pressed on a post that the user has not voted on already
    * then the upvote button becomes orange and the downvote button remains grey.
    */
   @Test
   public void test_unvotedToUpvote() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is not voted on
      ensurePostNotVotedOn(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that it both arrows are grey to start
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      upvoteArrow.click();
      // the check that the upvote arrow is now orange and the downvote arrow is still grey
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
   }

   /*
    * scenario 4:
    * Given a user is currently logged in
    * when the downvote button is pressed on a post that the user has not voted on already
    * then the downvote button becomes blue and the upvote button remains grey.
    */
   @Test
   public void test_unvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is not voted on
      ensurePostNotVotedOn(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that it both arrows are grey to start
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      downvoteArrow.click();
      Thread.sleep(50);
      // the check that the upvote arrow is still grey and the downvote arrow is now blue
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
   }

   /*
    * scenario 5:
    * Given a user is currently logged in and has upvoted a post
    * when the upvote button is pressed on this post (the one which was previously upvoted)
    * then the upvote button becomes grey and the downvote button remains grey.
    */
   @Test
   public void test_upvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is upvoted
      ensurePostUpvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that the upvote arrow is orange and the downvote arrow is grey
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      upvoteArrow.click();
      Thread.sleep(50);
      // the check that the upvote arrow is now grey and the downvote arrow is still grey
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
   }

   /*
    * scenario 6:
    * Given a user is currently logged in and has downvoted a post
    * when the downvote button is pressed on this post (the one which was previously downvoted)
    * then the downvote button becomes grey and the upvote button remains grey.
    */
   @Test
   public void test_downvotedToUnvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is upvoted
      ensurePostDownvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that the upvote arrow is grey and the downvote arrow is blue
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      downvoteArrow.click();
      Thread.sleep(50);
      // the check that the upvote arrow is still grey and the downvote arrow is now grey
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
   }

   /*
    * scenario 7:
    * Given a user is currently logged in and has upvoted a post
    * when the downvote button is pressed on this post (the one which was previously upvoted)
    * then the upvote button becomes grey and the downvote button becomes blue.
    */
   @Test
   public void test_upvotedToDownvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is upvoted
      ensurePostUpvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that the upvote arrow is orange and the downvote arrow is grey
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
      // then click the downvote arrow
      FancyPrinting.print("Clicking downvote arrow");
      downvoteArrow.click();
      Thread.sleep(50);
      // the check that the upvote arrow is now grey and the downvote arrow is now blue
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
   }

   /*
    * scenario 8:
    * Given a user is currently logged in and has downvoted a post
    * when the upvote button is pressed on this post (the one which was previously downvoted)
    * then the downvote button becomes grey and the upvote button becomes orange.
    */
   @Test
   public void test_downvotedToUpvoted() throws Exception
   {
      // make sure that we are logged in to reddit (also navigates to the reddit base page)
      ensureLoggedIn(webDriver);
      // gets the top post
      WebElement post = getTopPost(webDriver);
      // makes sure that the vote is upvoted
      ensurePostDownvoted(post);
      // get the upvote and downvote arrows for susequent actions and checks
      WebElement upvoteArrow   = getUpvoteArrowForPost(post);
      WebElement downvoteArrow = getDownvoteArrowForPost(post);
      // first check that the upvote arrow is grey and the downvote arrow is blue
      assertUpvoteArrowIsGrey(upvoteArrow);
      assertDownvoteArrowIsBlue(downvoteArrow);
      // then click the upvote arrow
      FancyPrinting.print("Clicking upvote arrow");
      upvoteArrow.click();
      Thread.sleep(50);
      // the check that the upvote arrow is now orange and the downvote arrow is now grey
      assertUpvoteArrowIsOrange(upvoteArrow);
      assertDownvoteArrowIsGrey(downvoteArrow);
   }
}
