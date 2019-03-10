# Changelog
Alle ændringer bliver dokumenteret i dette dokument sammen med de ugentlige releases.

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
