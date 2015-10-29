package com.nard;

/**
 * Created by Matt on 10/26/2015.
 */
public class FancyPrinting
{
   private static int indentLevel = 0;

   // no Instantiation
   private FancyPrinting()
   {
   }

   public static void print(String str)
   {
      for (int i = 0; i < indentLevel; i++)
      {
         System.out.print("   ");
      }
      System.out.println(str);
   }

   public static void clearPrintingIndent()
   {
      indentLevel = 0;
   }


   public static void increasePrintingIndent()
   {
      indentLevel++;
   }

   public static void decreasePrintingIndent()
   {
      indentLevel--;
   }
}
