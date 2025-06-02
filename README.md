# EasyTravelling - Aplicație de Management Agenție de Turism

## Descriere Generală
Aplicația EasyTravelling este un sistem complet de management pentru o agenție de turism, implementat în Java 21 cu arhitectură MVC în straturi și persistență în baza de date Oracle.

## Etapa I - Definirea și Implementarea Sistemului

### ✅ Cerința 1: Lista cu cel puțin 10 acțiuni/interogări

**Implementare:** `main/view/Main.java` - metoda `afiseazaMeniuPrincipal()`

**Cele 10 acțiuni implementate:**
1. **Testează angajați** - Test pentru funcționalitatea angajaților
2. **Testează creearea unei rezervări** - Simularea unei rezervări  
3. **Autentificare / Cont client** - Sistem complet de login/register
4. **Căutare locații turistice** - Browse și search pentru destinații
5. **Căutare servicii disponibile** - Browse și rezervare servicii
6. **Management rezervări (Admin)** - Administrare rezervări pentru admin
7. **Rapoarte și statistici** - Generare rapoarte detaliate și backup
8. **Management clienți (Admin)** - CRUD operații pentru clienți
9. **Management angajați (Admin)** - CRUD operații pentru angajați
10. **Ieșire** - Exit din aplicație

### ✅ Cerința 2: Lista cu cel puțin 8 tipuri de obiecte

**Implementare:** Package `main/domain/`

**Cele 8+ tipuri de obiecte:**
1. **`Client.java`** - Clienți ai agenției
2. **`Angajat.java`** - Clasa abstractă pentru angajați  
3. **`Ghid.java`** - Ghizi turistici (extends Angajat)
4. **`AgentVanzari.java`** - Agenți de vânzări (extends Angajat)
5. **`Director.java`** - Directori (extends Angajat)
6. **`Locatie.java`** - Locații turistice
7. **`Tara.java`** - Țări de destinație
8. **`Rezervare.java`** - Rezervări ale clienților
9. **`Serviciu.java`** - Servicii oferite (transport, cazare, etc.)
10. **`JobType` (enum)** - Tipuri de joburi pentru angajați

### ✅ Cerința 3: Clase simple cu atribute private/protected și metode de acces

**Implementare:** Toate clasele din `main/domain/`

**Exemple:**
- **`Client.java`**: Atribute private (`nume`, `prenume`, `email`, `telefon`) cu getteri/setteri
- **`Angajat.java`**: Atribute protected (`nume`, `prenume`, `salariuBaza`) pentru moștenire
- **`Ghid.java`**: Atribute private (`idLocatie`, `limbiVorbite`) cu metode de acces
- **`Rezervare.java`**: Encapsulare completă cu validări în setteri

### ✅ Cerința 4: Cel puțin 2 colecții diferite, una sortată

**Implementare:**

1. **List (ArrayList)** - în multiple locuri:
   - `Client.java`: `List<Rezervare> rezervari`
   - `LocatieService.java`: `List<Locatie> locatii`
   - `ServiciuService.java`: `List<Serviciu> servicii`

2. **Set (TreeSet) - SORTATĂ**:
   - `Ghid.java`: `Set<String> limbiVorbite = new TreeSet<>()` - Colecție sortată automat alfabetic

3. **Map (folosit în servicii)**:
   - În servicii pentru căutări rapide și statistici

### ✅ Cerința 5: Utilizare moștenire pentru clase adiționale

**Implementare:** `main/domain/`

**Ierarhia de moștenire:**
```
Angajat (abstract)
├── Ghid.java
├── AgentVanzari.java  
└── Director.java
```

**Polimorfism implementat:**
- Metoda abstractă `calculSalariu()` implementată diferit în fiecare subclasă
- Metoda `calculSalariuCuBonus()` cu comportament diferit
- Folosirea polimorfismului în colecții: `List<Angajat>` conține instanțe de toate tipurile

### ✅ Cerința 6: Cel puțin o clasă serviciu

**Implementare:** Package `main/service/`

**Clasele serviciu implementate:**
1. **`UserService.java`** - Servicii pentru autentificare și management utilizatori
2. **`LocatieService.java`** - Logica de business pentru locații
3. **`ServiciuService.java`** - Management servicii turistice
4. **`AdminService.java`** - Operații administrative CRUD
5. **`ReportService.java`** - Generare rapoarte și statistici
6. **`RezervareService.java`** - Logica pentru rezervări

### ✅ Cerința 7: Clasa Main cu apeluri către servicii

**Implementare:** `main/view/Main.java`

**Apeluri către servicii:**
- `TestAngajat.ruleazaTestAngajati()` 
- `RezervareService service = new RezervareService(); service.simuleazaRezervare()`
- `authController.handleAuthMenu()` (care folosește UserService)
- `locatieController.afiseazaLocatiiDisponibile()` (care folosește LocatieService)
- Și toate celelalte acțiuni prin controller-e care apelează servicii

## Etapa II - Persistența cu Baza de Date

### ✅ Cerința 1: Persistența cu baza de date relațională și JDBC

**Implementare:** Package `main/persistence/`

**Componente implementate:**
1. **`DBConn.java`** - Conexiune singleton la baza de date Oracle
2. **`GenericRepository.java`** - Interface generică pentru operații CRUD
3. **`ClientRepository.java`** - Repository specific pentru clienți
4. **`AngajatRepository.java`** - Repository specific pentru angajați

**Configurare baza de date:**
- Driver Oracle: `ojdbc17.jar`
- Conexiune la schema `AGENTIE_TURISM`
- Folosește secvențe Oracle pentru auto-increment ID-uri

### ✅ Cerința 2: Servicii CRUD pentru cel puțin 4 clase

**Implementare:** 

1. **CLIENT** - `ClientRepository.java`:
   - **Create**: `save(Client client)`
   - **Read**: `findAll()`, `findById(String email)`
   - **Update**: `update(Client client)`
   - **Delete**: `delete(Client client)`

2. **ANGAJAT** - `AngajatRepository.java`:
   - **Create**: `save(Angajat angajat)`
   - **Read**: `findAll()`, `findById(String email)`, `findByType(String tip)`
   - **Update**: `update(Angajat angajat)`
   - **Delete**: `delete(Angajat angajat)`

3. **SERVICIU** - prin `ServiciuService.java`:
   - **Create**: `initializeazaServicii()` 
   - **Read**: `getToateServiciile()`, `getServiciuById()`
   - **Update**: `rezervaServiciu()` (actualizează locuri disponibile)
   - **Delete**: Implementat prin setarea disponibilității

4. **LOCATIE** - prin `LocatieService.java`:
   - **Create**: `initializeazaLocatii()`
   - **Read**: `getToateLocatiile()`, `getLocatieById()`, `cautaDupaTara()`
   - **Update**: Prin servicii
   - **Delete**: Suport pentru eliminare

### ✅ Cerința 3: Servicii singleton generice

**Implementare:**

1. **`DBConn.java`** - **Singleton Pattern**:
```java
public class DBConn {
    private static DBConn instance;
    private static Connection connection;
    
    public static Connection getConnectionFromInstance() {
        if (instance == null) {
            instance = new DBConn();
        }
        return connection;
    }
}
```

2. **`GenericRepository<T>`** - **Generic Interface**:
```java
public interface GenericRepository<T> {
    T save(T entity);
    List<T> findAll();
    Optional<T> findById(String id);
    void update(T entity);
    void delete(T entity);
}
```

## Arhitectura Aplicației

### Separarea în straturi (MVC):

1. **Model (Domain Layer)**: `main/domain/` - Entitățile de business
2. **View (Presentation Layer)**: `main/view/Main.java` - Interfața utilizator
3. **Controller Layer**: `main/controller/` - Mediaza între view și servicii
4. **Service Layer**: `main/service/` - Logica de business
5. **Persistence Layer**: `main/persistence/` - Accesul la date

## Testare și Funcționalitate

**Aplicația este complet funcțională:**
- ✅ Se compilează fără erori
- ✅ Se conectează la baza de date Oracle
- ✅ Toate cele 10 acțiuni sunt implementate și funcționale
- ✅ Arhitectura MVC este respectată strict
- ✅ Toate cerințele sunt îndeplinite

## Tehnologii Utilizate

- **Java 21** (conform cerințelor)
- **Oracle Database** cu JDBC
- **Design Patterns**: Singleton, Repository, MVC
- **Collections Framework**: List, Set (TreeSet), Map
- **Inheritance & Polymorphism**: Implementat complet

---

**Concluzie**: Proiectul EasyTravelling implementează integral toate cerințele din ambele etape, oferind o aplicație robustă și bine structurată pentru managementul unei agenții de turism.
