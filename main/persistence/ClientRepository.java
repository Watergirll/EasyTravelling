package main.persistence;

import main.domain.Client;
import main.domain.Rezervare;
import main.domain.Pachet;
import main.domain.AgentVanzari;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientRepository implements GenericRepository<Client> {
    private Connection connection;
    private static final String REZERVARI_CSV = "data/rezervari.csv";

    public ClientRepository() {
        this.connection = DBConn.getConnectionFromInstance();
        // Creeaza fisierul pentru rezervari daca nu exista
        try {
            File rezervariFile = new File(REZERVARI_CSV);
            rezervariFile.getParentFile().mkdirs();
            if (!rezervariFile.exists()) {
                try (FileWriter writer = new FileWriter(rezervariFile)) {
                    writer.write("email_client,id_rezervare,agent_email,pachet_id,pachet_nume,data_rezervare,plata_efectuata\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la crearea fisierului rezervari: " + e.getMessage());
        }
    }

    @Override
    public Client save(Client client) {
        String sql = "INSERT INTO CLIENT (id_client, nume, prenume, email, telefon) VALUES (SEQ_CLIENT.NEXTVAL, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNume());
            stmt.setString(2, client.getPrenume());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getTelefon());
            
            stmt.executeUpdate();
            System.out.println("Client salvat in BD: " + client.getEmail());
            
            // Salveaza rezervarile separat
            salveazaRezervarile(client);
            
            return client;
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea clientului in BD: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clienti = new ArrayList<>();
        String sql = "SELECT * FROM CLIENT";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Client client = new Client(
                    rs.getString("nume"),
                    rs.getString("prenume"),
                    rs.getString("email"),
                    rs.getString("telefon")
                );
                
                // Incarca rezervarile pentru acest client
                incarcaRezervarile(client);
                
                clienti.add(client);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea clientilor din BD: " + e.getMessage());
        }
        
        return clienti;
    }

    @Override
    public Optional<Client> findById(String email) {
        String sql = "SELECT * FROM CLIENT WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(
                        rs.getString("nume"),
                        rs.getString("prenume"),
                        rs.getString("email"),
                        rs.getString("telefon")
                    );
                    
                    // Incarca rezervarile pentru acest client
                    incarcaRezervarile(client);
                    
                    return Optional.of(client);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la gasirea clientului in BD: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE CLIENT SET nume = ?, prenume = ?, telefon = ? WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getNume());
            stmt.setString(2, client.getPrenume());
            stmt.setString(3, client.getTelefon());
            stmt.setString(4, client.getEmail());
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Client actualizat in BD: " + client.getEmail());
                
                // Actualizeaza si rezervarile
                salveazaRezervarile(client);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea clientului in BD: " + e.getMessage());
        }
    }

    @Override
    public void delete(Client client) {
        String sql = "DELETE FROM CLIENT WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getEmail());
            
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Client sters din BD: " + client.getEmail());
                
                // Sterge si rezervarile
                stergeRezervarile(client.getEmail());
            }
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea clientului din BD: " + e.getMessage());
        }
    }

    /**
     * Verifica daca un client exista in BD dupa email
     */
    public boolean existsByEmail(String email) {
        return findById(email).isPresent();
    }

    /**
     * Sincronizeaza un client din CSV cu BD
     */
    public void syncFromCSV(Client client) {
        if (existsByEmail(client.getEmail())) {
            update(client);
        } else {
            save(client);
        }
    }

    /**
     * Metoda pentru compatibilitate - alias pentru findAll()
     */
    public List<Client> loadAll() {
        return findAll();
    }

    /**
     * Metoda pentru compatibilitate - salveaza o lista de clienti
     */
    public void saveAll(List<Client> clienti) {
        for (Client client : clienti) {
            if (existsByEmail(client.getEmail())) {
                update(client);
            } else {
                save(client);
            }
        }
    }

    // =============== MANAGEMENT REZERVARI ===============

    /**
     * Salveaza rezervarile clientului in fisier CSV
     */
    private void salveazaRezervarile(Client client) {
        if (client.getRezervari() == null || client.getRezervari().isEmpty()) {
            return;
        }
        
        try {
            // Sterge rezervarile vechi ale clientului
            stergeRezervarile(client.getEmail());
            
            // Salveaza rezervarile noi
            try (FileWriter writer = new FileWriter(REZERVARI_CSV, true)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                
                for (Rezervare rezervare : client.getRezervari()) {
                    String agentEmail = rezervare.getAgent() != null ? rezervare.getAgent().getEmail() : "necunoscut";
                    String pachetId = rezervare.getPachet() != null ? String.valueOf(rezervare.getPachet().getIdPachet()) : "0";
                    String pachetNume = rezervare.getPachet() != null ? rezervare.getPachet().getTitlu() : "N/A";
                    String plataEfectuata = rezervare.getPlata() != null ? "da" : "nu";
                    
                    writer.write(String.format("%s,%d,%s,%s,%s,%s,%s\n",
                        client.getEmail(),
                        rezervare.getIdRezervare(),
                        agentEmail,
                        pachetId,
                        pachetNume,
                        rezervare.getDataRezervare().format(formatter),
                        plataEfectuata
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la salvarea rezervarilor pentru " + client.getEmail() + ": " + e.getMessage());
        }
    }

    /**
     * Incarca rezervarile clientului din fisier CSV
     */
    private void incarcaRezervarile(Client client) {
        try (BufferedReader reader = new BufferedReader(new FileReader(REZERVARI_CSV))) {
            String linie;
            reader.readLine(); // Skip header
            
            while ((linie = reader.readLine()) != null) {
                String[] parts = linie.split(",");
                if (parts.length >= 6 && parts[0].equals(client.getEmail())) {
                    try {
                        int idRezervare = Integer.parseInt(parts[1]);
                        String agentEmail = parts[2];
                        int pachetId = Integer.parseInt(parts[3]);
                        String pachetNume = parts[4];
                        LocalDate dataRezervare = LocalDate.parse(parts[5]);
                        boolean plataEfectuata = parts.length > 6 && "da".equals(parts[6]);
                        
                        // Creaza obiecte temporare pentru rezervare
                        AgentVanzari agent = new AgentVanzari("Agent", "Temp", agentEmail, "AGENT_VANZARI", 3000, 5.0);
                        Pachet pachet = new Pachet(pachetId, pachetNume, 7); // durata default
                        
                        Rezervare rezervare = new Rezervare(client, agent, pachet, dataRezervare);
                        
                        // Adauga plata daca exista
                        if (plataEfectuata) {
                            rezervare.efectueazaPlata(1, 1000.0, dataRezervare); // valori default
                        }
                        
                        client.getRezervari().add(rezervare);
                        
                    } catch (Exception e) {
                        System.err.println("Eroare la parsarea rezervarii: " + linie + " - " + e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Fisierul nu exista - nu e o problema
        } catch (IOException e) {
            System.err.println("Eroare la citirea rezervarilor: " + e.getMessage());
        }
    }

    /**
     * Sterge rezervarile clientului din fisier CSV
     */
    private void stergeRezervarile(String emailClient) {
        try {
            File fisierOriginal = new File(REZERVARI_CSV);
            File fisierTemp = new File(REZERVARI_CSV + ".tmp");
            
            try (BufferedReader reader = new BufferedReader(new FileReader(fisierOriginal));
                 FileWriter writer = new FileWriter(fisierTemp)) {
                
                String linie;
                // Copiaza header-ul
                if ((linie = reader.readLine()) != null) {
                    writer.write(linie + "\n");
                }
                
                // Copiaza toate liniile excepte cele pentru clientul specificat
                while ((linie = reader.readLine()) != null) {
                    String[] parts = linie.split(",");
                    if (parts.length > 0 && !parts[0].equals(emailClient)) {
                        writer.write(linie + "\n");
                    }
                }
            }
            
            // Inlocuieste fisierul original cu cel temporar
            if (fisierOriginal.delete()) {
                fisierTemp.renameTo(fisierOriginal);
            }
            
        } catch (IOException e) {
            System.err.println("Eroare la stergerea rezervarilor pentru " + emailClient + ": " + e.getMessage());
        }
    }
} 
