package main.domain;

public class LimbaVorbita {
    private int idAngajat;
    private String limba;
    private String nivel; // poate fi null

    // Constructor complet
    public LimbaVorbita(int idAngajat, String limba, String nivel) {
        this.idAngajat = idAngajat;
        this.limba = limba;
        this.nivel = nivel;
    }

    // Constructor fara nivel (pentru compatibilitate)
    public LimbaVorbita(int idAngajat, String limba) {
        this(idAngajat, limba, null);
    }

    // Getters
    public int getIdAngajat() {
        return idAngajat;
    }

    public String getLimba() {
        return limba;
    }

    public String getNivel() {
        return nivel;
    }

    // Setters
    public void setIdAngajat(int idAngajat) {
        this.idAngajat = idAngajat;
    }

    public void setLimba(String limba) {
        this.limba = limba;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        LimbaVorbita that = (LimbaVorbita) obj;
        return idAngajat == that.idAngajat && 
               limba != null && limba.equals(that.limba);
    }

    @Override
    public int hashCode() {
        int result = idAngajat;
        result = 31 * result + (limba != null ? limba.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (nivel != null && !nivel.isEmpty()) {
            return limba + " (" + nivel + ")";
        } else {
            return limba;
        }
    }
} 