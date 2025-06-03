package main.controller;

import main.domain.Oferta;
import main.domain.Plata;
import main.domain.enums.Sezon;
import main.service.CatalogService;
import main.service.PlataService;
import main.service.AuditService;

import java.util.List;
import java.util.Scanner;

public class CatalogController {
    private final CatalogService catalogService;
    private final PlataService plataService;
    private final Scanner scanner;

    public CatalogController() {
        this.catalogService = new CatalogService();
        this.plataService = new PlataService();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Afiseaza meniul pentru browsing catalog
     */
    public void afiseazaMeniuCatalog() {
        AuditService.getInstance().logAction("browsing_catalog");
        
        while (true) {
            System.out.println("\n🏖️ ========== CATALOG OFERTE ==========");
            System.out.println("1. Vezi toate ofertele");
            System.out.println("2. Filtreaza dupa sezon");
            System.out.println("3. Filtreaza dupa locatie");
            System.out.println("4. Filtreaza dupa pret maxim");
            System.out.println("5. Filtrare avansata (sezon + locatie)");
            System.out.println("6. Statistici catalog");
            System.out.println("0. Inapoi");
            System.out.print("Alegerea ta: ");

            int optiune = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (optiune) {
                case 1 -> afiseazaToateOfertele();
                case 2 -> filtreazaDupaSezon();
                case 3 -> filtreazaDupaLocatie();
                case 4 -> filtreazaDupaPret();
                case 5 -> filtrareAvansata();
                case 6 -> afiseazaStatistici();
                case 0 -> {
                    return;
                }
                default -> System.out.println("❌ Optiune invalida!");
            }
        }
    }

    /**
     * Afiseaza toate ofertele disponibile
     */
    private void afiseazaToateOfertele() {
        AuditService.getInstance().logAction("view_all_offers");
        
        List<Oferta> oferte = catalogService.getToateOfertele();
        if (oferte.isEmpty()) {
            System.out.println("😔 Nu sunt oferte disponibile momentan.");
            return;
        }

        System.out.println("\n🏖️ TOATE OFERTELE DISPONIBILE:");
        System.out.println("=====================================");
        for (int i = 0; i < oferte.size(); i++) {
            System.out.printf("\n[%d] %s\n", (i + 1), oferte.get(i));
        }
        
        System.out.println("\nDoresti sa rezervi vreo oferta? (da/nu): ");
        String raspuns = scanner.nextLine();
        if (raspuns.equalsIgnoreCase("da")) {
            System.out.print("Introdu numarul ofertei: ");
            int numarOferta = scanner.nextInt();
            scanner.nextLine();
            
            if (numarOferta > 0 && numarOferta <= oferte.size()) {
                Oferta ofertaSelectata = oferte.get(numarOferta - 1);
                proceseazaRezervare(ofertaSelectata);
            } else {
                System.out.println("❌ Numar invalid!");
            }
        }
    }

    /**
     * Filtreaza ofertele dupa sezon
     */
    private void filtreazaDupaSezon() {
        AuditService.getInstance().logAction("filter_by_season");
        
        System.out.println("\n🗓️ SELECTEAZA SEZONUL:");
        System.out.println("1. " + Sezon.PRIMAVARA);
        System.out.println("2. " + Sezon.VARA);
        System.out.println("3. " + Sezon.TOAMNA);
        System.out.println("4. " + Sezon.IARNA);
        System.out.println("5. " + Sezon.TOT_ANUL);
        System.out.print("Alegerea ta: ");

        int alegere = scanner.nextInt();
        scanner.nextLine();

        Sezon sezonSelectat = switch (alegere) {
            case 1 -> Sezon.PRIMAVARA;
            case 2 -> Sezon.VARA;
            case 3 -> Sezon.TOAMNA;
            case 4 -> Sezon.IARNA;
            case 5 -> Sezon.TOT_ANUL;
            default -> null;
        };

        if (sezonSelectat == null) {
            System.out.println("❌ Alegere invalida!");
            return;
        }

        List<Oferta> oferteSezon = catalogService.getOferteDupaSezon(sezonSelectat);
        afiseazaListaOferte(oferteSezon, "OFERTE PENTRU " + sezonSelectat.getNume().toUpperCase());
    }

    /**
     * Filtreaza ofertele dupa locatie
     */
    private void filtreazaDupaLocatie() {
        AuditService.getInstance().logAction("filter_by_location");
        
        System.out.print("📍 Introdu locatia dorita (sau parte din nume): ");
        String locatie = scanner.nextLine();
        
        List<Oferta> oferteLocatie = catalogService.getOferteDupaLocatie(locatie);
        afiseazaListaOferte(oferteLocatie, "OFERTE PENTRU LOCATIA: " + locatie.toUpperCase());
    }

    /**
     * Filtreaza ofertele dupa pret maxim
     */
    private void filtreazaDupaPret() {
        AuditService.getInstance().logAction("filter_by_price");
        
        System.out.print("💰 Introdu pretul maxim (RON): ");
        double pretMaxim = scanner.nextDouble();
        scanner.nextLine();
        
        List<Oferta> ofertePret = catalogService.getOferteDupaPret(pretMaxim);
        afiseazaListaOferte(ofertePret, "OFERTE SUB " + pretMaxim + " RON");
    }

    /**
     * Filtrare avansata cu sezon si locatie
     */
    private void filtrareAvansata() {
        AuditService.getInstance().logAction("advanced_filter");
        
        System.out.println("\n🔍 FILTRARE AVANSATA");
        
        // Selectare sezon (optional)
        System.out.println("Selecteaza sezon (0 pentru orice sezon):");
        System.out.println("1. " + Sezon.PRIMAVARA);
        System.out.println("2. " + Sezon.VARA);
        System.out.println("3. " + Sezon.TOAMNA);
        System.out.println("4. " + Sezon.IARNA);
        System.out.println("5. " + Sezon.TOT_ANUL);
        System.out.print("Alegerea ta: ");
        
        int alegereSezon = scanner.nextInt();
        scanner.nextLine();
        
        Sezon sezonSelectat = switch (alegereSezon) {
            case 1 -> Sezon.PRIMAVARA;
            case 2 -> Sezon.VARA;
            case 3 -> Sezon.TOAMNA;
            case 4 -> Sezon.IARNA;
            case 5 -> Sezon.TOT_ANUL;
            default -> null;
        };
        
        // Introducere locatie (optional)
        System.out.print("Introdu locatia (apasa Enter pentru orice locatie): ");
        String locatie = scanner.nextLine();
        if (locatie.trim().isEmpty()) {
            locatie = null;
        }
        
        List<Oferta> oferteFiltered = catalogService.getOferteFiltered(sezonSelectat, locatie);
        String titlu = "REZULTATE FILTRARE";
        if (sezonSelectat != null) titlu += " - " + sezonSelectat.getNume();
        if (locatie != null) titlu += " - " + locatie;
        
        afiseazaListaOferte(oferteFiltered, titlu);
    }

    /**
     * Afiseaza statisticile catalogului
     */
    private void afiseazaStatistici() {
        AuditService.getInstance().logAction("view_catalog_stats");
        System.out.println("\n" + catalogService.getStatisticiCatalog());
    }

    /**
     * Afiseaza o lista de oferte
     */
    private void afiseazaListaOferte(List<Oferta> oferte, String titlu) {
        System.out.println("\n🏖️ " + titlu);
        System.out.println("=".repeat(titlu.length() + 4));
        
        if (oferte.isEmpty()) {
            System.out.println("😔 Nu au fost gasite oferte care sa corespunda criteriilor.");
            return;
        }

        for (int i = 0; i < oferte.size(); i++) {
            System.out.printf("\n[%d] %s\n", (i + 1), oferte.get(i));
        }
        
        // Optiune de rezervare
        System.out.println("\nDoresti sa rezervi vreo oferta? (da/nu): ");
        String raspuns = scanner.nextLine();
        if (raspuns.equalsIgnoreCase("da")) {
            System.out.print("Introdu numarul ofertei: ");
            int numarOferta = scanner.nextInt();
            scanner.nextLine();
            
            if (numarOferta > 0 && numarOferta <= oferte.size()) {
                Oferta ofertaSelectata = oferte.get(numarOferta - 1);
                proceseazaRezervare(ofertaSelectata);
            } else {
                System.out.println("❌ Numar invalid!");
            }
        }
    }

    /**
     * Proceseza rezervarea unei oferte (doar pentru clienti)
     */
    private void proceseazaRezervare(Oferta oferta) {
        System.out.println("\n🎯 REZERVARE OFERTA: " + oferta.getNume());
        System.out.println("💰 Pret total: " + oferta.getPret() + " RON");
        
        System.out.print("📧 Introdu email-ul tau: ");
        String email = scanner.nextLine();
        
        // Simuleaza crearea rezervarii (pentru simplificare, folosim ID-ul ofertei)
        int idRezervare = oferta.getIdOferta();
        
        // Afiseaza metodele de plata
        plataService.afiseazaMetodePlataDispo();
        System.out.print("Selecteaza metoda de plata (card/transfer/paypal/cash): ");
        String metodaPlata = scanner.nextLine();
        
        if (!plataService.esteMetodaValidaPlata(metodaPlata)) {
            System.out.println("❌ Metoda de plata invalida!");
            return;
        }
        
        // Creaza plata
        Plata plata = plataService.creazaPlata(idRezervare, email, oferta.getPret(), metodaPlata);
        System.out.println("\n✅ Rezervare creata cu succes!");
        System.out.println("📄 ID Rezervare: " + idRezervare);
        System.out.println("💳 ID Plata: " + plata.getIdPlata());
        
        // Proceseza plata
        System.out.print("Proceseaza plata acum? (da/nu): ");
        String proceseaza = scanner.nextLine();
        if (proceseaza.equalsIgnoreCase("da")) {
            boolean success = plataService.proceseazaPlata(plata.getIdPlata());
            if (success) {
                AuditService.getInstance().logAction("reservation_completed");
                System.out.println("🎉 Rezervarea ta a fost confirmata!");
            } else {
                System.out.println("❌ Plata a esuat. Rezervarea ramane in asteptare.");
            }
        } else {
            System.out.println("⏳ Plata ramane in asteptare. O poti completa mai tarziu.");
        }
    }
} 