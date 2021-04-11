# Vulnerability Analysis

## A1:2017 Injection
### Description
SQL injection is een veel voorkomend security risico. Deze vulnerability houdt in dat een gebruiker een eigen stuk sql code kan meesturen, waardoor de query wordt aangepast. 

### Risk
Aangezien er wordt gewerkt met een database en sql query's, kan er een risico voor sql injection zijn als er geen maatregelen genomen worden. 

### Counter-measures
In dit project wordt er gebruik gemaakt van Hibernate. Hibernate zorgt ervoor dat alle query's naar de database automatisch gemaakt worden. Ook wordt er gebruik gemaakt van parameters in de query's, waardoor er geen sql geinjecteerd kan worden.

## A5:2017 Broken Acces Control

### Description
Een aanvaller kan toch toegang krijgen tot pagina's of andere dingen waar hij normaal geen toegang voor heeft door bijvoorbeeld een URL handmatig aan te passen, of een bepaalde key handmatig aan te passen.
### Risk

In het project wordt geen authenticatie en autorisatie gebruikt. Maar met authenticatie kan er toch nog wel een risico zijn, bijvoorbeeld als de JWT token die gebruikt is om in te loggen nog geldig blijft na het uitloggen.

### Counter-measures
Toegang standaard weigeren. Zorgen dat de JWT token na het uitloggen niet meer geldig is.
Alle paden en pagina's mogen alleen bekeken worden met de juiste authenticatie/autorisatie.

## A9:2017 Using Components with Known Vulnerabilities
### Description
Deze vulnerability houdt in dat een project gebruik maakt van dependencies of andere components waar bekende problemen in zitten. Vaak gaat het hier om verouderde components.
### Risk
Aangezien er in dit project gewerkt wordt met maven en dependencies, is de kans groot dat niet altijd alle dependencies up to date zijn.

### Counter-measures
Met behulp van de dependabot wordt er automatisch gekeken of alle dependencies de juiste versie zijn. Als de bot verourderde versies vindt, worden deze components automatisch geupdate.