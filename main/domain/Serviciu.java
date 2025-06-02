package main.domain;

public class Serviciu {
    private int idServiciu;
    private String nume;
    private String categorie;
    private double pret;
    private int locuriDisponibile;
    private boolean disponibil;

    public Serviciu(int idServiciu, String nume, String categorie, double pret, int locuriDisponibile, boolean disponibil) {
        this.idServiciu = idServiciu;
        this.nume = nume;
        this.categorie = categorie;
        this.pret = pret;
        this.locuriDisponibile = locuriDisponibile;
        this.disponibil = disponibil;
    }

    // Getters
    public int getIdServiciu() {
        return idServiciu;
    }

    public String getNume() {
        return nume;
    }

    public String getCategorie() {
        return categorie;
    }

    public double getPret() {
        return pret;
    }

    public int getLocuriDisponibile() {
        return locuriDisponibile;
    }

    public boolean isDisponibil() {
        return disponibil;
    }

    // Setters
    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public void setLocuriDisponibile(int locuriDisponibile) {
        this.locuriDisponibile = locuriDisponibile;
    }

    public void setDisponibil(boolean disponibil) {
        this.disponibil = disponibil;
    }

    @Override
    public String toString() {
        return idServiciu + ": " + nume + " | " + categorie + " | " + pret + " RON | " + 
               "Locuri: " + locuriDisponibile + " | " + (disponibil ? "Disponibil" : "Indisponibil");
    }
} 
