package edu.citadel.cs.ecc.cs2;

import java.util.ArrayList;

public class Bank {
  private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

  public static void main(String[] args) {
    BankAccount ba = new BankAccount(0);
    SneakyAccount sa = new SneakyAccount(0);
    Bank bank = new Bank();

    ba.withdraw(-10000);
    sa.withdraw(-10000);

    bank.addAccount(ba);
    bank.addAccount(sa);

    bank.listAccountBalances();

  }

  private void listAccountBalances() {
    for (BankAccount a : accounts)
      System.out.println(a.toString());
  }

  public Bank() {}

  public void addAccount(BankAccount b) {
    accounts.add(b);
  }
}
