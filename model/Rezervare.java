package model;

import java.time.LocalDate;

public class Rezervare {
    private static int idRezervare = 0;
    private Client client;
    private AgentVanzari agent;
    private Pachet pachet;
    private LocalDate dataRezervare;
    private Plata plata; // poate fi null la inceput

    public Rezervare(Client client, AgentVanzari agent, Pachet pachet, LocalDate dataRezervare) {
        idRezervare = idRezervare + 1;
        this.client = client;
        this.agent = agent;
        this.pachet = pachet;
        this.dataRezervare = dataRezervare;
    }

    // Compozitional: rezervarea creeaza plata
    public void efectueazaPlata(int nrPlata, double suma, LocalDate data) {
        if (plata == null) {
            this.plata = new Plata(nrPlata, suma, data, this);
        } else {
            System.out.println("Plata a fost deja efectuata.");
        }
    }

    public int getIdRezervare() {
        return idRezervare;
    }

    public Client getClient() {
        return client;
    }

    public AgentVanzari getAgent() {
        return agent;
    }

    public Pachet getPachet() {
        return pachet;
    }

    public LocalDate getDataRezervare() {
        return dataRezervare;
    }

    public Plata getPlata() {
        return plata;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    @Override
    public String toString() {
        return "Rezervare #" + idRezervare +
                "\nClient: " + client +
                "\nAgent: " + agent +
                "\nPachet: " + (pachet != null ? pachet.getTitlu() : "N/A") +
                "\nData: " + dataRezervare +
                (plata != null ? ("\nPlata: " + plata.getSuma() + " RON") : "\nPlata: neinregistrata");
    }
}
