# Mobilepay

## Introduktion

Dette projekt er vores 1. semester projekt, hvor vi har udviklet en MobilePay-applikation. Programmet simulerer
grundlæggende funktioner af MobilePay og er implementeret med en JavaFX-brugergrænseflade.

### Funktioner

##### Oprettelse af bruger:

Brugere oprettes i databasen med deres indtastede data.

Adgangskoder krypteres med bcrypt for øget sikkerhed.

##### Bruger login

Brugere kan logge ind ved at indtaste mobilnummer og adgangskode.

Et vellykket login sætter den aktuelle session til den pågældende bruger.

##### Log af

Brugere kan logge af, hvilket rydder sessionsdata og returnerer brugeren til startskærmen.

##### Overførsel af penge mellem brugere

Brugere kan overføre penge, forudsat at de har en konto med tilstrækkelig saldo.

Transaktioner trækker beløbet fra afsenderens konto og krediterer modtagerens konto.

##### Transaktionshistorik

Viser en oversigt over alle indgående og udgående transaktioner.

Listen opdateres automatisk efter hver transaktion.

##### Administration af bruger konti

Brugere kan tilføje konti. Den første oprettede konto bliver automatisk den primære.

Konti oprettes med en standardsaldo, som kan justeres i settings tabellen i databasen.

Brugere kan fjerne konti, hvilket sletter dem fra både brugerens konto Arraylist og databasen.

Brugere kan vælge en primær konto, der automatisk modtager penge.

### Teknologier

JavaFX

Java

CSS

SQLite

### Installation

Klon repository: `git clone https://github.com/RasmusKD/MobilepayProjekt.git`

### Overvejelse om refactoring

I den nuværende version af programmet er transaktioner tilknyttet direkte til brugere. En fremtidig refaktorering
planlægger at ændre dette, så transaktioner er tilknyttet specifikke konti. Dette vil give en mere detaljeret oversigt
og gøre det muligt at spore, hvilket konto nummer der har foretaget hver overførsel. Denne ændring vil forbedre
programmet ved at give brugere mere præcise transaktionshistorikker.