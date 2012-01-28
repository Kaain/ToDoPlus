#ToDoListAndroidApp
********************
*[MultiSpinner.class](http://stackoverflow.com/questions/5015686/android-spinner-with-multiple-choice)
*[Tutorial for SQLite and ContentProvider] (http://www.vogella.de/articles/AndroidSQLite/article.html)


#Anforderungen
********************

##Datenmodell##	
###Das Datenmodell f�r Todos soll es erlauben, die folgende Information zu repr�sentieren:	
	*den Namen des Todo	1
	*eine Beschreibung des Todo	1
	*die Information dar�ber, ob das Todo erledigt wurde oder nicht	1
	*die Information dar�ber, ob es sich um ein besonders wichtiges / �favourite� Todo handelt oder nicht	1
	*das F�lligkeitsdatum des Todo und ggf. eine Uhrzeit	1
	*Alle Informationsbestandteile, inklusive des Namens, sollen nach Erstellung eines Todo �nderbar sein.	3
##Persistenz##	
###Die Persistierung der Todos soll auf einem Server erfolgen.	3
	*�nderungen auch auf dem Endger�t selbst gespeichert	3
	*Abgleich der lokalen Daten mit den serverseitig persistierten Daten	4
	*Erfolg/Misserfolg eines Abgleichs zwischen Endger�t und Server sollte durch geeignete Feedbackelemente angezeigt werden	2
##Anmeldung##
###Die Anmeldung soll durch Eingabe einer Email und eines Passworts erfolgen und durch Bet�tigung eines Login Buttons ausgel�st werden.	3
	*In das Eingabefeld f�r Email sollen nur Emailadressen eingegeben werden	2
	*Wird keine Email-Adresse eingegeben, erfolgt eine Fehlermeldung.	2
	*Bei Neueingabe des Email Feldes soll die Fehlermeldung nicht mehr sichtbar sein.	2
	*Passw�rter sollen numerisch und genau 6 Ziffern lang sein	1
	*Die Eingabe soll verschleiert (�ausgepunktet�) werden.	1
	*Die Bet�tigung des Login Buttons soll nur m�glich sein, wenn Werte f�r Email und Passwort eingegeben wurden.	2
	*Nach Bet�tigung des Login Buttons sollen die eingegebenen Werte an einen Server �bermittelt und dort �berpr�ft werden.	4
	*Die �berpr�fung soll asynchron erfolgen.	3
	*Schl�gt die �berpr�fung fehl, wird eine Fehlermeldung ausgegeben.	2
	*Bei Neueingabe eines der beiden Felder verschwindet die Fehlermeldung.	2
	*Bei erfolgreicher �berpr�fung der eingegebenen Werte soll die Anzeige der Todos erfolgen.	1
##Todoliste##	
###Die Anzeige der Todoliste soll eine �bersicht �ber alle Todos darstellen und die Erstellung neuer Todos erm�glichen.	3
	*Sie soll f�r jedes Todo die folgende Information darstellen:	
	*den Namen	1
	*das F�lligkeitsdatum	1
	*das Erledigsein/Nicht-Erledigtsein	1
	*die Wichtigkeit	1
	*Sie soll es dem Nutzer au�erdem erm�glichen, sich f�r jedes Todo dessen Details anzeigen zu lassen.	1
	*�nderungen der Todoliste, die in der Detailansicht eines Todos get�tigt werden k�nnen, sollen bei R�ckkehr in der �bersicht angezeigt werden.	2
	*Die Information zum Erledigtsein/Nicht-Erledigtsein bzw. zur Wichtigkeit soll modifiziert werden k�nnen, ohne die Detailanzeige anzufordern.	3
	*Todos sollen grunds�tzlich nach Erledigt/Nichterledigt sortiert sein und dann wahlweise nach Wichtigkeit+Datum oder nach Datum+Wichtigkeit.	3
	*Dem Nutzer soll es m�glich sein, die Anzeige nach Datum+Wichtigkeit vs. Wichtigkeit+Datum mit einer minimalen Klickanzahl auszuw�hlen.	2
	*�berf�llige Todos � d.h. Todos mit abgelaufenem F�lligkeitsdatum � sollen visuell besonders hervorgehoben werden.	2
##Detailansicht##
###Die Detailansicht soll alle durch ein Todo repr�sentierten Daten darstellen.	4
	*Sie soll au�erdem die �nderung zumindest der folgenden Daten eines Todo erm�glichen:	
	*Name	1
	*Beschreibung	1
	*F�lligkeitsdatum und Uhrzeit	2
	*Erledigtsein	1
	*F�r die Einstellung von Datum und Uhrzeit sollen die f�r diesen Zweck durch Android bereitgestellten UI Bedienelemente verwendet werden.	4
	*Das L�schen eines Todos soll ebenfalls �ber die Detailansicht erm�glicht werden.	2
##Erweiterung##	
###Erlauben Sie auf Ebene des Datenmodells die Assoziation eines Todo mit einer Menge von Kontakten.	1
	*Erlauben Sie dem Nutzer, Todos optional mit einer Menge von Kontakten zu verkn�pfen.	2
	*Die Auswahl der Kontakte soll auf Grundlage einer Liste aller verf�gbaren Kontakte erfolgen.	3
	*Zeigen Sie die verkn�pften Kontakte in der Detailansicht f�r Todos an.	2
	*Erm�glichen Sie das Hinzuf�gen und Entfernen von Kontakten zur Liste der verkn�pften Kontakte	2
	*Erm�glichen Sie au�erdem, dass dem Nutzer f�r jeden Kontakt die M�glichkeit der Kontaktaufnahme per Mail oder SMS gegeben wird, falls eine Mailadresse oder Mobilfunknummer vorhanden sind.	2
	*Bei Kontaktaufnahme sollen die Mailadresse/Mobilfunknummer, der Titel und die Beschreibung des Todos der jeweils verwendeten Android App �bermittelt werden.	2
	*Zeigen Sie alle Kontakte an, mit denen Todos assoziiert sind.	3
	*Zeigen Sie bei Auswahl eines Kontakts alle Todos an, die mit dem Kontakt assoziiert sind.	2
	*Zeigen Sie bei Auswahl eines Todos die Details des Todos an.	1
