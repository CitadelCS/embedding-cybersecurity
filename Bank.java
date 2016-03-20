package edu.citadel.cs.ecc.cs2;

import java.util.ArrayList;

public class Bank {
  /*
   * This ArrayList will also hold subtypes of BankAccount
   */
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
  
   /* Output:
        Initial Account Statuses:
        Account Balance: $0.00
        Account Balance: $0.00
        
        After withdrawing $-10000:
        Account Balance: $10,000.00
        Account Balance: $0.00
        
        SneakyAccount's true balance: 10000.0
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

  /*
   * If the ArrayList contains BankAccounts and subtypes of BankAccount, it will polymorphically
   * refer to overridden versions of public methods.
   */
  private void listAccountBalances() {
    for (BankAccount a : accounts)
      System.out.println(a.toString());
  }

  public Bank() {}

  /*
   * This method will accept any subtype of BankAccount as well...
   */
  public void addAccount(BankAccount b) {
    accounts.add(b);
  }
}
