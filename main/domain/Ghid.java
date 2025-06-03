package main.domain;

import main.domain.enums.JobType;
import main.persistence.LimbiVorbiteRepository;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;

public class Ghid extends Angajat {
    private int idLocatie; // presupunem ca fiecare ghid este asignat unei singure locatii
    private Set<String> limbiVorbite = new TreeSet<>(); // TreeSet = colectie sortata automat - CERINTA PROIECT
    private LimbiVorbiteRepository limbiRepository;

    // Constructor cu JobType
    public Ghid(String nume, String prenume, String email, JobType jobType, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, jobType, salariuBaza);
        this.idLocatie = idLocatie;
        this.limbiRepository = new LimbiVorbiteRepository();
        incarcaLimbiDinBD();
    }

    // Constructor cu String pentru compatibilitate cu codul existent
    public Ghid(String nume, String prenume, String email, String job, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, JobType.GHID, salariuBaza);
        this.idLocatie = idLocatie;
        this.limbiRepository = new LimbiVorbiteRepository();
        incarcaLimbiDinBD();
    }

    // Constructor implicit pentru ghizi (foloseste automat JobType.GHID)
    public Ghid(String nume, String prenume, String email, int salariuBaza, int idLocatie) {
        super(nume, prenume, email, JobType.GHID, salariuBaza);
        this.idLocatie = idLocatie;
        this.limbiRepository = new LimbiVorbiteRepository();
        incarcaLimbiDinBD();
    }

    /**
     * Incarca limbile din BD in TreeSet la crearea ghidului
     */
    private void incarcaLimbiDinBD() {
        if (limbiRepository != null && this.getIdAngajat() > 0) {
            try {
                List<LimbaVorbita> limbiDinBD = limbiRepository.getLimbiPentruAngajat(this.getIdAngajat());
                
                // Populeaza TreeSet-ul cu limbile din BD
                limbiVorbite.clear();
                for (LimbaVorbita limba : limbiDinBD) {
                    limbiVorbite.add(limba.getLimba().toLowerCase());
                }
                
                System.out.println("✅ Incarcate " + limbiVorbite.size() + " limbi din BD pentru ghidul " + this.getIdAngajat());
            } catch (Exception e) {
                System.err.println("Eroare la incarcarea limbilor pentru ghidul " + this.getIdAngajat() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Adauga o limba noua vorbita (doar cu numele limbii)
     * Pastreaza TreeSet ca structura principala si sincronizeaza cu BD
     */
    public void adaugaLimba(String limba) {
        adaugaLimba(limba, null);
    }

    /**
     * Adauga o limba noua vorbita cu nivel optional
     * TreeSet ramane structura principala de date sortate
     */
    public void adaugaLimba(String limba, String nivel) {
        String limbaLower = limba.toLowerCase();
        
        // Adauga in TreeSet (structura principala sortata)
        boolean adaugat = limbiVorbite.add(limbaLower);
        
        if (adaugat) {
            // Sincronizeaza cu BD in fundal
            if (this.getIdAngajat() > 0 && limbiRepository != null) {
                try {
                    LimbaVorbita limbaVorbita = new LimbaVorbita(this.getIdAngajat(), limba, nivel);
                    
                    // Verifica daca nu exista deja in BD
                    if (!limbiRepository.existaLimba(this.getIdAngajat(), limba)) {
                        limbiRepository.save(limbaVorbita);
                        System.out.println("✅ Limba '" + limba + "' adaugata in TreeSet si salvata in BD");
                    }
                } catch (Exception e) {
                    System.err.println("Eroare la salvarea limbii in BD: " + e.getMessage());
                    // Limba ramane in TreeSet chiar daca BD-ul da eroare
                }
            }
        } else {
            System.out.println("⚠️ Limba '" + limba + "' exista deja in BD");
        }
    }

    /**
     * Actualizeaza nivelul unei limbi existente in BD (TreeSet nu stocheaza niveluri)
     */
    public boolean actualizeazaNivelLimba(String limba, String nivelNou) {
        String limbaLower = limba.toLowerCase();
        
        // Verifica daca limba exista in TreeSet
        if (!limbiVorbite.contains(limbaLower)) {
            System.out.println("⚠️ Limba '" + limba + "' nu exista in TreeSet!");
            return false;
        }

        // Actualizeaza nivelul in BD
        if (this.getIdAngajat() > 0 && limbiRepository != null) {
            try {
                boolean success = limbiRepository.updateNivel(this.getIdAngajat(), limba, nivelNou);
                if (success) {
                    System.out.println("✅ Nivelul limbii '" + limba + "' actualizat in BD la '" + nivelNou + "'");
                }
                return success;
            } catch (Exception e) {
                System.err.println("Eroare la actualizarea nivelului in BD: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    /**
     * Sterge o limba vorbita din TreeSet si BD
     */
    public boolean stergeLimba(String limba) {
        String limbaLower = limba.toLowerCase();
        
        // Sterge din TreeSet (structura principala)
        boolean removed = limbiVorbite.remove(limbaLower);
        
        if (removed) {
            // Sincronizeaza cu BD
            if (this.getIdAngajat() > 0 && limbiRepository != null) {
                try {
                    limbiRepository.delete(this.getIdAngajat(), limba);
                    System.out.println("✅ Limba '" + limba + "' stearsa din TreeSet si BD");
                } catch (Exception e) {
                    System.err.println("Eroare la stergerea limbii din BD: " + e.getMessage());
                    // Limba ramane stearsa din TreeSet chiar daca BD-ul da eroare
                }
            }
        }
        
        return removed;
    }

    /**
     * Verifica daca ghidul vorbeste o anumita limba (foloseste TreeSet)
     */
    public boolean vorbeste(String limba) {
        return limbiVorbite.contains(limba.toLowerCase());
    }

    /**
     * Sincronizeaza TreeSet-ul cu BD (salveaza toate limbile din TreeSet in BD)
     */
    public void sincronizeazaLimbiCuBD() {
        if (this.getIdAngajat() > 0 && limbiRepository != null) {
            try {
                // Sterge toate limbile din BD pentru acest ghid
                limbiRepository.deleteToateLimbilePentruAngajat(this.getIdAngajat());
                
                // Adauga toate limbile din TreeSet in BD
                for (String limba : limbiVorbite) {
                    LimbaVorbita limbaVorbita = new LimbaVorbita(this.getIdAngajat(), limba, null);
                    limbiRepository.save(limbaVorbita);
                }
                
                System.out.println("✅ TreeSet sincronizat cu BD pentru ghidul " + this.getIdAngajat() + " (" + limbiVorbite.size() + " limbi)");
            } catch (Exception e) {
                System.err.println("Eroare la sincronizarea TreeSet cu BD: " + e.getMessage());
            }
        }
    }

    /**
     * Returneaza limbile cu detalii din BD (pentru afisare cu niveluri)
     */
    public List<LimbaVorbita> getLimbiCuDetalii() {
        if (this.getIdAngajat() > 0 && limbiRepository != null) {
            return limbiRepository.getLimbiPentruAngajat(this.getIdAngajat());
        }
        return new ArrayList<>();
    }

    /**
     * Returneaza limbile formatate cu niveluri din BD
     */
    public String getLimbiFormatate() {
        List<LimbaVorbita> limbiCuDetalii = getLimbiCuDetalii();
        
        if (limbiCuDetalii.isEmpty()) {
            // Fallback la TreeSet daca BD nu are date
            if (limbiVorbite.isEmpty()) {
                return "Nicio limba inregistrata";
            } else {
                return String.join(", ", limbiVorbite);
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limbiCuDetalii.size(); i++) {
            LimbaVorbita lv = limbiCuDetalii.get(i);
            sb.append(lv.toString());
            if (i < limbiCuDetalii.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    // Get pentru id-ul locatiei
    public int getIdLocatie() {
        return idLocatie;
    }

    // Get pentru TreeSet-ul de limbi (structura principala sortata)
    public Set<String> getLimbiVorbite() {
        return limbiVorbite;
    }

    //logica: sa plateasca 10% in plus la salariu pentru fiecare limba vorbita (foloseste TreeSet.size())
    @Override
    public double calculSalariu() {
        return this.getSalariuBaza() * (1 + 0.1 * limbiVorbite.size());
    }

    // Calculul salariului cu bonus din job
    @Override
    public double calculSalariuCuBonus() {
        double salariuCuLimbi = calculSalariu();
        if (getJobType() != null) {
            return salariuCuLimbi * getJobType().getBonusFactor();
        }
        return salariuCuLimbi;
    }

    // Afisare frumoasa a obiectului (foloseste TreeSet pentru sortare automata)
    @Override
    public String toString() {
        return "Ghid: " + super.toString() + " | Locatie: " + idLocatie + " | Limbi vorbite (TreeSet sortat): " + limbiVorbite;
    }
}


