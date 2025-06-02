package main.domain;

public class FirmaTransport {
    private int idFirmaTransport;
    private String nume;
    private String adresa;
    private String telefon;

    public FirmaTransport(int idFirmaTransport, String nume, String adresa, String telefon) {
        this.idFirmaTransport = idFirmaTransport;
        this.nume = nume;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    public int getIdFirmaTransport() {
        return idFirmaTransport;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    @Override
    public String toString() {
        return "FirmaTransport: " + nume +
                " | Adresa: " + adresa +
                " | Telefon: " + telefon;
    }
}

