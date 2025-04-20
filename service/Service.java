package service;

import model.*;
import model.enums.Sezon;
import model.enums.TipTransport;

import java.time.LocalDate;
import java.util.*;

public class Service {

    private List<Tara> tari = new ArrayList<>();
    private List<Catalog> cataloage = new ArrayList<>();
    private List<Pachet> pachete = new ArrayList<>();
    private List<FirmaTransport> firme = new ArrayList<>();
    private List<Transport> transporturi = new ArrayList<>();

    public Service() {
        initializeazaDate();
    }

    private void initializeazaDate() {
        // Tari
        Tara italia = new Tara("Italia");
        Tara japonia = new Tara("Japonia");
        tari.addAll(List.of(italia, japonia));

        // Cataloge
        Catalog catalogVaraItalia = new Catalog(1, Sezon.VARA, 2025, italia);
        Catalog catalogIarnaJaponia = new Catalog(2, Sezon.IARNA, 2025, japonia);
        cataloage.addAll(List.of(catalogVaraItalia, catalogIarnaJaponia));

        // Pachete
        Pachet pachetRoma = new Pachet(1, "Roma City Tour", 5);
        catalogVaraItalia.adaugaPachet(pachetRoma);
        pachete.add(pachetRoma);

        Pachet pachetTokyo = new Pachet(2, "Descopera Tokyo", 7);
        catalogIarnaJaponia.adaugaPachet(pachetTokyo);
        pachete.add(pachetTokyo);

        // Firma transport
        FirmaTransport firma = new FirmaTransport(1, "TravelExpress", "Bucuresti", "+40700000000");
        firme.add(firma);

        // Transport
        Transport t1 = new Transport(TipTransport.AVION, 50, firma);
        transporturi.add(t1);
    }

    public void simuleazaRezervare(Scanner scanner) {
        System.out.println("=== Rezervare noua ===");

        System.out.println("In ce sezon vrei sa calatoresti? (Primavara/Vara/Toamna/Iarna): ");
        String inputSezon = scanner.nextLine();
        Sezon sezon = Sezon.fromString(inputSezon);

        List<Catalog> cataloageFiltrate = cataloage.stream()
                .filter(c -> c.getSezon() == sezon)
                .toList();

        if (cataloageFiltrate.isEmpty()) {
            System.out.println("Nu exista cataloage pentru sezonul ales.");
            return;
        }

        System.out.println("Cataloge disponibile:");
        for (int i = 0; i < cataloageFiltrate.size(); i++) {
            Catalog c = cataloageFiltrate.get(i);
            System.out.println((i + 1) + ". " + c.getTara().getNume() + " – " + c.getAn() + ", " + c.getSezon());
        }

        System.out.print("Alege catalogul (numar): ");
        int indexCatalog = Integer.parseInt(scanner.nextLine()) - 1;
        Catalog ales = cataloageFiltrate.get(indexCatalog);

        List<Pachet> pacheteDisponibile = ales.getPachete();
        System.out.println("Pachete disponibile:");
        for (int i = 0; i < pacheteDisponibile.size(); i++) {
            System.out.println((i + 1) + ". " + pacheteDisponibile.get(i).getTitlu() + " – " + pacheteDisponibile.get(i).getDurataZile() + " zile");
        }

        System.out.print("Alege pachetul dorit (numar): ");
        int indexPachet = Integer.parseInt(scanner.nextLine()) - 1;
        Pachet pachetAles = pacheteDisponibile.get(indexPachet);

        System.out.print("Nume client: ");
        String nume = scanner.nextLine();
        System.out.print("Prenume client: ");
        String prenume = scanner.nextLine();
        System.out.print("Email client: ");
        String telefon = scanner.nextLine();
        System.out.print("Nr tel client: ");
        String email = scanner.nextLine();
        Client client = new Client(nume, prenume, email, telefon);

        AgentVanzari agent = new AgentVanzari("Ion", "Marian", "agent@easytravel.com", "agent", 3500, 1.0);

        Rezervare r = new Rezervare(client, agent, pachetAles, LocalDate.now());
        r.efectueazaPlata(1, 1000, LocalDate.now());

        System.out.println("\n=== Rezervare inregistrata cu succes! ===");
        System.out.println(r);
        System.out.println("Plata: " + r.getPlata());
    }
}
