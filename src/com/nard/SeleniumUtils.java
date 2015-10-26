package com.nard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Contains static utility methods related to Selenium.
 */
public final class SeleniumUtils
{
   public static boolean elementIsPresent(WebDriver driver, By locator)
   {
      return driver.findElements(locator).size() > 0;
   }

   /**
    * Private constructor to prevent instantion.
    */
   private SeleniumUtils()
   {
   }
}
