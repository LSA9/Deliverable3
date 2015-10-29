package com.nard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Contains static utility methods related to Selenium.
 */
public final class SeleniumUtils
{
   public static boolean elementIsPresent(WebDriver driver, By locator)
   {
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
      boolean present = (driver.findElements(locator).size() > 0);
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      return present;

   }

   /**
    * Private constructor to prevent instantion.
    */
   private SeleniumUtils()
   {
   }
}
