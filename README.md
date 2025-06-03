# EasyTravelling - Sistem de Management pentru Agenție de Turism

## Descrierea Proiectului

EasyTravelling este o aplicație Java pentru gestionarea unei agenții de turism, implementând un sistem complet de management pentru clienți, angajați, rezervări, oferte turistice și plăți.

---

## ETAPA I - Implementarea Detaliată

### 1. ✅ Lista cu 10+ Acțiuni/Interogări

**Fișier:** `main/service/AuditService.java`  
**22 DE ACȚIUNI IMPLEMENTATE:**

#### Acțiuni principale (10):
1. `TEST_ANGAJATI` – Test funcționalități angajați  
2. `AUTENTIFICARE` – Autentificare universală  
3. `CAUTARE_LOCATII` – Căutare locații turistice  
4. `CAUTARE_SERVICII` – Căutare servicii disponibile  
5. `MANAGEMENT_REZERVARI` – Management rezervări  
6. `RAPOARTE_STATISTICI` – Generare rapoarte  
7. `MANAGEMENT_CLIENTI` – Management clienți  
8. `MANAGEMENT_ANGAJATI` – Management angajați  
9. `TEST_REZERVARE` – Test rezervări  
10. `IESIRE` – Ieșire aplicație  

#### Acțiuni secundare (12):
11. `LOGIN_CLIENT`  
12. `LOGIN_ANGAJAT`  
13. `SIGNUP_CLIENT`  
14. `SIGNUP_ANGAJAT`  
15. `VIEW_PROFILE`  
16. `VIEW_CATALOG`  
17. `CREATE_RESERVATION`  
18. `CREATE_CLIENT/UPDATE_CLIENT/DELETE_CLIENT`  
19. `CREATE_ANGAJAT/UPDATE_ANGAJAT/DELETE_ANGAJAT`  
20. `GENERATE_REPORT`  
21. `BACKUP_DATA`  
22. `LOGOUT`  

---

### 2. ✅ Lista cu 8+ Tipuri de Obiecte

**Fișiere:** `main/domain/`  
**12 TIPURI DE OBIECTE IMPLEMENTATE:**

1. `Client.java`  
2. `Angajat.java` (abstract)  
3. `Ghid.java`  
4. `AgentVanzari.java`  
5. `Director.java` (Singleton)  
6. `Rezervare.java`  
7. `Oferta.java`  
8. `Locatie.java`  
9. `Pachet.java`  
10. `Plata.java`  
11. `Serviciu.java`  
12. `Tara.java`  

---

### 3. ✅ Clase Simple cu Atribute Private/Protected și Metode de Acces

**Exemplu:** `main/domain/Client.java`  
- Atribute private (`nume`, `email`, etc.)  
- Metode `get` / `set` publice

---

### 4. ✅ Minimum 2 Colecții Diferite (una sortată)

- `TreeSet` în `Ghid.java` – limbi vorbite (sortat automat)  
- `ArrayList` în `Client.java` – lista rezervărilor  
- `HashMap` în `UserService.java` – mapare email-parolă  

---

### 5. ✅ Moștenire

**Fișiere:** `Angajat.java`, `Ghid.java`, `AgentVanzari.java`, `Director.java`  
- `Angajat` – abstract, cu `calculeazaSalariu()`  
- `Ghid`, `AgentVanzari`, `Director` – moștenire + implementare proprie

---

### 6. ✅ Minimum o Clasă Serviciu

**Fișiere:** `main/service/`  
**9 CLASE SERVICIU IMPLEMENTATE:**

1. `UserService.java`  
2. `AdminService.java`  
3. `ReportService.java`  
4. `AuditService.java` (Singleton)  
5. `CatalogService.java`  
6. `PlataService.java`  
7. `LimbiService.java`  
8. `LocatieService.java`  
9. `ServiciuService.java`  

---

### 7. ✅ Clasa Main

**Fișier:** `main/view/Main.java`  
- Integrare cu toate serviciile  
- Meniu CLI complet  
- Arhitectură MVC respectată

---

## ETAPA II - Implementarea Detaliată

### 1. ✅ Persistența cu JDBC și Oracle

**Fișiere:**  
- `main/config/DatabaseConfig.java`  
- `main/persistence/ClientRepository.java`  
- `main/persistence/AngajatRepository.java`  
- `main/persistence/LimbiVorbiteRepository.java`

**Conexiune JDBC:**  
- Implementată prin `DBConn.java` (Singleton)

---

### 2. ✅ CRUD pentru 4+ Clase

**Exemplu:**  
- `ClientRepository` – `save`, `findAll`, `findById`, `update`, `delete`

---

### 3. ✅ Servicii Singleton Generice

- `AuditService` – Singleton thread-safe  
- `DBConn` – Singleton pentru conexiuni Oracle

---

### 4. ✅ Serviciul de Audit CSV

**Fișier generat:** `data/audit.csv`  
**Format:** `actiune,timestamp`


**Funcționalități:**
- Director/fișier create automat
- Logging automat în acțiuni importante
- Rapoarte și statistici pe baza fișierului

---

## ✅ REZUMAT GENERAL

### ETAPA I:
- ✅ 22 acțiuni (din min. 10)
- ✅ 12 tipuri de obiecte (din min. 8)
- ✅ Encapsulare corectă
- ✅ 3 colecții diferite (inclusiv sortată)
- ✅ Moștenire + abstractizare
- ✅ 9 clase serviciu
- ✅ Meniu CLI complet în `Main`

### ETAPA II:
- ✅ Conexiune Oracle via JDBC
- ✅ CRUD complet pentru cel puțin 4 clase
- ✅ Singleton pentru servicii centrale
- ✅ Audit CSV funcțional + structurat

---
