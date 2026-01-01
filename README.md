# Meldecenter

Dies ist eine Coding-Challenge-Anwendung, die mit Spring Boot 3 und PostgreSQL entwickelt wurde.

![Spring Boot](base-spring-boot.png)

## Voraussetzungen

### JDK Installation
Es wird empfohlen, eine JDK 21 über [SDKMAN!](https://sdkman.io/) zu installieren, z.B.:

```bash
sdk install java 21.0.9-zulu
```
*(Verfügbare Versionen von JDK 21 können über `sdk list java` abgerufen werden.)*

Oder `sdk env install` ausführen, dabei wird die JDK von [.sdkmanrc](.sdkmanrc) installiert:

```bash
sdk env install
```

### Docker Installation
Es wird empfohlen, [OrbStack](https://orbstack.dev/) oder [Docker Desktop](https://www.docker.com/products/docker-desktop) zu installieren.

`docker` und `docker compose` Befehle werden benötigt.

## Anwendung ausführen

Es gibt zwei Möglichkeiten, die Anwendung zu starten:

### 1. Docker-Modus
```bash
./gradlew composeUp
```
Dieser Befehl baut das Boot-Jar-Archiv und startet die gesamte Umgebung (Anwendung, Datenbank, FTP-Server) mithilfe von Docker Compose im Hintergrund (`-d`). Die Anwendung läuft dann in einem Container.

### 2. Entwicklungs-Modus
```bash
./gradlew bootRun
```
Dies startet die Anwendung lokal über Gradle. Dank der Spring Boot Docker Compose Integration werden die notwendigen Infrastruktur-Dienste (PostgreSQL, FTP) automatisch aus der `compose.yaml` gestartet, während die Anwendung selbst nativ auf Ihrem Host-System läuft. Dies ist ideal für die Entwicklung und das Debugging.

## Port-Belegung an localhost

| Anwendung / Dienst | Port (Localhost) | Port (Container) | Protokoll / Zweck |
| :--- |:-----------------| :--- | :--- |
| **Meldecenter** | `8080`           | `8080` | REST API |
| **PostgreSQL** | `5432`           | `5432` | Datenbank (R2DBC/JDBC) |
| **FTP Server** | `21`             | `21` | FTP Control Port |
| **FTP Server** | `21000`          | `21000` | FTP Passive Data Port |
| **Jaeger UI** | `16686`          | `16686` | Web-Oberfläche (Tracing) |
| **Jaeger OTLP** | `4317`           | `4317` | Tracing via gRPC |
| **Jaeger OTLP** | `4318`           | `4318` | Tracing via HTTP |


## Tests ausführen

Um die Unit- und Integrationstests auszuführen, verwenden Sie:

```bash
./gradlew test
```

**Wichtig:** Um die Tests lokal erfolgreich auszuführen – auch wenn der FTP-Container bereits durch `./gradlew composeUp` oder `./gradlew bootRun` aktiv ist –, wird dieser von Testcontainers wiederverwendet statt neu gestartet. Dies ist eine Einschränkung des FTP-Images: Port `21000` muss verfügbar sein und kann nicht geändert werden.

Manchmal kann es vorkommen, dass der FTP-Container beim Starten fehlschlägt, scheint ein Problem mit dem Image zu sein.

## End-to-End Tests

### Swagger UI
Die interaktive API-Dokumentation (Swagger UI) ist unter folgendem Link verfügbar, sobald die Anwendung läuft:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Basis E2E-Test-Skript ausführen
Ein einfaches Bash-Skript steht zur Verfügung, um den Workflow der Anwendung zu testen (Meldungen senden und Export auslösen):

1. Stellen Sie sicher, dass die Anwendung gestartet wurde.
2. Machen Sie das Skript ausführbar:
   ```bash
   chmod +x simple-e2e-test.sh
   ```
3. Führen Sie das Skript aus:
   ```bash
   ./simple-e2e-test.sh
   ```

### Speicherort der generierten XML-Dateien
Die vom System generierten XML-Dateien werden auf den FTP-Server hochgeladen. Im lokalen Dateisystem sind diese (bedingt durch das Docker-Volume-Mapping) hier zu finden:
`src/test/resources/ausgehend/ftp`

Die Struktur der generierten XML-Dateien orientiert sich an den Referenzdateien im Verzeichnis `src/test/resources/eingehend/ftp/inbox`.

**Hinweis: Die automatisierte Verarbeitung der XML-Dateien durch den fiktiven FTP-Server sowie die damit verbundene Erstellung von Antwortdateien in den Verzeichnissen `error` und `outbox` wurden im Rahmen dieser Implementierung nicht berücksichtigt.**

## Weitere Dokumentation

- [Details zum Test XML-Mapping](src/test/resources/README.md)
