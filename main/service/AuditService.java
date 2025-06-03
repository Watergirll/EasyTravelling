package main.service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AuditService {
    private static final String AUDIT_FILE = "data/audit.csv";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static AuditService instance;

    // Singleton pattern pentru audit service
    private AuditService() {
        createDataDirectoryIfNotExists();
        createAuditFileIfNotExists();
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    /**
     * Înregistrează o acțiune în fișierul de audit
     * @param numeActiune Numele acțiunii executate
     */
    public void logAction(String numeActiune) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String auditLine = numeActiune + "," + timestamp;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(AUDIT_FILE, true))) {
            writer.println(auditLine);
        } catch (IOException e) {
            System.err.println("Eroare la scrierea în fișierul de audit: " + e.getMessage());
        }
    }

    /**
     * Înregistrează o acțiune cu detalii suplimentare
     * @param numeActiune Numele acțiunii executate
     * @param detalii Detalii suplimentare despre acțiune
     */
    public void logActionWithDetails(String numeActiune, String detalii) {
        String actionWithDetails = numeActiune + " - " + detalii;
        logAction(actionWithDetails);
    }

    /**
     * Citește toate intrările din fișierul de audit
     * @return Lista cu toate acțiunile auditate
     */
    public java.util.List<String> readAuditLog() {
        java.util.List<String> auditEntries = new java.util.ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(AUDIT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                auditEntries.add(line);
            }
        } catch (IOException e) {
            System.err.println("❌ Eroare la citirea fișierului de audit: " + e.getMessage());
        }
        
        return auditEntries;
    }

    /**
     * Afișează ultimele N acțiuni din audit
     * @param count Numărul de acțiuni de afișat
     */
    public void displayLastActions(int count) {
        java.util.List<String> auditEntries = readAuditLog();
        
        System.out.println("\n=== ULTIMELE " + count + " ACțIUNI AUDITATE ===");
        
        if (auditEntries.isEmpty()) {
            System.out.println("Nu există acțiuni în audit.");
            return;
        }
        
        int startIndex = Math.max(0, auditEntries.size() - count);
        for (int i = startIndex; i < auditEntries.size(); i++) {
            String[] parts = auditEntries.get(i).split(",", 2);
            if (parts.length == 2) {
                System.out.println("• " + parts[0] + " - " + parts[1]);
            }
        }
    }

    /**
     * Obține statistici despre acțiunile executate
     */
    public void displayAuditStatistics() {
        java.util.List<String> auditEntries = readAuditLog();
        java.util.Map<String, Integer> actionCounts = new java.util.HashMap<>();
        
        System.out.println("\n=== STATISTICI AUDIT ===");
        System.out.println("Total acțiuni executate: " + auditEntries.size());
        
        if (auditEntries.isEmpty()) {
            System.out.println("Nu există date de audit.");
            return;
        }
        
        // Contorizează acțiunile
        for (String entry : auditEntries) {
            String[] parts = entry.split(",", 2);
            if (parts.length >= 1) {
                String action = parts[0].trim();
                actionCounts.put(action, actionCounts.getOrDefault(action, 0) + 1);
            }
        }
        
        System.out.println("\nTop acțiuni executate:");
        actionCounts.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(10)
            .forEach(entry -> System.out.println("• " + entry.getKey() + ": " + entry.getValue() + " ori"));
        
        // Afișează prima și ultima acțiune
        if (!auditEntries.isEmpty()) {
            String[] firstParts = auditEntries.get(0).split(",", 2);
            String[] lastParts = auditEntries.get(auditEntries.size() - 1).split(",", 2);
            
            System.out.println("\nPrima acțiune: " + firstParts[0] + 
                (firstParts.length > 1 ? " (" + firstParts[1] + ")" : ""));
            System.out.println("Ultima acțiune: " + lastParts[0] + 
                (lastParts.length > 1 ? " (" + lastParts[1] + ")" : ""));
        }
    }

    /**
     * Șterge fișierul de audit (pentru resetare)
     */
    public void clearAuditLog() {
        try {
            Files.deleteIfExists(Paths.get(AUDIT_FILE));
            createAuditFileIfNotExists();
            System.out.println("✅ Audit log șters cu succes!");
        } catch (IOException e) {
            System.err.println("❌ Eroare la ștergerea audit log: " + e.getMessage());
        }
    }

    private void createDataDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            System.err.println("Eroare la crearea directorului data: " + e.getMessage());
        }
    }

    private void createAuditFileIfNotExists() {
        try {
            if (!Files.exists(Paths.get(AUDIT_FILE))) {
                Files.createFile(Paths.get(AUDIT_FILE));
            }
        } catch (IOException e) {
            System.err.println("Eroare la crearea fișierului de audit: " + e.getMessage());
        }
    }

    /**
     * Constante pentru numele acțiunilor (pentru consistență)
     */
    public static class Actions {
        public static final String TEST_ANGAJATI = "Test Angajati";
        public static final String TEST_REZERVARE = "Test Creare Rezervare";
        public static final String AUTENTIFICARE = "Autentificare Client/Angajat";
        public static final String CAUTARE_LOCATII = "Cautare Locatii Turistice";
        public static final String CAUTARE_SERVICII = "Cautare Servicii Disponibile";
        public static final String MANAGEMENT_REZERVARI = "Management Rezervari Admin";
        public static final String RAPOARTE_STATISTICI = "Rapoarte si Statistici";
        public static final String MANAGEMENT_CLIENTI = "Management Clienti Admin";
        public static final String MANAGEMENT_ANGAJATI = "Management Angajati Admin";
        public static final String IESIRE = "Iesire din Aplicatie";
        
        // Sub-acțiuni pentru detalii suplimentare
        public static final String LOGIN_CLIENT = "Login Client";
        public static final String LOGIN_ANGAJAT = "Login Angajat";
        public static final String SIGNUP_CLIENT = "Signup Client";
        public static final String SIGNUP_ANGAJAT = "Signup Angajat";
        public static final String LOGOUT = "Logout";
        public static final String VIEW_PROFILE = "View Profile";
        public static final String VIEW_CATALOG = "View Catalog";
        public static final String CREATE_RESERVATION = "Create Reservation";
        public static final String CREATE_CLIENT = "Create Client";
        public static final String UPDATE_CLIENT = "Update Client";
        public static final String DELETE_CLIENT = "Delete Client";
        public static final String CREATE_ANGAJAT = "Create Angajat";
        public static final String UPDATE_ANGAJAT = "Update Angajat";
        public static final String DELETE_ANGAJAT = "Delete Angajat";
        public static final String GENERATE_REPORT = "Generate Report";
        public static final String BACKUP_DATA = "Backup Data";
    }
} 