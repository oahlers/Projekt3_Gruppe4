Demo Applikation


Dette er en demo-applikation bygget med Java og Spring Framework, der bruger en MySQL-database. Denne README-fil indeholder instruktioner til at installere, starte og køre applikationen samt information om demobruger og deployment.
Deployment på Azure
Applikationen er udrullet på Microsoft Azure og kan tilgås via følgende link: https://baprivat-afg3g3g8bmf9avgh.germanywestcentral-01.azurewebsites.net/


For at teste applikationen kan du bruge følgende login-oplysninger:  

Bruger-ID: **1** 

Brugernavn: **demo**

Password: **demo**


Softwaremæssige forudsætninger
For at køre applikationen lokalt skal følgende software være installeret:  

Java: JDK 17 eller nyere  
Maven: Version 3.8.0 eller nyere (til build og dependency management)  
MySQL: Version 8.0 eller nyere (til databasen)  
Git: Til at klone repositoryet  
IDE: IntelliJ IDEA, Eclipse eller en anden IDE med understøttelse af Java og Spring  
Spring Boot: Version 3.0 eller nyere (håndteres via Maven)

Installationsvejledning for at køre programmet via. localhost:
**1. Klon repository**
   
Klon projektet fra GitHub:  
git clone https://github.com/oahlers/Projekt3_Gruppe4.git

**2. Konfigurer databasen**

Installer MySQL, hvis det ikke allerede er installeret.  
Opret en database i MySQL:  CREATE DATABASE bilabonnement;

Opdater databasekonfigurationen i src/main/resources/application.properties: 

spring.datasource.url=jdbc:mysql://localhost:3306/demo_app

spring.datasource.url=${JDBC_DATABASE_URL}

spring.datasource.username=${JDBC_USERNAME}

spring.datasource.password=${JDBC_PASSWORD}

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.datasource.hikari.connection-timeout=10000

spring.datasource.hikari.initialization-fail-timeout=-1



**3. Kør applikationen**
Når applikationen er startet, kan du tilgå den via:http://localhost:8080


Log ind med demobruger-oplysningerne:  

Bruger-ID: 1

Brugernavn: demo

Password: demo

Yderligere noter:


Testfilerne ligger inde test/java/com.example.gruppe4_projekt3_service
For at køre programmet via IntelliJ IDEA, så startes programmet via: Gruppe4Projekt3Application.
Sørg for, at port 8080 er ledig, da dette er standardporten for Spring Boot.  
Hvis du støder på problemer med databasen, tjek, at MySQL kører, og at konfigurationen i application.properties er korrekt.  
