package main.service;

import main.domain.Client;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private Map<String, Client> clienti = new HashMap<>();
    private Client currentClient = null;
    private static final String CSV_FILE_PATH = "data/clienti.csv";

    public AuthService() {
        incarcaClientiDinFisier();
    }

    // ============ BUSINESS LOGIC METHODS (fara UI) ============

    /**
     * Autentifica un client cu email si parola
     * @param email Email-ul clientului
     * @param parola Parola clientului
     * @return Client daca autentificarea reuseste, null altfel
     */
    public Client authenticateClient(String email, String parola) {
        Client client = clienti.get(email);
        
        if (client != null && client.getParola() != null && client.getParola().equals(parola)) {
            currentClient = client;
            return client;
        }
        return null;
    }

    /**
     * Verifica daca un email exista deja in sistem
     * @param email Email-ul de verificat
     * @return true daca email-ul exista, false altfel
     */
    public boolean emailExists(String email) {
        return clienti.containsKey(email);
    }

    /**
     * Creeaza un client nou
     * @param nume Numele clientului
     * @param prenume Prenumele clientului  
     * @param email Email-ul clientului
     * @param telefon Telefonul clientului
     * @param parola Parola clientului
     * @return true daca crearea reuseste, false altfel
     */
    public boolean createClient(String nume, String prenume, String email, String telefon, String parola) {
        try {
            // Verifica din nou daca email-ul exista (safety check)
            if (emailExists(email)) {
                return false;
            }

            // Creeaza clientul nou
            Client clientNou = new Client(nume, prenume, email, telefon);
            clientNou.setParola(parola);
            
            // Adauga in map
            clienti.put(email, clientNou);
            
            // Salveaza in fisier
            salveazaClientInFisier(clientNou);
            
            return true;
        } catch (Exception e) {
            System.err.println("Eroare la crearea clientului: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returneaza clientul curent logat
     * @return Client curent sau null daca nimeni nu e logat
     */
    public Client getCurrentClient() {
        return currentClient;
    }

    /**
     * Deconecteaza utilizatorul curent
     */
    public void logout() {
        currentClient = null;
    }

    /**
     * Returneaza numarul de clienti inregistrati
     * @return numarul de clienti
     */
    public int getClientsCount() {
        return clienti.size();
    }

    /**
     * Verifica daca un client este logat
     * @return true daca un client este logat, false altfel
     */
    public boolean isClientLoggedIn() {
        return currentClient != null;
    }

    // ============ PERSISTENCE METHODS ============

    private void incarcaClientiDinFisier() {
        try {
            // Verifica daca fisierul exista
            if (!Files.exists(Paths.get(CSV_FILE_PATH))) {
                System.out.println("Fisierul clienti.csv nu exista. Va fi creat la primul sign up.");
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH))) {
                String linie;
                while ((linie = reader.readLine()) != null) {
                    String[] parts = linie.split(",");
                    if (parts.length == 4) {
                        String nume = parts[0].trim();
                        String prenume = parts[1].trim();
                        String email = parts[2].trim();
                        String parola = parts[3].trim();
                        
                        Client client = new Client(nume, prenume, email, "");
                        client.setParola(parola);
                        
                        clienti.put(email, client);
                    }
                }
                System.out.println("Au fost incarcati " + clienti.size() + " clienti din fisier.");
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea fisierului clienti.csv: " + e.getMessage());
        }
    }

    private void salveazaClientInFisier(Client client) {
        try {
            // Creeaza fisierul daca nu exista
            File file = new File(CSV_FILE_PATH);
            file.getParentFile().mkdirs();
            
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(client.getNume() + "," + 
                           client.getPrenume() + "," + 
                           client.getEmail() + "," + 
                           client.getParola() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Eroare la salvarea clientului in fisier: " + e.getMessage());
        }
    }
} 
