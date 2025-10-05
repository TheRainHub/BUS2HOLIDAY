# 🚍 Enterprise Bus Transportation Management System
*(Inspired by FlixBus)*

## 🎯 Téma práce
Cílem této semestrální práce je **návrh a implementace podnikového informačního systému** pro autobusovou dopravní společnost.
Projekt je zaměřen na tvorbu **vícevrstvé aplikace (enterprise-level)** s důrazem na:
- návrh architektury backend systému,
- použití moderních technologií,
- bezpečnost a testování pomocí unit testů a Postman kolekcí.

Aplikace bude implementována jako **čistě backendová REST API služba** bez grafického rozhraní.

---

## ⚙️ Popis systému
Systém simuluje funkce online platformy podobné FlixBus – bez fyzických poboček.
Uživatel zadává parametry cesty (odkud, kam, datum, počet osob) → systém zobrazí seznam dostupných jízd → uživatel si vybere spoj → zvolí sedadlo → provede platbu.

### Klíčové vlastnosti
- Vícevrstvá architektura (Controller – Service – Repository – Entity)
- Spring Boot REST API
- Spring Security + JWT autentizace
- Databáze: PostgreSQL
- Testování: JUnit, Mockito
- Dokumentace API: OpenAPI / Swagger
- CI/CD (volitelné: GitLab CI, Docker)

---

## 🧩 Hlavní funkce systému

### 👤 Správa uživatelů a rolí
- Registrace, přihlášení, JWT autentizace
- Autorizace podle role (Admin / User / Driver)
- Správa uživatelských dat

### 🚌 Správa autobusů a tras
- Evidence vozidel a jejich kapacit
- Přidělení řidičů k autobusům
- Definice tras, vzdáleností a zastávek

### 🕓 Plánování jízd
- Tvorba jednotlivých spojů (odjezd, příjezd, kapacita)
- Úprava a mazání spojů
- Zobrazení dostupnosti

### 🔍 Vyhledávání spojů
- Filtrování podle města, data, ceny, dostupnosti
- Přehled nejbližších spojů

### 🎟️ Rezervace a prodej jízdenek
- Výběr sedadla
- Nákup jízdenky a online platba
- Zrušení jízdenky do 15 minut před odjezdem

### 🚛 Řidičský modul
- Přehled přidělených spojů
- Seznam cestujících

### 🧾 Administrativní modul
- Přehled tržeb, obsazenosti a statistik
- Správa uživatelů, tras a autobusů

### 🔒 Bezpečnostní vrstva
- Spring Security + JWT
- Role-based access control (RBAC)

---

## 👥 Role v systému

| Role | Popis |
|------|--------|
| **Admin** | Má plný přístup. Spravuje uživatele, role, trasy, autobusy, řidiče a objednávky. |
| **User (Customer)** | Vyhledává jízdy, kupuje a ruší jízdenky, vybírá sedadla, sleduje své objednávky. |
| **Driver** | Vidí seznam jízd, které mu byly přiděleny, a seznam cestujících. |

---
