package edu.citadel.cs.ecc.cs2;

public class BankAccount {
  private double balance;

  public BankAccount(double balance) {
    this.balance = balance;
  }

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

  public static void main(String[] args) {
    BankAccount a1 = new BankAccount(100);
    System.out.println(a1);
    if (a1.withdraw(-10000)) System.out.println("> Withdrawal Successful\n");
    System.out.println(a1.toString());
  }
}
