# EasyTravelling - Sistem de Management pentru Agenție de Turism

## Descrierea Proiectului

EasyTravelling este o aplicație Java pentru gestionarea unei agenții de turism, implementând un sistem complet de management pentru clienți, angajați, rezervări, oferte turistice și plăți.

---

## ETAPA I - Definirea și Implementarea Sistemului

### 1. Lista Acțiunilor/Interogărilor (Cerința: minimum 10)

Sistemul implementează **24 de acțiuni distincte**, depășind cu mult cerința minimă:

#### Acțiuni Principale (10):
1. **Autentificare Client/Angajat** - Login universal în sistem
2. **Cautare Locatii Turistice** - Filtrare și căutare destinații
3. **Cautare Servicii Disponibile** - Explorare servicii turistice
4. **Management Rezervari Admin** - Administrare rezervări
5. **Rapoarte si Statistici** - Generare rapoarte diverse
6. **Management Clienti Admin** - CRUD operații pentru clienți
7. **Management Angajati Admin** - CRUD operații pentru angajați
8. **Test Angajati** - Funcționalități demo pentru testare
9. **Test Creare Rezervare** - Demo pentru sistem rezervări
10. **Iesire din Aplicatie** - Logout și închidere sistem

#### Acțiuni Secundare/Sub-acțiuni (14):
11. **Login Client** - Autentificare specifică clienți
12. **Login Angajat** - Autentificare specifică angajați
13. **Signup Client** - Înregistrare clienți noi
14. **Signup Angajat** - Înregistrare angajați noi
15. **Logout** - Deconectare din sistem
16. **View Profile** - Vizualizare profil utilizator
17. **Create Client** - Crearea unui client nou
18. **Update Client** - Actualizarea datelor client
19. **Delete Client** - Ștergerea unui client
20. **Create Angajat** - Crearea unui angajat nou
21. **Update Angajat** - Actualizarea datelor angajat
22. **Delete Angajat** - Ștergerea unui angajat
23. **Generate Report** - Generare rapoarte specifice
24. **Backup Data** - Backup baza de date

*Implementare*: Toate acțiunile sunt definite în `main.service.AuditService.Actions` și sunt loggate automat în fișierul CSV de audit.

### 2. Lista Tipurilor de Obiecte (Cerința: minimum 8)

Sistemul implementează **16 tipuri de obiecte**, depășind cerința:

#### Obiecte Principale (Domain Classes):
1. **Client** - Clienții agenției de turism
2. **Angajat** - Clasa părinte pentru angajați (moștenire)
3. **Ghid** - Ghizi turistici (extinde Angajat)
4. **AgentVanzari** - Agenți de vânzări (extinde Angajat)
5. **Director** - Directorul agenției (extinde Angajat)
6. **Rezervare** - Rezervările clienților
7. **Pachet** - Pachete turistice
8. **Locatie** - Destinații turistice

#### Obiecte Secundare:
9. **Tara** - Țări pentru destinații
10. **Cazare** - Tipuri de cazare
11. **Camera** - Camere de hotel
12. **Transport** - Mijloace de transport
13. **FirmaTransport** - Companii de transport
14. **Serviciu** - Servicii turistice oferite
15. **Ruta** - Rute de călătorie
16. **Oferta** - Oferte speciale (nou adăugat pentru catalog)
17. **Plata** - Procesarea plăților (nou adăugat)
18. **LimbaVorbita** - Limbile vorbite de ghizi (nou adăugat)

*Locație*: Toate clasele domain se află în `main.domain/` și `main.domain.enums/`

### 3. Clase Simple cu Atribute Private/Protected și Metode de Acces

✅ **Implementat complet** - Toate clasele domain respectă principiile OOP:

```java
// Exemplu din Client.java
public class Client {
    private String nume;
    private String prenume;
    private String email;
    private String telefon;
    
    // Constructori
    public Client(String nume, String prenume, String email, String telefon) { ... }
    
    // Getters și Setters
    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    // ... etc
}
```

### 4. Colecții Diferite (Cerința: minimum 2, una sortată)

✅ **Implementat - 4 tipuri de colecții**:

#### a) TreeSet (Sortată - CERINȚA SPECIALĂ)
```java
// În Ghid.java - TreeSet pentru limbile vorbite (sortare automată)
private Set<String> limbiVorbite = new TreeSet<>();
```
**Avantaj**: Sortare automată alfabetică a limbilor vorbite de ghizi

#### b) ArrayList
```java
// În CatalogService.java
private List<Oferta> oferte = new ArrayList<>();

// În Client.java
private List<Rezervare> rezervari = new ArrayList<>();
```

#### c) HashMap
```java
// În AuditService.java pentru statistici
Map<String, Integer> actionCounts = new HashMap<>();
```

#### d) LinkedList (în servicii)
```java
// Pentru operații frecvente de inserare/ștergere
```

*Locația principală*: `main.domain.Ghid.java` (TreeSet), `main.service.*` (List, Map)

### 5. Moștenire pentru Clase Adiționale

✅ **Implementat - Ierarhie completă de angajați**:

```
Angajat (clasă părinte)
├── Ghid extends Angajat
├── AgentVanzari extends Angajat
└── Director extends Angajat
```

**Implementare**:
- `main.domain.Angajat` - clasa de bază
- `main.domain.Ghid` - ghizi cu limbi vorbite și locații
- `main.domain.AgentVanzari` - agenți cu sistem de comisioane
- `main.domain.Director` - directori cu privilegii administrative

**Polimorfism**: Metoda `calculSalariu()` este overridden în fiecare subclasă pentru logică specifică.

### 6. Clase Serviciu

✅ **Implementat - 9 servicii specializate**:

1. **UserService** - Management utilizatori și autentificare
2. **CatalogService** - Gestionarea ofertelor turistice
3. **PlataService** - Procesarea plăților
4. **AuditService** - Logging acțiuni în CSV
5. **AdminService** - Operații administrative
6. **ReportService** - Generare rapoarte
7. **LimbiService** - Management limbi vorbite
8. **LocatieService** - Gestionarea locațiilor
9. **ServiciuService** - Management servicii turistice

*Locație*: `main.service/`

### 7. Clasa Main

✅ **Implementat**: `main.view.Main.java`

**Arhitectură MVC implementată**:
- **Model**: `main.domain.*`
- **View**: `main.view.*` (AuthView, ClientView, DirectorView, AngajatView)
- **Controller**: `main.controller.*`

---

## ETAPA II - Persistența și Baza de Date

### 1. Persistența cu JDBC

✅ **Implementat complet**:

**Conexiune BD**: `main.persistence.DBConn.java`
```java
// Singleton pattern pentru conexiunea la Oracle DB
public class DBConn {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    // ...
}
```

### 2. Operații CRUD pentru minimum 4 Clase

✅ **Implementat pentru 5 clase**:

#### a) **ClientRepository** - CRUD complet
- Create: `save(Client client)`
- Read: `loadAll()`, `findByEmail()`
- Update: `update(Client client)`
- Delete: `delete(int id)`

#### b) **AngajatRepository** - CRUD complet
- Suportă toate tipurile: Ghid, AgentVanzari, Director
- Operații specializate per tip de angajat

#### c) **LimbiVorbiteRepository** - CRUD complet
- Gestionează limbile vorbite de ghizi
- Sincronizare TreeSet cu BD

#### d) **Generic Repository Pattern**
- `main.persistence.GenericRepository` - interfață generică
- Template pentru operații CRUD standard

#### e) **Operații suplimentare**:
- Rezervări, Locații, Servicii prin serviciile respective

*Locație*: `main.persistence/`

### 3. Servicii Singleton

✅ **Implementat**:

#### UserService - Singleton Principal
```java
public class UserService {
    private static UserService instance;
    
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
}
```

#### AuditService - Singleton pentru Audit
```java
public class AuditService {
    private static AuditService instance;
    
    public static AuditService getInstance() { ... }
}
```

### 4. Serviciu de Audit cu CSV

✅ **Implementat complet**: `main.service.AuditService`

**Funcționalități**:
- ✅ Scriere automată în CSV la fiecare acțiune
- ✅ Format: `nume_actiune,timestamp`
- ✅ Fișier: `data/audit.csv`
- ✅ Statistici și rapoarte audit
- ✅ Singleton pattern

**Exemplu ieșire CSV**:
```
Login Client,2024-01-15 14:30:25
Cautare Locatii Turistice,2024-01-15 14:31:10
Management Rezervari Admin,2024-01-15 14:32:45
```

---

## Arhitectura Aplicației

### Design Patterns Utilizate

1. **Singleton**: UserService, AuditService
2. **MVC**: Separarea responsabilităților
3. **Repository**: Pentru persistența datelor
4. **Factory**: Pentru crearea obiectelor în funcție de tip

### Structura Proiectului

```
EasyTravelling/
├── main/
│   ├── domain/           # Clasele de domeniu (16 tipuri)
│   │   ├── enums/        # Enumerări (JobType, Sezon)
│   │   └── *.java        # Client, Angajat, Ghid, etc.
│   ├── persistence/      # Layer-ul de persistență
│   │   ├── DBConn.java   # Conexiunea la BD
│   │   └── *Repository.java # Repository-uri CRUD
│   ├── service/          # Layer-ul de servicii (9 servicii)
│   │   └── *.java        # UserService, AuditService, etc.
│   ├── controller/       # Controllere MVC
│   │   └── *.java        # Business logic
│   └── view/             # Layer-ul de prezentare
│       ├── Main.java     # Punctul de intrare
│       └── *View.java    # UI pentru fiecare rol
├── data/
│   └── audit.csv         # Fișierul de audit
├── lib/                  # JDBC drivers
└── README.md            # Această documentație
```

---

## Funcționalități Principale

### Pentru Clienți
- Autentificare și înregistrare
- Căutare oferte și destinații
- Realizarea rezervărilor
- Vizualizarea istoricului

### Pentru Ghizi
- **TreeSet sortarea limbilor vorbite** (cerința colecție sortată)
- Management locații de lucru
- Calcularea salariului cu bonus pe limbi
- Sincronizarea limbilor cu BD

### Pentru Agenți de Vânzări
- Sistema de comisioane
- Statistici vânzări
- Management clienți

### Pentru Director (Admin)
- CRUD complet pentru clienți și angajați
- Rapoarte și statistici
- Management general al sistemului

---

## Demonstrarea Cerințelor

### ✅ Etapa I - Toate cerințele îndeplinite:
- [x] 10+ acțiuni (implementate 24)
- [x] 8+ tipuri obiecte (implementate 18)
- [x] Clase cu atribute private și metode acces
- [x] 2+ colecții diferite, una sortată (TreeSet, ArrayList, HashMap)
- [x] Moștenire (Angajat → Ghid/Agent/Director)
- [x] Servicii pentru operații sistem
- [x] Clasa Main cu apeluri către servicii

### ✅ Etapa II - Toate cerințele îndeplinite:
- [x] Persistența cu JDBC și Oracle DB
- [x] CRUD pentru 4+ clase (implementat pentru 5+)
- [x] Servicii singleton (UserService, AuditService)
- [x] Serviciu audit cu CSV (format: nume_actiune,timestamp)

---

## Rularea Aplicației

```bash
# Compilare
javac -cp "lib/*" main/view/Main.java main/domain/*.java main/domain/enums/*.java main/persistence/*.java main/service/*.java main/controller/*.java main/view/*.java

# Rulare
java -cp ".:lib/*" main.view.Main
```

---

## Concluzie

Proiectul EasyTravelling demonstrează o implementare completă și avansată a cerințelor, cu:
- **Depășirea cerințelor minimale** (24 acțiuni vs 10, 18 obiecte vs 8)
- **Arhitectură MVC robustă** cu separarea responsabilităților
- **Persistența completă** cu JDBC și baza de date Oracle
- **Design patterns moderne** (Singleton, Repository, MVC)
- **Structuri de date sortate** (TreeSet pentru limbile ghizilor)
- **Audit complet** pentru toate acțiunile sistemului

Aplicația este gata pentru producție și poate gestiona eficient o agenție de turism reală.
