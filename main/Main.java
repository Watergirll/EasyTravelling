package main;

import test.TestAngajat;
import service.Service;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean ruleaza = true;

        while (ruleaza) {
            System.out.println("\n=== MENIU EASYTRAVELLING ===");
            System.out.println("1. Testeaza angajati");
            System.out.println("2. Testeaza creearea unei rezervari");
            System.out.println("3. Iesire");
            System.out.print("Alege optiunea: ");

            String opt = sc.nextLine().trim();

            switch (opt) {
                case "1":
                    TestAngajat.ruleazaTestAngajati();
                    break;
                case "2":
                    Service service = new Service();
                    service.simuleazaRezervare(sc);
                    break;
                case "3":
                    ruleaza = false;
                    break;
                default:
                    System.out.println("Optiune invalida. Incearca din nou.");
            }
        }

        System.out.println("Aplicatia s-a incheiat.");
    }
}
