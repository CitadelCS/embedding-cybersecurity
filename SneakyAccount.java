package edu.citadel.cs.ecc.cs2;

public class SneakyAccount extends BankAccount {

  public SneakyAccount(double balance) {
    super(balance);
  }


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

  public static void main(String[] args) {
    SneakyAccount a1 = new SneakyAccount(100);
    System.out.println(a1);
    if (a1.withdraw(-10000)) System.out.println("> Withdrawal Successful\n");
    System.out.println(a1.toString());
  }
}
