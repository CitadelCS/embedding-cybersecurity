# Embedding Cybersecurity Concepts - CS2
These code examples are taken from our publication referenced below.
Verdicchio, Michael, Deepti Joshi, and Shankar M. Banik. "Embedding cybersecurity in the second programming course (CS2)." Journal of Computing Sciences in Colleges 32.2 (2016): 165-171.

## Example 1: Validating Method Parameters
This method does not validate the range of the parameter. It checks for sufficient funds, which is a good banking check, but it does not prevent negative amounts, which equate to a deposit not a withdrawal, and thus a potential exploit.
```java
  public boolean withdraw(double amount) {
    if (amount > this.balance) return false;
    this.balance -= amount;
    return true;
  }
```

## Example 2: Unintended Inheritance
Non-final classes can be extended. Even if instance variables remain private, the behavior of any public method can be overridden in the child class and produce misleading or unintended functionality. Through polymorphism, child class instances can referred to by any parent type reference.
```java
public class BankAccount {
  private double balance;

// Some code omitted...

  public double getBalance() {
    return this.balance;
  }

  /*
   * This public method references a public getter method, getBalance. Since this class does not
   * prevent inheritance, this method could end up invoking an overridden version of getBalance and
   * display inaccurate results.
   */
  public String toString() {
    String fmt = "Account Balance: $%,.2f";
    return String.format(fmt, this.getBalance());
  }
```
Inside the child class, called SneakyAccount:
```java
  /*
   * This method overrides the inherited version and returns a false result. The inherited toString
   * method will invoke this version for all child class types.
   */
  public double getBalance() {
    return 0;
  }

  /*
   * This method allows us to see what getBalance should have returned had it not been overridden.
   */
  public double getSuperBalance() {
    return super.getBalance();
  }
```

An example client that assumes it is working only with BankAccount objects:
```java
public class Bank {
  // This ArrayList will also hold subtypes of BankAccount
  private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

  /*
   * This example shows how a malicious child type can misrepresent object states if inheritance is
   * not prevented. Suppose a vulnerability exists in BankAccount which allows withdrawals of
   * negative numbers, creating deposits. Wishing to avoid attention, the child type SneakyAccount
   * is created and overrides the getBalance method to always report $0. The exploit is performed on
   * a normal account and for the malicious type, and when the account reporting procedure is run,
   * the balance change is noticed in the normal type but unnoticed in the child type.
   * 
   * If BankAccount is a final class, the inheritance is not possible. Alternatively, if the
   * addAccount and listAccountBalances methods prevent child type objects and prevent polymorphism,
   * the false reporting is not possible. Final classes preventing unwanted inheritance is the
   * recommended fix.
   */
  public static void main(String[] args) {
    BankAccount ba = new BankAccount(0);
    SneakyAccount sa = new SneakyAccount(0);
    Bank bank = new Bank();

    bank.addAccount(ba);
    bank.addAccount(sa);

    System.out.println("Initial Account Statuses:");
    bank.listAccountBalances();

    ba.withdraw(-10000);
    sa.withdraw(-10000);

    System.out.println("\nAfter withdrawing $-10000:");
    bank.listAccountBalances();

    System.out.println("\nSneakyAccount's true balance: " + sa.getSuperBalance());
  }
```
Here is the output for running the code above:
```
Initial Account Statuses:
Account Balance: $0.00
Account Balance: $0.00

After withdrawing $-10000:
Account Balance: $10,000.00
Account Balance: $0.00

SneakyAccount's true balance: 10000.0
```

## Example 3: Handling Exceptions
In Java and in many other languages, runtime exceptions must either be thrown (or passed) to the calling function, or handled where the exception occurs. The latter is always the preferred solution, as the former assumes that the calling function will properly interpret and handle the exception generated. Consider the following toy example:
```java
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
```
The first part of this example shows the main method intercepting a thrown exception from the `doSomethingDangerous` method, which attempts to open a non-existent file, generating a `FileNotFoundException`. But the main method is listening for any type of exception, and prints out a misleading result despite the critical error. Such techniques could be used to hide important error messages.

The second part of the example is where the same exception is handled where it is generated, in the `doSomethingLessDangerous` method. Even with the same attempt to suppress in the main method, the error message is still reported to standard output because it is handled on the spot. Here is the output from running the program:
```
PART 1: Nothing to see here. Move along.
Bad file name.
```
It is important to make sure that critical errors are handled in the most appropriate way to the particular software being developed -- whether that happens to be with code written by others or in the code being created. In some cases, however, there is no better choice but to throw the handling of exceptions to the calling function.

## Example 4: Validating File Input Data
The code below demonstrates an insecure and a secure way to read data from a text file in an expected format. If expectations regarding the format and types of data are not checked, it becomes possible for malicious data to cause undesired behavior.

This example reads in student data from a text file which is expected to have two data per line -- a student ID number followed by a GPA. A simple Student class was created to maintain the ID and GPA and to implement a `toString` method. The name of the file is specified by the user and then opened and processed by the program. The `readFileInsecure` method is too confident in the data being perfectly formed, and if anything goes wrong, the exceptions are thrown back to the main method (which does not handle them). The `readFileSecure` method is the better version of this method. It protects against an incorrect filename or missing file, and it verifies the format of each line of the file. If malformed data is found, the lines are skipped but processing continues instead of terminating abnormally.

```java
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadTextFile {

  public static void main(String[] args) throws IOException {
    System.out.print("Enter file name: ");
    Scanner input = new Scanner(System.in);
    String filename = input.nextLine();
    System.out.print("Part 1:\t");
    ArrayList<Student> rosterA = readFileSecure(filename);
    System.out.println(rosterA);
    System.out.print("\nPart 2:\t");
    ArrayList<Student> rosterB = readFileInsecure(filename);
    System.out.println(rosterB);
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
  private static ArrayList<Student> readFileInsecure(String filename) throws FileNotFoundException {
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
      return roster;
    }

    while (fileScan != null && fileScan.hasNextLine()) {
      int age = 0;
      double gpa = 0;
      String[] tokens = fileScan.nextLine().split("\\s+");
      if (tokens.length != 2) continue;
      try {
        age = Integer.parseInt(tokens[0]);
        gpa = Double.parseDouble(tokens[1]);
      } catch (NumberFormatException e) {
        continue; // skip the rest of the loop
      }
      roster.add(new Student(age, gpa));
    }
    fileScan.close();
    return roster;
  }
}
```

As a test run, the following textfile is provided to the program, which has errors on the 4th and 5th lines. The secure method skips them and reports the proper lines whereas the insecure method crashes with an error message. This kind of behavior can be exploited to creat DoS and/or injection attacks depending on the context.

```
123 4.00
456 3.82
789 2.49
abc	def
888 3.34 '-?
```
Output:
```
Enter file name: grades.txt
Part 1:	[<ID: 123, GPA: 4.00>, <ID: 456, GPA: 3.82>, <ID: 789, GPA: 2.49>]

Part 2:	Exception in thread "main" java.util.InputMismatchException
	at java.util.Scanner.throwFor(Unknown Source)
	at java.util.Scanner.next(Unknown Source)
	at java.util.Scanner.nextInt(Unknown Source)
	at java.util.Scanner.nextInt(Unknown Source)
	at ReadTextFile.readFileInsecure(ReadTextFile.java:40)
	at ReadTextFile.main(ReadTextFile.java:17)
```
## Example 5: Infinite and Inefficient Recursion
Recursion is an elegant programming technique whereby a method or function calls itself as part of solving a problem. The key factor is to ensure that recursive calls are made with a smaller instance of the problem, and that a recursive function is properly designed by having a base case to end a sequence of recursive calls. If a base case is omitted or malformed, an infinite recursive call chain will be activated. While an infinite loop will often cause the program to hang while a loop counter races, inifinite recursion will often crash the program due to a memory error. This type of crash, rather than a program stuck in execution, can be exploited for malicious purposes.

The reson that infinite recursion causes a memory error is because of the call stack used by the runtime environment. When a method is invoked, its local variables and information, and return address are stored in memory in a new activation frame and pushed onto the runtime stack. These frames are createded for every invocation, regardless if it is the same method being invoked many times or not. Infinite recursion will cause so many of these activation frames to be pushed onto the stack that it will eventually cause a stack overflow error due to insufficient memory.

Consider the following example which prints out the digits of a number in reverse order to standard output. The algorithm is straight-forward, with the modulus operation printing the least-significant digit and the integer division recursively calling with the remaining significant digits. However, no base case is provided to stop the recursion, which will print zero forever once all the digits have been divided out by the integer division. The method body should be wrapped in an if-statement testing for values of n which are strictly greater than zero.

```java
  public static void reverseDisplay(int n) {
    System.out.print(n % 10);
    reverseDisplay(n / 10);
  }
```

Infinite recursion is not the only exploitable scenario involving recursion. If a recursive solution is not applicable to all problem sizes, or if complex problem sizes are not detected, system resources can be wasted by an attacker by providing a valid but too-large problem size. A classic example of this is computing the nth Fibonacci number, which comes with a classic recurrence defnition which is easy to code as a recursive program, and is shown below:
```java
  /*
   * This famous example's implementation was borrowed from
   * http://introcs.cs.princeton.edu/java/23recursion/Fibonacci.java
   */
  public static long fibSlow(int n) {
    if (n <= 1) return n;
    else return fibSlow(n - 1) + fibSlow(n - 2);
  }
}
```
While this code runs fine for smaller problem sizes, it quickly gets stuck for moderately large problem sizes due to its wasteful complexity. Since it re-computes smaller subproblems, the runtime compounds with exponential complexity as the problem size increases. Situations like this are avoided by using loops instead of recursion, or by using tail-recursion, which passes along partial solutions to recursive calls so that the final recursive call can compile the final answer without backtracking through previous activation frames on the call stack. In fact, many compilers detect tail-recursion and optimize it so that it runs nearly as well as a loop. Here is the tail-recursive version of the nth Fibonacci calculation, which requires a wrapper method to begin, and passes in the current value, the previous value, and the number of numbers to go.
```java
  public static long fibFast(int n) {
    if (n <= 1) return n;
    return fibFastTR(1, 0, n);
  }

  private static long fibFastTR(long current, long previous, int n) {
    if (n == 1) return current;
    return fibFastTR(current + previous, current, n - 1);
  }
```
While it is less compact, it is exponentially faster. Both implementations were asked to compute the 50th Fibonacci number and were clocked for time elapsed. The timing code and console output are shown below. The tail-recursive version took less than 1ms while the fully recursive version took nearly one minute.
```java
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    System.out.print(fibSlow(50));
    System.out.printf(" -- fibSlow Time: %dms\n", (System.currentTimeMillis() - start));
    start = System.currentTimeMillis();
    System.out.print(fibFast(50));
    System.out.printf(" -- fibFast Time: %dms\n", (System.currentTimeMillis() - start));
  }
```
```
12586269025 -- fibSlow Time: 50412ms
12586269025 -- fibFast Time: 0ms
```
