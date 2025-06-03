package main.service;

import main.domain.LimbaVorbita;
import main.domain.Ghid;
import main.persistence.LimbiVorbiteRepository;
import java.util.List;
import java.util.Arrays;

public class LimbiService {
    private LimbiVorbiteRepository limbiRepository;
    
    // Niveluri predefinite pentru limbi
    public static final List<String> NIVELURI_DISPONIBILE = Arrays.asList(
        "Incepator", "Elementar", "Intermediar", "Intermediar-Avansat", "Avansat", "Nativ"
    );

    public LimbiService() {
        this.limbiRepository = new LimbiVorbiteRepository();
    }

    /**
     * Adauga o limba noua pentru un ghid
     */
    public boolean adaugaLimbaGhid(int idAngajat, String limba, String nivel) {
        // Validare nivel
        if (nivel != null && !nivel.isEmpty() && !NIVELURI_DISPONIBILE.contains(nivel)) {
            System.out.println("⚠️ Nivel invalid. Niveluri disponibile: " + NIVELURI_DISPONIBILE);
            return false;
        }

        // Verifica daca limba exista deja
        if (limbiRepository.existaLimba(idAngajat, limba)) {
            System.out.println("⚠️ Limba '" + limba + "' exista deja pentru acest ghid!");
            return false;
        }

        // Creaza si salveaza limba
        LimbaVorbita limbaVorbita = new LimbaVorbita(idAngajat, limba, nivel);
        boolean success = limbiRepository.save(limbaVorbita);
        
        if (success) {
            System.out.println("✅ Limba '" + limba + "' adaugata cu succes!");
        } else {
            System.out.println("❌ Eroare la adaugarea limbii!");
        }
        
        return success;
    }

    /**
     * Actualizeaza nivelul unei limbi
     */
    public boolean actualizeazaNivelLimba(int idAngajat, String limba, String nivelNou) {
        // Validare nivel
        if (nivelNou != null && !nivelNou.isEmpty() && !NIVELURI_DISPONIBILE.contains(nivelNou)) {
            System.out.println("⚠️ Nivel invalid. Niveluri disponibile: " + NIVELURI_DISPONIBILE);
            return false;
        }

        // Verifica daca limba exista
        if (!limbiRepository.existaLimba(idAngajat, limba)) {
            System.out.println("⚠️ Limba '" + limba + "' nu exista pentru acest ghid!");
            return false;
        }

        boolean success = limbiRepository.updateNivel(idAngajat, limba, nivelNou);
        
        if (success) {
            System.out.println("✅ Nivelul limbii '" + limba + "' actualizat la '" + nivelNou + "'!");
        } else {
            System.out.println("❌ Eroare la actualizarea nivelului!");
        }
        
        return success;
    }

    /**
     * Sterge o limba pentru un ghid
     */
    public boolean stergeLimbaGhid(int idAngajat, String limba) {
        if (!limbiRepository.existaLimba(idAngajat, limba)) {
            System.out.println("⚠️ Limba '" + limba + "' nu exista pentru acest ghid!");
            return false;
        }

        boolean success = limbiRepository.delete(idAngajat, limba);
        
        if (success) {
            System.out.println("✅ Limba '" + limba + "' stearsa cu succes!");
        } else {
            System.out.println("❌ Eroare la stergerea limbii!");
        }
        
        return success;
    }

    /**
     * Incarca limbile pentru un ghid
     */
    public List<LimbaVorbita> getLimbiPentruGhid(int idAngajat) {
        return limbiRepository.getLimbiPentruAngajat(idAngajat);
    }

    /**
     * Afiseaza limbile unui ghid formatat
     */
    public void afiseazaLimbiGhid(int idAngajat, String numeGhid) {
        List<LimbaVorbita> limbi = getLimbiPentruGhid(idAngajat);
        
        System.out.println("\n=== LIMBI VORBITE - " + numeGhid + " ===");
        
        if (limbi.isEmpty()) {
            System.out.println("Nicio limba inregistrata.");
        } else {
            System.out.println("Numar total limbi: " + limbi.size());
            System.out.println("Bonus salariu: +" + (limbi.size() * 10) + "%");
            System.out.println("\nDetalii limbi:");
            
            for (int i = 0; i < limbi.size(); i++) {
                LimbaVorbita limba = limbi.get(i);
                System.out.println((i + 1) + ". " + limba.toString());
            }
        }
    }

    /**
     * Afiseaza nivelurile disponibile
     */
    public void afiseazaNiveluriDisponibile() {
        System.out.println("\n=== NIVELURI DISPONIBILE ===");
        for (int i = 0; i < NIVELURI_DISPONIBILE.size(); i++) {
            System.out.println((i + 1) + ". " + NIVELURI_DISPONIBILE.get(i));
        }
    }

    /**
     * Valideaza un nivel
     */
    public boolean esteNivelValid(String nivel) {
        return nivel == null || nivel.isEmpty() || NIVELURI_DISPONIBILE.contains(nivel);
    }

    /**
     * Returneaza toate limbile din sistem (pentru statistici)
     */
    public List<LimbaVorbita> getToateLimbile() {
        return limbiRepository.loadAll();
    }

    /**
     * Statistici rapide despre limbi
     */
    public void afiseazaStatisticiLimbi() {
        List<LimbaVorbita> toateLimbile = getToateLimbile();
        
        System.out.println("\n=== STATISTICI LIMBI VORBITE ===");
        System.out.println("Total intrari in BD: " + toateLimbile.size());
        
        // Numarare limbi unice
        long limbiUnice = toateLimbile.stream()
            .map(LimbaVorbita::getLimba)
            .distinct()
            .count();
        System.out.println("Limbi unice: " + limbiUnice);
        
        // Numarare ghizi cu limbi
        long ghiziCuLimbi = toateLimbile.stream()
            .map(LimbaVorbita::getIdAngajat)
            .distinct()
            .count();
        System.out.println("Ghizi cu limbi inregistrate: " + ghiziCuLimbi);
    }

    /**
     * Sincronizeaza limbile unui ghid cu BD
     */
    public boolean sincronizeazaLimbiPentruGhid(Ghid ghid) {
        if (ghid == null || ghid.getIdAngajat() <= 0) {
            return false;
        }

        try {
            List<LimbaVorbita> limbiDetaliate = ghid.getLimbiCuDetalii();
            return limbiRepository.sincronizeazaLimbi(ghid.getIdAngajat(), limbiDetaliate);
        } catch (Exception e) {
            System.err.println("Eroare la sincronizarea limbilor: " + e.getMessage());
            return false;
        }
    }
} 