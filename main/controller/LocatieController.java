package main.controller;

import main.service.LocatieService;
import main.domain.Locatie;
import java.util.List;
import java.util.Scanner;

public class LocatieController {
    private LocatieService locatieService;

    public LocatieController() {
        this.locatieService = new LocatieService();
    }

    public void afiseazaLocatiiDisponibile(Scanner sc) {
        boolean inLocatieMenu = true;

        while (inLocatieMenu) {
            System.out.println("\n=== CaUTARE LOCAtII TURISTICE ===");
            System.out.println("1. Vezi toate locatiile");
            System.out.println("2. Cautare dupa tara");
            System.out.println("3. Cautare dupa nume locatie");
            System.out.println("4. Detalii locatie");
            System.out.println("5. Vezi tarile disponibile");
            System.out.println("6. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    afiseazaToateLocatiile();
                    break;
                case "2":
                    cautaLocatiiDupaTara(sc);
                    break;
                case "3":
                    cautaLocatiiDupaNume(sc);
                    break;
                case "4":
                    afiseazaDetaliiLocatie(sc);
                    break;
                case "5":
                    afiseazaTariDisponibile();
                    break;
                case "6":
                    inLocatieMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaToateLocatiile() {
        System.out.println("\n=== TOATE LOCAtIILE DISPONIBILE ===");
        List<Locatie> locatii = locatieService.getToateLocatiile();
        
        if (locatii.isEmpty()) {
            System.out.println("Nu sunt locatii disponibile.");
        } else {
            for (Locatie loc : locatii) {
                System.out.println(loc);
            }
            System.out.println("\nTotal locatii: " + locatieService.getTotalLocatii());
        }
    }

    private void cautaLocatiiDupaTara(Scanner sc) {
        System.out.print("Introdu tara: ");
        String tara = sc.nextLine().trim();

        List<Locatie> rezultate = locatieService.cautaDupaTara(tara);

        System.out.println("\n=== REZULTATE CaUTARE DUPa tARa ===");
        if (rezultate.isEmpty()) {
            System.out.println("Nu s-au gasit locatii in tara specificata.");
        } else {
            for (Locatie loc : rezultate) {
                System.out.println(loc);
            }
        }
    }

    private void cautaLocatiiDupaNume(Scanner sc) {
        System.out.print("Introdu numele locatiei: ");
        String nume = sc.nextLine().trim();

        List<Locatie> rezultate = locatieService.cautaDupaNumeLocatie(nume);

        System.out.println("\n=== REZULTATE CaUTARE DUPa NUME ===");
        if (rezultate.isEmpty()) {
            System.out.println("Nu s-au gasit locatii cu numele specificat.");
        } else {
            for (Locatie loc : rezultate) {
                System.out.println(loc);
            }
        }
    }

    private void afiseazaDetaliiLocatie(Scanner sc) {
        System.out.print("Introdu ID-ul locatiei: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());

            Locatie locatie = locatieService.getLocatieById(id);
            if (locatie != null) {
                System.out.println("\n=== DETALII LOCAtIE ===");
                System.out.println("ID: " + locatie.getIdLocatie());
                System.out.println("Nume: " + locatie.getNume());
                System.out.println("tara: " + (locatie.getTara() != null ? locatie.getTara().getNume() : "Necunoscuta"));
                System.out.println("Cazari disponibile: " + locatie.getCazari().size());
            } else {
                System.out.println("Locatia cu ID-ul " + id + " nu a fost gasita.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalid!");
        }
    }

    private void afiseazaTariDisponibile() {
        System.out.println("\n=== taRI DISPONIBILE ===");
        List<String> tari = locatieService.getTariDisponibile();
        
        if (tari.isEmpty()) {
            System.out.println("Nu sunt tari disponibile.");
        } else {
            for (int i = 0; i < tari.size(); i++) {
                System.out.println((i + 1) + ". " + tari.get(i));
            }
        }
    }
} 
