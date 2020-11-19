# musicpong
#### 3. Semester: Entwicklung interaktiver Benutzungsoberflächen
###### Projekt von Sandra Kiefer, Matteo Bentivegna und Niklas Schlögel

### Videodemo
[Link zur Videodemo](https://www.youtube.com/watch?v=_gIz0HEii-M)

### Hinweis

Da dieses Programm die Spotify API zur Musikanalyse benutzt, werden API Credentials benötigt, die kostenfrei beantragt werden können.

### Projektbeschreibung

musicpong ist eine eigene Umsetzung des Spieleklassikers "Pong" in JavaFX, jedoch unterstützt von und synchronisiert mit Musik.
Der Ball pulsiert im Takt und die gesamte GUI passt sich farblich dem Coverbild des aktuell gespielten Tracks an.

### Installationsanweisungen

Spotify Credentials:
* Spotify Developer Account erstellen unter https://developer.spotify.com/dashboard/
* Auf dem Dashboard unter "Create an App" eine neue App erstellen
* Client ID und Client Secret merken/kopieren (1)

Programm installieren:
* Projekt klonen
```
$ git clone https://github.com/niklasschloegel/musicpong.git
```
* In der Datei *musicpong/assets/config/config.properties* die Spotify API Credentials an der zugehörigen Stelle eingeben aus (1)
* In das Verzeichnis *musicpong/* zurück wechseln
* Ausführen mit
```
$ ./mvnw javafx:run
```
