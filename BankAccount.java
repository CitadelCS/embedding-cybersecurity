package edu.citadel.cs.ecc.cs2;

public class BankAccount {
  private double balance;

  public BankAccount(double balance) {
    this.balance = balance;
  }

  public boolean withdraw(double amount) {
    if (amount > this.balance) return false;
    this.balance -= amount;
    return true;
  }

  public double getBalance() {
    return this.balance;
  }

  public String toString() {
    String fmt = "Account Balance: $%,.2f\n";
    return String.format(fmt, this.getBalance());
  }

  public static void main(String[] args) {
    BankAccount a1 = new BankAccount(100);
    System.out.println(a1);
    if (a1.withdraw(-10000)) System.out.println("> Withdrawal Successful\n");
    System.out.println(a1.toString());
  }
}
