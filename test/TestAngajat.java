package test;

import main.domain.AgentVanzari;
import main.domain.Angajat;
import main.domain.Director;
import main.domain.Ghid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

    public class TestAngajat {
        public static void ruleazaTestAngajati() {
            try {
                Scanner in = new Scanner(new File("input/angajati.txt"));

                int n = in.nextInt();
                in.nextLine();
                Angajat[] a = new Angajat[n];

                String linie;
                for (int i = 0; i < n; i++) {
                    linie = in.nextLine();
                    String[] aux = linie.split(",");
                    int salariu = Integer.parseInt(aux[4]);

                    switch (aux[0].toUpperCase()) {
                        case "AGENT":
                            a[i] = new AgentVanzari(aux[1], aux[2], aux[3], aux[0], salariu, Double.parseDouble(aux[5]));
                            break;
                        case "GHID":
                            a[i] = new Ghid(aux[1], aux[2], aux[3], aux[0], salariu, Integer.parseInt(aux[5]));
                            break;
                        case "DIRECTOR":
                            a[i] = Director.getInstanceDirector(aux[1], aux[2], aux[3], aux[0], salariu);
                            break;
                    }

                }

                for (int i = 0; i < n; i++) {
                    System.out.println(a[i]);
                    //afisarea salariului fiecarui angajat
                    System.out.println("Angajatul castiga un salariu de " + a[i].calculSalariu() + "RON");
                }

            } catch (FileNotFoundException e) {
                System.out.println("Fisierul nu a fost gasit: " + e.getMessage());
            }
        }
    }



