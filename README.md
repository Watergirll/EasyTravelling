# EasyTravelling
Java uni project that aims to show a good grasp of the oop concepts studied.

# 🧭 EasyTravelling – Etapa 1: Arhitectura Obiectuală & Simulare Rezervare CLI

## 🎯 Scopul etapei
Această etapă a avut ca scop modelarea unei agenții de turism folosind conceptele de programare orientată pe obiect (OOP) în Java. Aplicația simulează un scenariu real în care un client efectuează o rezervare turistică completă, interactiv, printr-un meniu de tip CLI (Command-Line Interface).

---

## 🧠 Ce am implementat până acum

### 🔹 Modelare OOP
- Am definit clasele de bază pornind de la structura logică a bazei de date: `Client`, `AgentVanzari`, `Ghid`, `Director`, `Catalog`, `Pachet`, `Transport`, `Tara`, `Plata`, `Rezervare`
- Am folosit concepte OOP:
  - **Moștenire** – `Angajat` este clasă abstractă extinsă de `Ghid`, `AgentVanzari`, `Director`
  - **Polimorfism** – `calculeazaSalariu()` este implementată diferit pentru fiecare subclasă
  - **Compoziție** – `Rezervare` gestionează intern `Plata`, `Cazare` gestionează `Camera`
  - **Agregare** – `Catalog` conține `Pachet`, `Pachet` aparține unui `Catalog`
  - **Singleton Pattern** – clasa `Director` permite o singură instanțiere controlată
  - **Enum-uri** – `Sezon` este definit ca `enum` și folosit în logica de selecție a cataloagelor

---

### 🔹 Testare funcțională
- Clasa `TestAngajat`:
  - Citește angajați din fișier (`input/angajati.txt`)
  - Creează instanțe din fișier folosind `switch` și `instanceof`
  - Afișează `toString()`, `hashCode()` și `calculeazaSalariu()` pentru fiecare obiect

---

### 🔹 Interfață CLI (meniu text în consola)
- Clasa `Main`:
  - Meniu interactiv:
    - `1. Testeaza angajati`
    - `2. Simuleaza rezervare`
    - `3. Iesire`
  - Opțiunile apelează metode din clasele `test` și `service`

---

### 🔹 Simulare rezervare (CLI)
- Utilizatorul:
  - Alege sezonul (`Sezon`)
  - Selectează un catalog + pachet turistic
  - Introduce date despre client
  - Se creează automat agentul și transportul
  - Se generează `Rezervare` și `Plata` asociată
- Rezultatul este afișat în consolă

---

## 📁 Exemplu de input: fisier 'angajati.txt'
`
5
agent,Popescu,Andrei,andrei@email.com,3000,12.0
ghid,Ionescu,Maria,maria@email.com,2800,1
agent,Stanescu,Cristian,cristian@email.com,3100,15.0
ghid,Georgescu,Elena,elena@email.com,2900,2
director,Vasilescu,Ioana,ioana@email.com,10000
`

---

## ✅ Tehnologii & concepte folosite

- ✅ Java 21
- ✅ Programare Orientată Obiect (OOP)
- ✅ Singleton Pattern (`Director`)
- ✅ Factory logic via `switch`
- ✅ Enum `Sezon`
- ✅ `Scanner`, `BufferedReader`, `File`
- ✅ CLI interactiv (meniu text)
- ✅ Separare pe pachete: `model`, `service`, `test`, `main`, `input`
- ✅ Testare bazată pe fișiere externe `.txt`

---

## 🚀 Urmează în etapa următoare

- Persistență a rezervărilor în colecții (Map, List)
- Listare / căutare rezervări
- Introducerea cazărilor, camerelor și traseelor per pachet
- Validare automată a disponibilității
- Scriere în fișiere CSV sau conectare la bază de date

---







