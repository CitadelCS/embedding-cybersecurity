package edu.citadel.cs.ecc.cs2;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class HandleExceptions {

  public static void main(String[] args) {
    try {
      doSomethingDangerous();
    } catch (Exception e) {
      System.out.println("Nothing to see here. Move along.");
    }
    doSomethingLessDangerous();
  }

  public static void doSomethingDangerous() throws IOException {
    Scanner fileScan = new Scanner(new File("this doesn't exist.txt"));
    fileScan.close();
  }

  public static void doSomethingLessDangerous() {
    try {
      Scanner fileScan = new Scanner(new File("this doesn't exist.txt"));
    } catch (IOException ioe) {
      System.out.println("Bad file name.");
    }
  }
}
