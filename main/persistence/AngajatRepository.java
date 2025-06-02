package main.persistence;

import main.domain.Angajat;
import main.domain.Ghid;
import main.domain.AgentVanzari;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AngajatRepository implements GenericRepository<Angajat> {
    private Connection connection;

    public AngajatRepository() {
        this.connection = DBConn.getConnectionFromInstance();
    }

    @Override
    public Angajat save(Angajat angajat) {
        String sql = "INSERT INTO ANGAJAT (id_angajat, nume, prenume, email, telefon, salariu, job_id) VALUES (SEQ_ANGAJAT.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, angajat.getNume());
            stmt.setString(2, angajat.getPrenume());
            stmt.setString(3, angajat.getEmail());
            stmt.setString(4, ""); // telefon - nu avem in clasa Angajat
            stmt.setDouble(5, angajat.getSalariuBaza());
            stmt.setInt(6, 1); // job_id default - ar trebui sa avem mapping pentru JobType
            
            stmt.executeUpdate();
            System.out.println("Angajat salvat in BD: " + angajat.getEmail());
            return angajat;
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea angajatului in BD: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Angajat> findAll() {
        List<Angajat> angajati = new ArrayList<>();
        String sql = "SELECT a.*, g.id_angajat as ghid_id, av.id_angajat as agent_id FROM ANGAJAT a " +
                    "LEFT JOIN GHID g ON a.id_angajat = g.id_angajat " +
                    "LEFT JOIN AGENT_VANZARI av ON a.id_angajat = av.id_angajat";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Angajat angajat = createAngajatFromResultSet(rs);
                if (angajat != null) {
                    angajati.add(angajat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea angajatilor din BD: " + e.getMessage());
        }
        
        return angajati;
    }

    @Override
    public Optional<Angajat> findById(String email) {
        String sql = "SELECT a.*, g.id_angajat as ghid_id, av.id_angajat as agent_id FROM ANGAJAT a " +
                    "LEFT JOIN GHID g ON a.id_angajat = g.id_angajat " +
                    "LEFT JOIN AGENT_VANZARI av ON a.id_angajat = av.id_angajat " +
                    "WHERE a.email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Angajat angajat = createAngajatFromResultSet(rs);
                    return Optional.ofNullable(angajat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la gasirea angajatului in BD: " + e.getMessage());
        }
        
        return Optional.empty();
    }

    @Override
    public void update(Angajat angajat) {
        String sql = "UPDATE ANGAJAT SET nume = ?, prenume = ?, salariu = ? WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, angajat.getNume());
            stmt.setString(2, angajat.getPrenume());
            stmt.setDouble(3, angajat.getSalariuBaza());
            stmt.setString(4, angajat.getEmail());
            
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Angajat actualizat in BD: " + angajat.getEmail());
            }
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea angajatului in BD: " + e.getMessage());
        }
    }

    @Override
    public void delete(Angajat angajat) {
        String sql = "DELETE FROM ANGAJAT WHERE email = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, angajat.getEmail());
            
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Angajat sters din BD: " + angajat.getEmail());
            }
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea angajatului din BD: " + e.getMessage());
        }
    }

    /**
     * Creeaza un obiect Angajat din ResultSet
     */
    private Angajat createAngajatFromResultSet(ResultSet rs) throws SQLException {
        String nume = rs.getString("nume");
        String prenume = rs.getString("prenume");
        String email = rs.getString("email");
        double salariu = rs.getDouble("salariu");
        
        // Verificam tipul angajatului pe baza join-urilor
        boolean isGhid = rs.getObject("ghid_id") != null;
        boolean isAgent = rs.getObject("agent_id") != null;
        
        if (isGhid) {
            return new Ghid(nume, prenume, email, (int)salariu, 1); // default location
        } else if (isAgent) {
            return new AgentVanzari(nume, prenume, email, (int)salariu, 0.05); // default commission
        } else {
            return null; // Nu suportam alte tipuri deocamdata
        }
    }

    /**
     * Verifica daca un angajat exista in BD dupa email
     */
    public boolean existsByEmail(String email) {
        return findById(email).isPresent();
    }

    /**
     * Sincronizeaza un angajat din CSV cu BD
     */
    public void syncFromCSV(Angajat angajat) {
        if (existsByEmail(angajat.getEmail())) {
            update(angajat);
        } else {
            save(angajat);
        }
    }

    /**
     * Gaseste angajati dupa tip (folosind join-urile cu tabelele specifice)
     */
    public List<Angajat> findByType(String tipAngajat) {
        List<Angajat> angajati = new ArrayList<>();
        String sql;
        
        if ("GHID".equals(tipAngajat)) {
            sql = "SELECT a.*, g.id_angajat as ghid_id, NULL as agent_id FROM ANGAJAT a " +
                  "INNER JOIN GHID g ON a.id_angajat = g.id_angajat";
        } else if ("AGENT".equals(tipAngajat)) {
            sql = "SELECT a.*, NULL as ghid_id, av.id_angajat as agent_id FROM ANGAJAT a " +
                  "INNER JOIN AGENT_VANZARI av ON a.id_angajat = av.id_angajat";
        } else {
            return angajati; // Lista goala pentru tipuri necunoscute
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Angajat angajat = createAngajatFromResultSet(rs);
                if (angajat != null) {
                    angajati.add(angajat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la gasirea angajatilor dupa tip: " + e.getMessage());
        }
        
        return angajati;
    }

    /**
     * Metoda pentru compatibilitate - alias pentru findAll()
     */
    public List<Angajat> loadAll() {
        return findAll();
    }

    /**
     * Metoda pentru compatibilitate - salveaza o lista de angajati
     */
    public void saveAll(List<Angajat> angajati) {
        for (Angajat angajat : angajati) {
            if (existsByEmail(angajat.getEmail())) {
                update(angajat);
            } else {
                save(angajat);
            }
        }
    }
} 
