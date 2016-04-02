package edu.citadel.cs.ecc.cs2;

public class RecursionMistakes {

  public static void main(String[] args) {
    long start = System.currentTimeMillis();
 //   System.out.print(fibSlow(50));
    System.out.printf(" -- fibSlow Time: %dms\n", (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    System.out.print(fibFast(50));
    System.out.printf(" -- fibFast Time: %dms\n", (System.currentTimeMillis() - start));
    
    reverseDisplay(-12345);
  }

  public static long fibFast(int n) {
    if (n <= 1) return n;
    return fibFastTR(1, 0, n);
  }

  private static long fibFastTR(long current, long previous, int n) {
    if (n == 1) return current;
    return fibFastTR(current + previous, current, n - 1);
  }

  /*
   * This famous example's implementation was borrowed from
   * http://introcs.cs.princeton.edu/java/23recursion/Fibonacci.java
   */
  public static long fibSlow(int n) {
    if (n <= 1) return n;
    else return fibSlow(n - 1) + fibSlow(n - 2);
  }

  public static void reverseDisplay(int n) {
    if (n != 0) {
      System.out.print(n % 10);
      reverseDisplay(n / 10);
    }
  }
}


/*
 * 12586269025 -- fibSlow Time: 50412ms 12586269025 -- fibFast Time: 0ms
 */

