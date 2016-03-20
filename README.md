# Embedding Cybersecurity Concepts - CS2

## Example 1: Validating Method Parameters
```java
  /*
   * This method does not validate the range of the parameter. It checks for sufficient funds, which
   * is a good banking check, but it does not prevent negative amounts, which equate to a deposit
   * not a withdrawal, and thus a potential exploit.
   */
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
