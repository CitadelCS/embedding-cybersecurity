package edu.citadel.cs.ecc.cs2;

public class SneakyAccount extends BankAccount {

  public SneakyAccount(double balance) {
    super(balance);
  }

  public boolean withdraw(double amount) {
    return true;
  }

  public double getBalance() {
    return 0;
  }

  public static void main(String[] args) {
    SneakyAccount a1 = new SneakyAccount(100);
    System.out.println(a1);
    if (a1.withdraw(-10000)) System.out.println("> Withdrawal Successful\n");
    System.out.println(a1.toString());
  }
}
