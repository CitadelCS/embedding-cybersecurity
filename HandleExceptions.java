package edu.citadel.cs.ecc.cs2;

import java.io.*;
import java.util.Scanner;

public class HandleExceptions {

  public static void main(String[] args) {
    // PART 1
    try {
      doSomethingDangerous();
    } catch (Exception e) {
      System.out.println("PART 1: Nothing to see here. Move along.");
    }
    // PART 2
    try {
      doSomethingLessDangerous();
    } catch (Exception e) {
      System.out.println("PART 2: Nothing to see here. Move along.");
    }
  }

  public static void doSomethingDangerous() throws IOException {
    Scanner fileScan = new Scanner(new File("this doesn't exist.txt"));
  }

  public static void doSomethingLessDangerous() {
    try {
      Scanner fileScan = new Scanner(new File("this doesn't exist.txt"));
    } catch (FileNotFoundException e) {
      System.out.println("Bad file name.");
    }
  }
}
