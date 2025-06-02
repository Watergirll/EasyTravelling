package main.controller;

import main.service.ServiciuService;
import main.domain.Serviciu;
import java.util.List;
import java.util.Scanner;

public class ServiciuController {
    private ServiciuService serviciuService;

    public ServiciuController() {
        this.serviciuService = new ServiciuService();
    }

    public void afiseazaServiciiDisponibile(Scanner sc) {
        boolean inServiciuMenu = true;

        while (inServiciuMenu) {
            System.out.println("\n=== CaUTARE SERVICII DISPONIBILE ===");
            System.out.println("1. Vezi toate serviciile");
            System.out.println("2. Cautare dupa categorie");
            System.out.println("3. Cautare dupa pret maxim");
            System.out.println("4. Servicii disponibile acum");
            System.out.println("5. Detalii serviciu");
            System.out.println("6. Rezerva serviciu");
            System.out.println("7. Vezi categoriile disponibile");
            System.out.println("8. Back");
            System.out.print("Alege optiunea: ");

            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    afiseazaToateServiciile();
                    break;
                case "2":
                    cautaServiciiDupaCategorie(sc);
                    break;
                case "3":
                    cautaServiciiDupaPret(sc);
                    break;
                case "4":
                    afiseazaServiciiDisponibile();
                    break;
                case "5":
                    afiseazaDetaliiServiciu(sc);
                    break;
                case "6":
                    rezervaServiciu(sc);
                    break;
                case "7":
                    afiseazaCategoriiDisponibile();
                    break;
                case "8":
                    inServiciuMenu = false;
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    private void afiseazaToateServiciile() {
        System.out.println("\n=== TOATE SERVICIILE DISPONIBILE ===");
        List<Serviciu> servicii = serviciuService.getToateServiciile();
        
        if (servicii.isEmpty()) {
            System.out.println("Nu sunt servicii disponibile.");
        } else {
            for (Serviciu serv : servicii) {
                System.out.println(serv);
            }
            System.out.println("\nTotal servicii: " + serviciuService.getTotalServicii());
        }
    }

    private void cautaServiciiDupaCategorie(Scanner sc) {
        System.out.print("Introdu categoria: ");
        String categoria = sc.nextLine().trim();

        List<Serviciu> rezultate = serviciuService.cautaDupaCategorie(categoria);

        System.out.println("\n=== REZULTATE CaUTARE DUPa CATEGORIE ===");
        if (rezultate.isEmpty()) {
            System.out.println("Nu s-au gasit servicii in categoria specificata.");
        } else {
            for (Serviciu serv : rezultate) {
                System.out.println(serv);
            }
        }
    }

    private void cautaServiciiDupaPret(Scanner sc) {
        System.out.print("Introdu pretul maxim (RON): ");
        try {
            double pretMaxim = Double.parseDouble(sc.nextLine().trim());

            List<Serviciu> rezultate = serviciuService.cautaDupaPret(pretMaxim);

            System.out.println("\n=== REZULTATE CaUTARE DUPa PREt ===");
            if (rezultate.isEmpty()) {
                System.out.println("Nu s-au gasit servicii in bugetul specificat.");
            } else {
                for (Serviciu serv : rezultate) {
                    System.out.println(serv);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Pret invalid!");
        }
    }

    private void afiseazaServiciiDisponibile() {
        System.out.println("\n=== SERVICII DISPONIBILE ACUM ===");
        List<Serviciu> disponibile = serviciuService.getServiciiDisponibile();
        
        if (disponibile.isEmpty()) {
            System.out.println("Nu sunt servicii disponibile in acest moment.");
        } else {
            for (Serviciu serv : disponibile) {
                System.out.println(serv);
            }
        }
    }

    private void afiseazaDetaliiServiciu(Scanner sc) {
        System.out.print("Introdu ID-ul serviciului: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());

            Serviciu serviciu = serviciuService.getServiciuById(id);
            if (serviciu != null) {
                System.out.println("\n=== DETALII SERVICIU ===");
                System.out.println("ID: " + serviciu.getIdServiciu());
                System.out.println("Nume: " + serviciu.getNume());
                System.out.println("Categorie: " + serviciu.getCategorie());
                System.out.println("Pret: " + serviciu.getPret() + " RON");
                System.out.println("Locuri disponibile: " + serviciu.getLocuriDisponibile());
                System.out.println("Status: " + (serviciu.isDisponibil() ? "Disponibil" : "Indisponibil"));
            } else {
                System.out.println("Serviciul cu ID-ul " + id + " nu a fost gasit.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalid!");
        }
    }

    private void rezervaServiciu(Scanner sc) {
        System.out.print("Introdu ID-ul serviciului: ");
        try {
            int id = Integer.parseInt(sc.nextLine().trim());

            Serviciu serviciu = serviciuService.getServiciuById(id);
            if (serviciu != null) {
                if (serviciu.isDisponibil() && serviciu.getLocuriDisponibile() > 0) {
                    System.out.print("Numarul de locuri dorite: ");
                    int locuriDorite = Integer.parseInt(sc.nextLine().trim());

                    if (locuriDorite > 0 && locuriDorite <= serviciu.getLocuriDisponibile()) {
                        boolean rezervat = serviciuService.rezervaServiciu(id, locuriDorite);
                        
                        if (rezervat) {
                            double pretTotal = serviciuService.calculeazaPretTotal(id, locuriDorite);
                            
                            System.out.println("\n=== REZERVARE REUsITa ===");
                            System.out.println("Serviciu: " + serviciu.getNume());
                            System.out.println("Locuri rezervate: " + locuriDorite);
                            System.out.println("Pret total: " + pretTotal + " RON");
                            System.out.println("Locuri ramase: " + serviciu.getLocuriDisponibile());
                        } else {
                            System.out.println("Rezervarea nu a putut fi efectuata!");
                        }
                    } else {
                        System.out.println("Numarul de locuri solicitat este invalid!");
                    }
                } else {
                    System.out.println("Serviciul nu este disponibil in acest moment.");
                }
            } else {
                System.out.println("Serviciul cu ID-ul " + id + " nu a fost gasit.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Numar invalid!");
        }
    }

    private void afiseazaCategoriiDisponibile() {
        System.out.println("\n=== CATEGORII DISPONIBILE ===");
        List<String> categorii = serviciuService.getCategoriiDisponibile();
        
        if (categorii.isEmpty()) {
            System.out.println("Nu sunt categorii disponibile.");
        } else {
            for (int i = 0; i < categorii.size(); i++) {
                System.out.println((i + 1) + ". " + categorii.get(i));
            }
        }
    }
} 
