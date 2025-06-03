package main.view;

import main.domain.Angajat;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import main.domain.LimbaVorbita;
import main.controller.*;
import main.service.AuditService;
import main.service.LocatieService;
import main.service.LimbiService;
import main.domain.Locatie;
import java.util.Scanner;
import java.util.List;

public class AngajatView {
    private UnifiedAuthController authController;
    private CatalogController catalogController;
    private LocatieController locatieController;
    private ServiciuController serviciuController;
    private AuditService auditService;
    private LocatieService locatieService;
    private LimbiService limbiService;

    public AngajatView(UnifiedAuthController authController, CatalogController catalogController,
                      LocatieController locatieController, ServiciuController serviciuController) {
        this.authController = authController;
        this.catalogController = catalogController;
        this.locatieController = locatieController;
        this.serviciuController = serviciuController;
        this.auditService = AuditService.getInstance();
        this.locatieService = new LocatieService();
        this.limbiService = new LimbiService();
    }

    public void afiseazaMeniuGhid(Ghid ghid, Scanner sc) {
        boolean inGhidMenu = true;
        
        while (inGhidMenu) {
            afiseazaOptiuniGhid(ghid);
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    catalogController.afiseazaMeniuCatalog();
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.CAUTARE_LOCATII);
                    locatieController.afiseazaLocatiiDisponibile(sc);
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.CAUTARE_SERVICII);
                    serviciuController.afiseazaServiciiDisponibile(sc);
                    break;
                case "4":
                    gestioneazaLimbiGhid(ghid, sc);
                    break;
                case "5":
                    auditService.logAction(AuditService.Actions.VIEW_PROFILE);
                    afiseazaProfilAngajat(ghid);
                    break;
                case "6":
                    afiseazaDetaliiSalariu(ghid);
                    break;
                case "7":
                    auditService.logAction(AuditService.Actions.LOGOUT);
                    authController.getUserService().logout();
                    afiseazaLogoutMessage();
                    inGhidMenu = false;
                    break;
                default:
                    afiseazaOptiuneInvalida();
            }
        }
    }

    public void afiseazaMeniuAgent(AgentVanzari agent, Scanner sc) {
        boolean inAgentMenu = true;
        
        while (inAgentMenu) {
            afiseazaOptiuniAgent(agent);
            String optiune = sc.nextLine().trim();

            switch (optiune) {
                case "1":
                    catalogController.afiseazaMeniuCatalog();
                    break;
                case "2":
                    auditService.logAction(AuditService.Actions.CAUTARE_LOCATII);
                    locatieController.afiseazaLocatiiDisponibile(sc);
                    break;
                case "3":
                    auditService.logAction(AuditService.Actions.CAUTARE_SERVICII);
                    serviciuController.afiseazaServiciiDisponibile(sc);
                    break;
                case "4":
                    afiseazaStatisticiVanzari(agent);
                    break;
                case "5":
                    auditService.logAction(AuditService.Actions.VIEW_PROFILE);
                    afiseazaProfilAngajat(agent);
                    break;
                case "6":
                    afiseazaDetaliiSalariu(agent);
                    break;
                case "7":
                    auditService.logAction(AuditService.Actions.LOGOUT);
                    authController.getUserService().logout();
                    afiseazaLogoutMessage();
                    inAgentMenu = false;
                    break;
                default:
                    afiseazaOptiuneInvalida();
            }
        }
    }

    private void afiseazaOptiuniGhid(Ghid ghid) {
        System.out.println("\n=== MENIU GHID TURISTIC ===");
        System.out.println("Utilizator: " + ghid.getNume() + " " + ghid.getPrenume());
        
        String numeLocatie = getNumeLocatie(ghid.getIdLocatie());
        System.out.println("📍 Locatie de lucru: " + numeLocatie);
        
        // Afiseaza limbile din TreeSet (sortate automat)
        System.out.println("🗣️ Limbi vorbite (TreeSet): " + ghid.getLimbiVorbite().size() + " limbi");
        
        System.out.println("1. 🏖️ Catalog oferte");
        System.out.println("2. 🌍 Cautare locatii turistice");
        System.out.println("3. ⚙️ Cautare servicii disponibile");
        System.out.println("4. 🗣️ Gestiune limbi vorbite");
        System.out.println("5. 👤 Profilul meu");
        System.out.println("6. 💰 Detalii salariu");
        System.out.println("7. 🔓 Logout");
        System.out.print("Alege optiunea: ");
    }

    private void afiseazaOptiuniAgent(AgentVanzari agent) {
        System.out.println("\n=== MENIU AGENT VANZARI ===");
        System.out.println("Utilizator: " + agent.getNume() + " " + agent.getPrenume());
        System.out.println("Comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
        System.out.println("1. 🏖️ Catalog oferte");
        System.out.println("2. 🌍 Cautare locatii turistice");
        System.out.println("3. ⚙️ Cautare servicii disponibile");
        System.out.println("4. 💼 Statistici vanzari");
        System.out.println("5. 👤 Profilul meu");
        System.out.println("6. 💰 Detalii salariu");
        System.out.println("7. 🔓 Logout");
        System.out.print("Alege optiunea: ");
    }

    private void afiseazaProfilAngajat(Angajat angajat) {
        System.out.println("\n=== PROFILUL MEU ===");
        System.out.println("Nume: " + angajat.getNume() + " " + angajat.getPrenume());
        System.out.println("Email: " + angajat.getEmail());
        System.out.println("Job: " + angajat.getJobTitle());
        System.out.println("ID Angajat: " + angajat.getIdAngajat());
        
        if (angajat instanceof Ghid) {
            Ghid ghid = (Ghid) angajat;
            String numeLocatie = getNumeLocatie(ghid.getIdLocatie());
            System.out.println("📍 Locatie de lucru: " + numeLocatie);
            
            // Afiseaza TreeSet-ul sortat
            System.out.println("🗣️ Limbi vorbite (TreeSet sortat): " + ghid.getLimbiVorbite());
            
            // Afiseaza si detaliile din BD daca sunt disponibile
            List<LimbaVorbita> limbiCuDetalii = ghid.getLimbiCuDetalii();
            if (!limbiCuDetalii.isEmpty()) {
                System.out.println("📋 Detalii limbi din BD: " + ghid.getLimbiFormatate());
            }
        }
    }

    private void afiseazaDetaliiSalariu(Angajat angajat) {
        System.out.println("\n=== DETALII SALARIU ===");
        System.out.println("Nume: " + angajat.getNume() + " " + angajat.getPrenume());
        System.out.println("Job: " + angajat.getJobTitle());
        System.out.println("Salariu baza: " + angajat.getSalariuBaza() + " RON");
        System.out.println("Salariu calculat: " + String.format("%.2f", angajat.calculSalariu()) + " RON");
        
        if (angajat instanceof Ghid) {
            Ghid ghid = (Ghid) angajat;
            String numeLocatie = getNumeLocatie(ghid.getIdLocatie());
            System.out.println("📍 Locatie de lucru: " + numeLocatie);
            System.out.println("Bonus limbi (+10% per limba): " + ghid.getLimbiVorbite().size() + " limbi");
            System.out.println("🗣️ Limbi vorbite (TreeSet): " + ghid.getLimbiVorbite());
        } else if (angajat instanceof AgentVanzari) {
            AgentVanzari agent = (AgentVanzari) angajat;
            System.out.println("Comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
            System.out.println("Bonus comision: " + String.format("%.2f", agent.calculComision()) + " RON");
        }
    }

    private void gestioneazaLimbiGhid(Ghid ghid, Scanner sc) {
        boolean inLimbiMenu = true;
        
        while (inLimbiMenu) {
            afiseazaMeniuLimbi(ghid);
            String optiune = sc.nextLine().trim();
            
            switch (optiune) {
                case "1":
                    adaugaLimbaNoua(ghid, sc);
                    break;
                case "2":
                    afiseazaLimbiGhid(ghid);
                    break;
                case "3":
                    actualizeazaNivelLimba(ghid, sc);
                    break;
                case "4":
                    stergeLimba(ghid, sc);
                    break;
                case "5":
                    sincronizeazaLimbi(ghid);
                    break;
                case "6":
                    limbiService.afiseazaNiveluriDisponibile();
                    break;
                case "7":
                    inLimbiMenu = false;
                    break;
                default:
                    afiseazaOptiuneInvalida();
            }
        }
    }

    private void afiseazaMeniuLimbi(Ghid ghid) {
        System.out.println("\n=== GESTIUNE LIMBI VORBITE ===");
        System.out.println("Ghid: " + ghid.getNume() + " " + ghid.getPrenume());
        System.out.println("🗣️ TreeSet limbi (sortate): " + ghid.getLimbiVorbite());
        System.out.println("📊 Numar limbi: " + ghid.getLimbiVorbite().size());
        System.out.println("💰 Bonus salariu: +" + (ghid.getLimbiVorbite().size() * 10) + "%");
        System.out.println();
        System.out.println("1. ➕ Adauga limba noua");
        System.out.println("2. 👁️ Vezi toate limbile");
        System.out.println("3. ✏️ Actualizeaza nivel limba");
        System.out.println("4. ❌ Sterge limba");
        System.out.println("5. 🔄 Sincronizeaza cu BD");
        System.out.println("6. 📋 Vezi niveluri disponibile");
        System.out.println("7. ⬅️ Inapoi");
        System.out.print("Alege optiunea: ");
    }

    private void adaugaLimbaNoua(Ghid ghid, Scanner sc) {
        System.out.print("Introdu numele limbii: ");
        String limba = sc.nextLine().trim();
        
        if (limba.isEmpty()) {
            System.out.println("❌ Numele limbii nu poate fi gol!");
            return;
        }
        
        System.out.print("Introdu nivelul (optional, Enter pentru a sari): ");
        String nivel = sc.nextLine().trim();
        
        if (nivel.isEmpty()) {
            nivel = null;
        } else if (!limbiService.esteNivelValid(nivel)) {
            System.out.println("⚠️ Nivel invalid!");
            limbiService.afiseazaNiveluriDisponibile();
            return;
        }
        
        // Adauga in TreeSet si sincronizeaza cu BD
        ghid.adaugaLimba(limba, nivel);
        
        System.out.println("✅ Limba adaugata in TreeSet!");
        System.out.println("🗣️ TreeSet actualizat: " + ghid.getLimbiVorbite());
    }

    private void afiseazaLimbiGhid(Ghid ghid) {
        System.out.println("\n=== LIMBILE MELE ===");
        System.out.println("🗣️ TreeSet (sortate automat): " + ghid.getLimbiVorbite());
        System.out.println("📊 Numar total: " + ghid.getLimbiVorbite().size());
        
        // Afiseaza si detaliile din BD
        List<LimbaVorbita> limbiCuDetalii = ghid.getLimbiCuDetalii();
        if (!limbiCuDetalii.isEmpty()) {
            System.out.println("\n📋 DETALII DIN BAZA DE DATE:");
            for (int i = 0; i < limbiCuDetalii.size(); i++) {
                LimbaVorbita limba = limbiCuDetalii.get(i);
                System.out.println((i + 1) + ". " + limba.toString());
            }
        } else {
            System.out.println("📋 Nu sunt detalii in BD (doar TreeSet)");
        }
    }

    private void actualizeazaNivelLimba(Ghid ghid, Scanner sc) {
        if (ghid.getLimbiVorbite().isEmpty()) {
            System.out.println("❌ Nu ai nicio limba inregistrata!");
            return;
        }
        
        System.out.println("Limbile tale (TreeSet): " + ghid.getLimbiVorbite());
        System.out.print("Introdu numele limbii pentru actualizare nivel: ");
        String limba = sc.nextLine().trim();
        
        if (!ghid.vorbeste(limba)) {
            System.out.println("❌ Limba '" + limba + "' nu exista in TreeSet!");
            return;
        }
        
        limbiService.afiseazaNiveluriDisponibile();
        System.out.print("Introdu noul nivel: ");
        String nivelNou = sc.nextLine().trim();
        
        if (!limbiService.esteNivelValid(nivelNou)) {
            System.out.println("❌ Nivel invalid!");
            return;
        }
        
        boolean success = ghid.actualizeazaNivelLimba(limba, nivelNou);
        if (success) {
            System.out.println("✅ Nivel actualizat cu succes!");
        } else {
            System.out.println("❌ Eroare la actualizarea nivelului!");
        }
    }

    private void stergeLimba(Ghid ghid, Scanner sc) {
        if (ghid.getLimbiVorbite().isEmpty()) {
            System.out.println("❌ Nu ai nicio limba inregistrata!");
            return;
        }
        
        System.out.println("Limbile tale (TreeSet): " + ghid.getLimbiVorbite());
        System.out.print("Introdu numele limbii de sters: ");
        String limba = sc.nextLine().trim();
        
        boolean success = ghid.stergeLimba(limba);
        if (success) {
            System.out.println("✅ Limba stearsa din TreeSet si BD!");
            System.out.println("🗣️ TreeSet actualizat: " + ghid.getLimbiVorbite());
        } else {
            System.out.println("❌ Limba nu a fost gasita in TreeSet!");
        }
    }

    private void sincronizeazaLimbi(Ghid ghid) {
        System.out.println("🔄 Sincronizare TreeSet cu BD...");
        ghid.sincronizeazaLimbiCuBD();
        System.out.println("✅ Sincronizare completa!");
        System.out.println("🗣️ TreeSet: " + ghid.getLimbiVorbite());
    }

    private void afiseazaStatisticiVanzari(AgentVanzari agent) {
        System.out.println("\n=== STATISTICI VANZARI ===");
        System.out.println("Total vanzari: " + String.format("%.2f", agent.getTotalVanzari()) + " RON");
        System.out.println("Comision: " + String.format("%.2f", agent.getComisionPercentage()) + "%");
        System.out.println("Comision castigat: " + String.format("%.2f", agent.calculComision()) + " RON");
        System.out.println("Salariu total (baza + comision): " + String.format("%.2f", agent.calculSalariu()) + " RON");
    }

    private void afiseazaLogoutMessage() {
        System.out.println("Logout efectuat cu succes!");
    }

    private void afiseazaOptiuneInvalida() {
        System.out.println("Optiune invalida!");
    }

    public void afiseazaAutentificareReusita() {
        System.out.println("\n✅ Autentificare reusita!");
        System.out.println("Tip utilizator: " + authController.getCurrentUserType());
        System.out.println("Nume: " + authController.getCurrentUserName());
    }

    private String getNumeLocatie(int idLocatie) {
        Locatie locatie = locatieService.getLocatieById(idLocatie);
        if (locatie != null) {
            return locatie.getNume() + ", " + locatie.getTara().getNume() + " (ID: " + idLocatie + ")";
        } else {
            return "Locatie necunoscuta (ID: " + idLocatie + ")";
        }
    }
} 