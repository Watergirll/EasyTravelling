package model;

import model.enums.Sezon;
import java.util.ArrayList;
import java.util.List;

public class Catalog {
    private int idCatalog;
    private Sezon sezon;
    private int an;
    private Tara tara;
    private List<Pachet> pachete = new ArrayList<>();

    public Catalog(int idCatalog, Sezon sezon, int an, Tara tara) {
        this.idCatalog = idCatalog;
        this.sezon = sezon;
        this.an = an;
        this.tara = tara;
    }

    public int getIdCatalog() {
        return idCatalog;
    }

    public Sezon getSezon() {
        return sezon;
    }

    public int getAn() {
        return an;
    }

    public Tara getTara() {
        return tara;
    }

    public void setTara(Tara tara) {
        if (this.tara != tara) {
            this.tara = tara;
            if (tara != null && !tara.getCataloage().contains(this)) {
                tara.adaugaCatalog(this); // ↔ sincronizare inversa (daca nu a fost deja adaugat)
            }
        }
    }

    public List<Pachet> getPachete() {
        return pachete;
    }

    public void adaugaPachet(Pachet pachet) {
        if (!pachete.contains(pachet)) {
            pachete.add(pachet);
            pachet.setCatalog(this); // ↔ sincronizare inversa
        }
    }


    @Override
    public String toString() {
        return "Catalog #" + idCatalog + " | " + sezon + " " + an +
                " | Tara: " + (tara != null ? tara.getNume() : "N/A") +
                " | Pachete: " + pachete.size();
    }
}
