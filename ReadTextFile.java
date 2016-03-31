package edu.citadel.cs.ecc.cs2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadTextFile {

  public static void main(String[] args) throws IOException {
    System.out.println("Enter file name: ");
    Scanner input = new Scanner(System.in);
    String filename = input.nextLine();
    readFile(filename);
    input.close();
  }

  /*
   * This method is error prone. It first attempts to open a file named by the String parameter,
   * which could have come from anywhere. In this example it is read in the main method from
   * standard input and could be malformed or even malicious. In other contexts, this scenario can
   * be used for query injection attacks. If anything is wrong when opening the file, a FileNotFound
   * exception will be generated, but the method throws this exception to the calling method rather
   * than handling it gracefully. This can lead to DoS attacks if the exception is repeatedly
   * triggered. Next, this method presumes the integrity of the data in the file, which is assumed
   * to have int-double pairs on each line representing student ID numbers and corresponding GPAs.
   * If any non-numerical or malformed data is present in the file, a NumberFormatException will be
   * triggered. This can lead to DoS or injection attacks.
   * 
   * While the method is shorter, it is not worth the security risks imposed.
   */
  private static ArrayList<Student> readFile(String filename) throws FileNotFoundException {
    ArrayList<Student> roster = new ArrayList<Student>();
    Scanner fileScan = new Scanner(new File(filename));
    while (fileScan.hasNextLine()) {
      roster.add(new Student(fileScan.nextInt(), fileScan.nextDouble()));
    }
    fileScan.close();
    return roster;
  }

  /*
   * This is the better version of the method above. First, the integrity of the filename is checked
   * and the FileNotFound exception is handled directly rather than being thrown to the calling
   * method. Next, the file data is read but inspected for malformed lines and improper data types.
   * If these conditions are detected, the line is skipped and reading continues.
   */
  private static ArrayList<Student> readFileSecure(String filename) {
    ArrayList<Student> roster = new ArrayList<Student>();
    Scanner fileScan = null;
    try {
      fileScan = new Scanner(new File(filename));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }

    while (fileScan != null && fileScan.hasNextLine()) {
      int age = 0;
      double gpa = 0;
      String[] tokens = fileScan.nextLine().split(" \t");
      try {
        age = Integer.parseInt(tokens[0]);
        gpa = Double.parseDouble(tokens[1]);
      } catch (NumberFormatException e) {
        continue; /////////////////////////////////// does this cut the loop or the catch
      } finally { ////////////////////////////////////// is this right?
        roster.add(new Student(age, gpa));
      }
    }
    fileScan.close();
    return roster;
  }


}
