# Changelog
Alle ændringer bliver dokumenteret i dette dokument sammen med de ugentlige releases.

## v0.4 - 24/3/2019
### Tilføjet
- KD-Træ mergede ind i.
- Forskellige tykkelser på veje
- Level of Detail drawing tilføjet. Værdierne er dog lidt optimistike lige nu.

### Ændringer
- Farver er ved at blive rykket ud i en seperat config fil

### Bugs osv.
- \#14 er delvis fixet, men kræver stadig arbejde
- Fixede bug med langsom drawing der stammede fra settings der ikke blev slettede.

## v0.3 - 17/3/2019
### Tilføjet
- Flere kommentarer i koden overalt
- Kan anvende textures som fill til waytypes
- Alerts under menuen Program-> Help & About
- KD-træ (ikke færdigt, ligger i seperat branch, punkt-baseret og ikke way-baseret lige nu)

### Ændringer
- Refaktoriseing fra projekt-start er færdig

### Bugs osv.
- \#56 og #14 er stadig ikke fixet

## v0.2 - 10/3/2019
### Tilføjet
- Menubar igennem FXML, til senere brug
- Kan bygge til jar igennem bat fil (ikke optimalt workflow lige pt. fungerer uden gradle men med installation af javaFX)
- Zoom indikator i applikationsvinduet (indikerer ikke rigtige enheder)
- Mapper til de forskellige MVC dele af programmet

### Ændringer
- Separeret en masse klasser for clean code
- Waytypes.txt i alfabetisk rækkefølge samt flere waytypes
- Refactoring af mange variabel navne, metodenavne og klassenavne, samt generel syntax-styling over alt
- Resourcer bliver loadet differentieret ud fra om det er gennem gradle eller jar

### Bugs osv.
- Relation tegne bug > issue #14
- Titel tekst encoding i jar er forkert > issue #56

## v0.1 - 3/3/2019
### Tilføjet
- Gruppekontrakt (Ligger i Github Wiki)
- OSMDrawing
- WayTypeFactory: en nemmere måde at definere hvad type en bestemt key-value pair hører til.

### Ændringer
- Refaktorisering af model parsing
- Ændret farverne

### Bugs osv.
