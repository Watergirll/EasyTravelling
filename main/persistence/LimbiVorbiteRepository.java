package main.persistence;

import main.domain.LimbaVorbita;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LimbiVorbiteRepository {
    private Connection connection;

    public LimbiVorbiteRepository() {
        this.connection = DBConn.getConnectionFromInstance();
    }

    /**
     * Salveaza o limba vorbita in BD
     */
    public boolean save(LimbaVorbita limbaVorbita) {
        String sql = "INSERT INTO LIMBI_VORBITE (ID_ANGAJAT, LIMBA, NIVEL) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limbaVorbita.getIdAngajat());
            stmt.setString(2, limbaVorbita.getLimba());
            stmt.setString(3, limbaVorbita.getNivel());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la salvarea limbii vorbite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Incarca toate limbile vorbite pentru un angajat
     */
    public List<LimbaVorbita> getLimbiPentruAngajat(int idAngajat) {
        List<LimbaVorbita> limbi = new ArrayList<>();
        String sql = "SELECT ID_ANGAJAT, LIMBA, NIVEL FROM LIMBI_VORBITE WHERE ID_ANGAJAT = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAngajat);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LimbaVorbita limba = new LimbaVorbita(
                        rs.getInt("ID_ANGAJAT"),
                        rs.getString("LIMBA"),
                        rs.getString("NIVEL")
                    );
                    limbi.add(limba);
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea limbilor vorbite: " + e.getMessage());
        }
        
        return limbi;
    }

    /**
     * Incarca toate limbile vorbite din BD
     */
    public List<LimbaVorbita> loadAll() {
        List<LimbaVorbita> limbi = new ArrayList<>();
        String sql = "SELECT ID_ANGAJAT, LIMBA, NIVEL FROM LIMBI_VORBITE ORDER BY ID_ANGAJAT, LIMBA";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                LimbaVorbita limba = new LimbaVorbita(
                    rs.getInt("ID_ANGAJAT"),
                    rs.getString("LIMBA"),
                    rs.getString("NIVEL")
                );
                limbi.add(limba);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la incarcarea tuturor limbilor vorbite: " + e.getMessage());
        }
        
        return limbi;
    }

    /**
     * Actualizeaza nivelul unei limbi
     */
    public boolean updateNivel(int idAngajat, String limba, String nivelNou) {
        String sql = "UPDATE LIMBI_VORBITE SET NIVEL = ? WHERE ID_ANGAJAT = ? AND LIMBA = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nivelNou);
            stmt.setInt(2, idAngajat);
            stmt.setString(3, limba);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea nivelului limbii: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sterge o limba vorbita
     */
    public boolean delete(int idAngajat, String limba) {
        String sql = "DELETE FROM LIMBI_VORBITE WHERE ID_ANGAJAT = ? AND LIMBA = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAngajat);
            stmt.setString(2, limba);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea limbii vorbite: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sterge toate limbile pentru un angajat
     */
    public boolean deleteToateLimbilePentruAngajat(int idAngajat) {
        String sql = "DELETE FROM LIMBI_VORBITE WHERE ID_ANGAJAT = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAngajat);
            
            int rowsAffected = stmt.executeUpdate();
            return true; // chiar daca nu sunt limbii de sters, operatiunea e ok
        } catch (SQLException e) {
            System.err.println("Eroare la stergerea limbilor pentru angajat: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica daca o limba exista pentru un angajat
     */
    public boolean existaLimba(int idAngajat, String limba) {
        String sql = "SELECT 1 FROM LIMBI_VORBITE WHERE ID_ANGAJAT = ? AND LIMBA = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAngajat);
            stmt.setString(2, limba);
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la verificarea limbii: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sincronizeaza limbile din memorie cu BD pentru un angajat
     */
    public boolean sincronizeazaLimbi(int idAngajat, List<LimbaVorbita> limbiNoi) {
        try {
            // Sterge toate limbile existente pentru angajat
            deleteToateLimbilePentruAngajat(idAngajat);
            
            // Adauga limbile noi
            for (LimbaVorbita limba : limbiNoi) {
                save(limba);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Eroare la sincronizarea limbilor: " + e.getMessage());
            return false;
        }
    }
} 