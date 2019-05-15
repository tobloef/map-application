# Changelog
Alle ændringer bliver dokumenteret i dette dokument sammen med de ugentlige releases.

## Færdig release
![udklip](https://github.itu.dk/storage/user/1986/files/4d4a4400-71db-11e9-9b64-ddfc651550d4)
Utallige ændringer.

## v0.9 - 28/4/2019
![image](https://github.itu.dk/storage/user/1986/files/6b10ef80-6a0a-11e9-979b-7d9d1b1e9467)

### Tilføjet
- Point of Interest som bruger kan tilføje til kortet.
- Printing af rutevejledning.

### Ændringer
- Optimerede dijkstra 
- Fixede rundkørelser. 
- Optimerede parsing memory footprint


### Bugs osv.
- Lidt utydeligt, da vi opdatere imens changelog bliver skrevet.

## v0.8 - 21/4/2019
v0.7 blev skippet, så dette er for de sidste 2 uger.
### Tilføjet
- Navigation Graph som gør det muligt at finde vej.
- Dijkstra
- Debugging view af Dijkstra. 
- Nearest Neighbor

### Ændringer
- Clamping af zoomniveauer.
- Building, Barriers og UNKNOWNS bliver ignoreret for at spare RAM. Det cuttede ram forbrug for tegning af hele danmark ned til 140 mb fra 400 mb. 
- Det sidste punkt bliver tegnet i polylines for at det ser mere korrekt ud når der er zoomet ud. 
- Der repaintes ikke 2 ganger under load af programmet.

### Bugs osv.
- Vejfinding giver ikke altid den korrekte længde
- Djikstra og generation af knude graf vil blive omskrevet til at være pænnere. 


## v0.6 - 07/4/2019
### Tilføjet
- Enable og disable all waytypes i waytype selector UI'en
- Relation merging således at alle relations bliver merged korrekt.

### Ændringer
- .obj er blevet ændret til .ser som er konventionen med serialiseret data
- ændret fra osmdrawing til danmarkskort package navn
- bedre zoom værdier.

### Bugs osv.
- \#101 og \#104 som var bugs der var en del af relation merging er blevet fixet
- \#106 er blevet opdaget i nodegrafen
- visse coastlines bliver ikke til øer, da de ikke har en relation.

## v0.5 - 31/3/2019
### Tilføjet
- Data om waytypes rykket fra Enum til .yaml filer, så der også anvendes temaer
- Toggle waytypes om de skal vises eller ej (ikke færdig, på seperat branch)
- Nodegraf er lavet så veje hænger sammen som data (der er stadig bugs, på seperat branch)

### Ændringer
- Level of detail værdier er blevet tweaked en smule
- Rykket information om tegning af waytypes ud i DrawingInfo

### Bugs osv.
- \#95 ny bug, temaer overrider ikke korrekt
- Meget arbejde på \#14 i den her uge, stadig ikke helt færdig

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
