package com.nard;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class TestVotingButtons
{
   private static WebDriver driver;

   /**
    * Creates the FirefoxDriver and sets the implicit timeout to 10 seconds.
    * <p>
    * Since this is annotated with @BeforeClass, this method is called once before a test run of this
    * class (rather than @Before, which runs once per method), which is done to improve performance.
    */
   @BeforeClass
   public static void openBrowser()
   {
      FancyPrinting.print("Opening Firefox...");
      driver = new FirefoxDriver();
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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
      driver.close();
   }

   private void ensureOnRedditBasePage() throws TimeoutException
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
      FancyPrinting.decreasePrintingIndent();
   }

   private void ensureLoggedOut() throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged out");
      ensureOnRedditBasePage();
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.className("logout hover"), By.linkText("logout"));
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

   private void ensureLoggedIn() throws TimeoutException
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Ensuring that the user is logged in");
      ensureOnRedditBasePage();
      // check if we're already logged in by looking for logout link
      ByChained logoutLinkLocator = new ByChained(By.className("logout hover"), By.linkText("logout"));
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

   private void logIn()
   {
      FancyPrinting.increasePrintingIndent();
      FancyPrinting.print("Logging the user in");
      ensureOnRedditBasePage();
      FancyPrinting.decreasePrintingIndent();
   }
}
