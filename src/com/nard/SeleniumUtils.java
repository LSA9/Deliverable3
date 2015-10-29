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

   public static WebElement getTopPost(WebDriver driver)
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Getting top post");
      ensureOnRedditBasePage(driver);
      WebElement siteTable = driver.findElement(By.cssSelector(".sitetable"));
      assertEquals("Retrieved siteTable element", siteTable.getAttribute("id"), "siteTable");
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

   public static void ensurePostUpvoted(WebElement post)
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

      FancyPrinting.decreasePrintingIndent();
   }

   public static void ensurePostDownvoted(WebElement post)
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

      FancyPrinting.decreasePrintingIndent();
   }

   public static void ensurePostNotVotedOn(WebElement post) throws Exception
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

      FancyPrinting.decreasePrintingIndent();
   }

   public static WebElement getUpvoteArrowForPost(WebElement post)
   {
      WebElement midcol = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      return midcol.findElement(By.xpath(".//div[@aria-label='upvote']"));
   }

   public static WebElement getDownvoteArrowForPost(WebElement post)
   {
      WebElement midcol = post.findElement(By.xpath(".//div[contains(@class,'midcol')]"));
      return midcol.findElement(By.xpath(".//div[@aria-label='downvote']"));
   }

   public static void ensureOnRedditBasePage(WebDriver driver) throws TimeoutException
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
         WebElement xButton = body.findElement(new ByChained(By.className("modal-content"),
                                                             By.xpath(".//a[contains(@class,'c-close')]")));
         xButton.click();

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
         FancyPrinting.print("Modal dialog now closed");
      }
      else
      {
         FancyPrinting.print("Modal dialog not open");
      }
      FancyPrinting.decreasePrintingIndent();
   }

   public static void ensureLoggedOut(WebDriver driver) throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged out");
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (elementIsPresent(driver, logoutLinkLocator))
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

   public static void ensureLoggedIn(WebDriver driver) throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged in");
      ensureOnRedditBasePage(driver);
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.cssSelector(".logout.hover"), By.linkText("logout"));
      if (elementIsPresent(driver, logoutLinkLocator))
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

   private static boolean elementIsPresent(WebDriver driver, By locator)
   {
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      boolean present = (driver.findElements(locator).size() > 0);
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      return present;

   }
}
