import java.util.Arrays;
import java.util.Scanner;

import java.util.ArrayList;

public class Exercices_1_2
{
   public static void main(String[] args)
   {
      System.out.println("=== Exercice 1 et 2:\n");
      
      Scanner in = new Scanner(System.in);

      System.out.print("Saisissez un entier: ");      
      int x = Integer.parseInt(in.nextLine());

      if (isEven(x))
         System.out.println(x + " est pair.");
      else
         System.out.println(x + " est impair.");

      if (isPositive(x))
         System.out.println(x + " est positif ou nul.");
      else
         System.out.println(x + " est négatif.");

      
      System.out.print("Saisissez les entiers du tableau (un élément par ligne, terminé par une ligne vide): ");

      ArrayList<Integer> arrList = new ArrayList<Integer>();
      String line = in.nextLine();
      
      while (! line.isEmpty())
      {
         arrList.add( Integer.parseInt(line) );

         line = in.nextLine();
      }

      in.close();

      Integer[] arr = new Integer[arrList.size()];
      arr = arrList.toArray(arr);

      System.out.println("tableau saisi: " + Arrays.toString(arr));
      quicksort(arr);
      System.out.println("tableau trié: " + Arrays.toString(arr));
   }

   
   // EXERCICE 1   
   static boolean isEven(int x)
   {
      return (x % 2 == 0);
   }
   static boolean isPositive(int x)
   {
      return (x >= 0);
   }

   
   // EXERCICE 2
   public static void quicksort(Comparable[] arr)
   {
      quicksort(arr, 0, arr.length);
   }
   public static void quicksort(Comparable[] arr, int begin, int length)
   {
      if (length < 2)
      {}
      else if (length == 2)
      {
         if ( arr[begin].compareTo( arr[begin+length-1] ) > 0 )
            swap(arr, begin, begin+length-1);
      }
      else
      {
         Comparable pivot = arr[begin + length - 1];

         int leftI  = begin;
         int rightI = begin + length - 2;

         while (leftI < rightI)
         {
            for (; leftI < begin+length-1 && arr[leftI].compareTo(pivot) <= 0; leftI++);
            for (; rightI >= 0            && arr[rightI].compareTo(pivot) > 0; rightI--);

            if (leftI < rightI)
               swap(arr, leftI, rightI);
         }

         swap(arr, leftI, begin+length-1);

         quicksort(arr, begin, leftI - begin);
         quicksort(arr, leftI, begin+length - leftI);
      }
   }
   public static void swap(Object[] arr, int a, int b)
   {
      Object tmp = arr[a];
      arr[a]     = arr[b];
      arr[b]     = tmp;
   }
}
