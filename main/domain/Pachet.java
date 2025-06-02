package main.domain;

public class Pachet {
    private int idPachet;
    private String titlu;
    private int durataZile;
    private Catalog catalog; // legatura catre catalogul parinte

    public Pachet(int idPachet, String titlu, int durataZile) {
        this.idPachet = idPachet;
        this.titlu = titlu;
        this.durataZile = durataZile;
    }

    public int getIdPachet() {
        return idPachet;
    }

    public String getTitlu() {
        return titlu;
    }

    public int getDurataZile() {
        return durataZile;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        if (this.catalog != catalog) {
            this.catalog = catalog;
            if (catalog != null && !catalog.getPachete().contains(this)) {
                catalog.adaugaPachet(this); // sincronizare inversa
            }
        }
    }

    @Override
    public String toString() {
        return "Pachet: " + titlu +
                " | Durata: " + durataZile + " zile" +
                " | Catalog: " + (catalog != null ? catalog.getIdCatalog() : "N/A");
    }
}

