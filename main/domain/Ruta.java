package main.domain;

import java.time.LocalDateTime;

public class Ruta {
    private int idRuta;
    private Locatie locatieStart;
    private Locatie locatieFinal;
    private LocalDateTime plecare;
    private LocalDateTime sosire;
    private Transport transport; // poate fi null (merge pe jos etc.)

    public Ruta(int idRuta, Locatie locatieStart, Locatie locatieFinal,
                LocalDateTime plecare, LocalDateTime sosire, Transport transport) {
        this.idRuta = idRuta;
        this.locatieStart = locatieStart;
        this.locatieFinal = locatieFinal;
        this.plecare = plecare;
        this.sosire = sosire;
        this.transport = transport;
    }

    // Getteri
    public int getIdRuta() {
        return idRuta;
    }

    public Locatie getLocatieStart() {
        return locatieStart;
    }

    public Locatie getLocatieFinal() {
        return locatieFinal;
    }

    public LocalDateTime getPlecare() {
        return plecare;
    }

    public LocalDateTime getSosire() {
        return sosire;
    }

    public Transport getTransport() {
        return transport;
    }

    // Setteri
    public void setLocatieStart(Locatie locatieStart) {
        this.locatieStart = locatieStart;
    }

    public void setLocatieFinal(Locatie locatieFinal) {
        this.locatieFinal = locatieFinal;
    }

    public void setPlecare(LocalDateTime plecare) {
        this.plecare = plecare;
    }

    public void setSosire(LocalDateTime sosire) {
        this.sosire = sosire;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Ruta #" + idRuta +
                " | De la: " + locatieStart.getNume() +
                " ? La: " + locatieFinal.getNume() +
                " | Plecare: " + plecare +
                " | Sosire: " + sosire +
                (transport != null ? " | Transport: " + transport : " | fara transport");
    }
}

