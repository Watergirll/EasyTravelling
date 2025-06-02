package main.persistence;

import main.domain.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements GenericRepository<Client> {
    private Connection connection;

    public ClientRepository() {
        this.connection = DBConn.getConnectionFromInstance();
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
} 
