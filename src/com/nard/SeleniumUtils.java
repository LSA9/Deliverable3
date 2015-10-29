package com.nard;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Contains static utility methods related to Selenium.
 */
public final class SeleniumUtils
{
   /**
    * Private constructor to prevent instantiation.
    */
   private SeleniumUtils()
   {
   }

   /**
    * Gets the WebElement containing the top post of the front page of reddit.
    *
    * @param driver the web driver
    * @return the element containing the top post
    */
   public static WebElement getTopPost(WebDriver driver)
   {
      // make sure we are on reddit
      ensureOnRedditBasePage(driver);
      // get the site table
      WebElement siteTable = driver.findElement(By.cssSelector(".sitetable"));
      // assert that we found the siteTable, (i.e. not the organic-siteTable [used for ad posts])
      assertEquals("Retrieved siteTable element", siteTable.getAttribute("id"), "siteTable");
      // get all divs under the siteTable element whose id's start with "thing" (each of these divs is a post)
      List<WebElement> postList = siteTable.findElements(By.xpath(".//div[starts-with(@id,'thing')]"));
      WebElement       post     = null;

      // iterate through the posts
      for (WebElement we : postList)
      {
         // get the span with class "rank", which indicates the order of the posts (also, ad posts don't have a rank)
         WebElement rankElement = we.findElement(By.className("rank"));
         // get the top post (i.e. rank == 1)
         if (rankElement.getText().trim().equals("1"))
         {
            post = we;
            break;
         }
      }

      return post;
   }

   /**
    * Upvotes the given post if it was downvoted or unvoted.
    *
    * @param post the post to upvote if necessary
    */
   public static void ensurePostUpvoted(WebElement post)
   {
      // use midcol to determine voting state of post
      WebElement midcol = getMidcolDivForPost(post);
      String midColClass = midcol.getAttribute("class");
      if (midColClass.contains("dislikes") || midColClass.contains("unvoted"))
      {
         /* click like button to bring post to upvoted */
         // first get like (up arrow) webelement
         WebElement upvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
         // send click to upvote arrow
         upvoteArrow.click();
      }
   }

   public static void ensurePostDownvoted(WebElement post)
   {
      // use midcol to determine voting state of post
      WebElement midcol = getMidcolDivForPost(post);
      String     midColClass = midcol.getAttribute("class");
      if ((midColClass.contains("likes") && !midColClass.contains("dislikes")) || midColClass.contains("unvoted"))
      {
         /* click downvote arrow to make post downvoted */
         // first get dislike (down arrow) webelement
         WebElement downvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
         // send click to downvote arrow
         downvoteArrow.click();
      }
   }

   public static void ensurePostNotVotedOn(WebElement post) throws Exception
   {
      // use midcol to determine voting state of post
      WebElement midcol = getMidcolDivForPost(post);
      String     midColClass = midcol.getAttribute("class");
      if (midColClass.contains("dislikes"))
      {
         /* click dislike button to return post to unvoted */
         // first get dislike (down arrow) webelement
         WebElement downvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
         // send click to downvote arrow
         downvoteArrow.click();
      }
      else if (midColClass.contains("likes"))
      {
         /* click like button to return post to unvoted */
         // first get like (up arrow) webelement
         WebElement upvoteArrow = midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
         // send click to upvote arrow
         upvoteArrow.click();
      }
   }

   public static WebElement getUpvoteArrowForPost(WebElement post)
   {
      // midcol of post contains the voting buttons
      WebElement midcol = getMidcolDivForPost(post);
      return midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
   }

   public static WebElement getDownvoteArrowForPost(WebElement post)
   {
      // midcol of post contains the voting buttons
      WebElement midcol = getMidcolDivForPost(post);
      return midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
   }

   public static void ensureOnRedditBasePage(WebDriver driver) throws TimeoutException
   {
      // only reload reddit if we are not already on reddit
      if (!driver.getCurrentUrl().equals(Constants.REDDIT_BASE_URL))
      {
         // not on reddit, reload page
         driver.get(Constants.REDDIT_BASE_URL);
         // wait for page to load
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(By.id("siteTable")));
      }
      // make sure that any dialogs that may have been open are now closed.
      ensureNoModalDialogOpen(driver);
   }

   public static void ensureLoggedOut(WebDriver driver) throws TimeoutException
   {
      // make sure we're on reddit
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (elementIsPresent(driver, logoutLinkLocator))
      {
         // we are logged in, so log out by clicking on logout link
         WebElement logoutLinkElement = driver.findElement(logoutLinkLocator);
         logoutLinkElement.click();
         // wait for logout to complete by waiting for login form to appear
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(By.id("login_login-main")));
      }
   }

   public static void ensureLoggedIn(WebDriver driver) throws TimeoutException
   {
      // make sure we're on reddit
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (!elementIsPresent(driver, logoutLinkLocator)) // if we are logged out, then log in
      {
         /* First, get the necessary web elements */
         WebElement usernameField = driver.findElement(new ByChained(By.id("login_login-main"), By.name("user")));
         WebElement passwordField = driver.findElement(new ByChained(By.id("login_login-main"), By.name("passwd")));
         WebElement loginButton = driver.findElement(new ByChained(By.id("login_login-main"), By.className("btn")));

         // input username and password
         usernameField.sendKeys(Constants.REDDIT_ACCT_NAME);
         passwordField.sendKeys(Constants.REDDIT_PASSWORD);

         // click login button to login
         loginButton.click();

         // wait for login to complete by waiting for logout link to appear
         WebElement element = (new WebDriverWait(driver, 10))
               .until(ExpectedConditions.visibilityOfElementLocated(logoutLinkLocator));
      }
   }

   /**
    * Gets the midcol div contained in the given post.
    * <p>
    * The midcol div contains the voting buttons and post score of the given post. Additionally, the class of the
    * midcol div changes (between "midcol unvoted", "midcol likes", "midcol dislikes") depending on the voting state
    * of the post, so we can use the "unvoted"/"likes"/"dislikes" part to determine how the post has been voted upon.
    *
    * @param post
    * @return
    */
   private static WebElement getMidcolDivForPost(WebElement post)
   {
      return post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
   }

   private static void ensureNoModalDialogOpen(WebDriver driver)
   {
      WebElement body = driver.findElement(By.xpath("//body"));
      if (body.getAttribute("class").contains("modal-open"))
      {
         /* modal dialog is open, so close it */
         // get the X button in the top right corner of the dialog
         WebElement xButton = body.findElement(new ByChained(By.className("modal-content"),
                                                             By.xpath(".//a[contains(@class,'c-close')]")));
         // click the X button
         xButton.click();

         // wait until the dialog is gone (by waiting until the X button is not visible).
         Wait<WebElement> wait = new FluentWait<WebElement>(xButton)
               .withTimeout(10, TimeUnit.SECONDS)
               .pollingEvery(100, TimeUnit.MILLISECONDS)
               .ignoring(NoSuchElementException.class);
         wait.until(new Function<WebElement, Boolean>()
         {
            @Override
            public Boolean apply(WebElement modal)
            {
               return !modal.isDisplayed();
            }
         });
      }
   }

   /**
    * Determines if the element specified by the given locator is immediately present on the current webpage.
    */
   private static boolean elementIsPresent(WebDriver driver, By locator)
   {
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      boolean present = (driver.findElements(locator).size() > 0);
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      return present;

   }
}
