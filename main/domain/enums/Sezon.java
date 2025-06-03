package main.domain.enums;

public enum Sezon {
    PRIMAVARA("Primavara", "Mar-Mai", "🌸"),
    VARA("Vara", "Iun-Aug", "☀️"),
    TOAMNA("Toamna", "Sep-Nov", "🍂"),
    IARNA("Iarna", "Dec-Feb", "❄️"),
    TOT_ANUL("Tot anul", "An intreg", "🌍");

    private final String nume;
    private final String perioada;
    private final String icon;

    Sezon(String nume, String perioada, String icon) {
        this.nume = nume;
        this.perioada = perioada;
        this.icon = icon;
    }

    public String getNume() {
        return nume;
    }

    public String getPerioada() {
        return perioada;
    }

    public String getIcon() {
        return icon;
    }

    /**
     * Metoda pentru a obtine enum-ul din string (pentru compatibilitate cu RezervareService)
     */
    public static Sezon fromString(String sezonString) {
        if (sezonString == null || sezonString.trim().isEmpty()) {
            return null;
        }
        
        String input = sezonString.trim().toLowerCase();
        
        return switch (input) {
            case "primavara", "primăvară", "spring" -> PRIMAVARA;
            case "vara", "vară", "summer" -> VARA;
            case "toamna", "toamnă", "autumn", "fall" -> TOAMNA;
            case "iarna", "iarnă", "winter" -> IARNA;
            case "tot anul", "tot_anul", "all year", "year round" -> TOT_ANUL;
            default -> {
                // Incearca match prin nume
                for (Sezon s : values()) {
                    if (s.nume.toLowerCase().equals(input)) {
                        yield s;
                    }
                }
                yield VARA; // default fallback
            }
        };
    }

    @Override
    public String toString() {
        return icon + " " + nume + " (" + perioada + ")";
    }
}

