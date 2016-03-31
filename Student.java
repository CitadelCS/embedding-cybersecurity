package edu.citadel.cs.ecc.cs2;

public class Student {
  int ID;
  double gpa;

  public Student(int i, double g) {
    ID = i;
    gpa = g;
  }

  public String toString() {
    return String.format("<ID: %d, GPA: %.2f>", ID, gpa);
  }
}
