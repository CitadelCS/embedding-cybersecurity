package edu.citadel.cs.ecc.cs2;

public class BankAccount {
  private String name;
  private double balance;
  private String acctNum;

  public BankAccount(String name, double balance, String acctNum) {
    this.name = name;
    this.balance = balance;
    this.acctNum = acctNum;
  }

  public boolean withdraw(double amount) {
    if (amount > this.balance) return false;
    this.balance -= amount;
    return true;
  }

  public String toString() {
    return String.format("%s\nAcctNo: %s\n$%.2f\n", this.name, this.acctNum, this.balance);
  }

  public static void main(String[] args) {
    BankAccount a1 = new BankAccount("Hacker", 100, "1052-2234-8931");
    System.out.println(a1);
    if (a1.withdraw(-10000)) System.out.println("Withdrawal Successful.\n");
    System.out.println(a1.toString());
  }
}
