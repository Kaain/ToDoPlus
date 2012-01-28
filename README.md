#ToDoListAndroidApp
********************
*[MultiSpinner.class](http://stackoverflow.com/questions/5015686/android-spinner-with-multiple-choice)
*[Tutorial for SQLite and ContentProvider] (http://www.vogella.de/articles/AndroidSQLite/article.html)


#Anforderungen
********************

##Datenmodell##	
###Das Datenmodell für Todos soll es erlauben, die folgende Information zu repräsentieren:	
	*den Namen des Todo	1
	*eine Beschreibung des Todo	1
	*die Information darüber, ob das Todo erledigt wurde oder nicht	1
	*die Information darüber, ob es sich um ein besonders wichtiges / ‘favourite’ Todo handelt oder nicht	1
	*das Fälligkeitsdatum des Todo und ggf. eine Uhrzeit	1
	*Alle Informationsbestandteile, inklusive des Namens, sollen nach Erstellung eines Todo änderbar sein.	3
##Persistenz##	
###Die Persistierung der Todos soll auf einem Server erfolgen.	3
	*Änderungen auch auf dem Endgerät selbst gespeichert	3
	*Abgleich der lokalen Daten mit den serverseitig persistierten Daten	4
	*Erfolg/Misserfolg eines Abgleichs zwischen Endgerät und Server sollte durch geeignete Feedbackelemente angezeigt werden	2
##Anmeldung##
###Die Anmeldung soll durch Eingabe einer Email und eines Passworts erfolgen und durch Betätigung eines Login Buttons ausgelöst werden.	3
	*In das Eingabefeld für Email sollen nur Emailadressen eingegeben werden	2
	*Wird keine Email-Adresse eingegeben, erfolgt eine Fehlermeldung.	2
	*Bei Neueingabe des Email Feldes soll die Fehlermeldung nicht mehr sichtbar sein.	2
	*Passwörter sollen numerisch und genau 6 Ziffern lang sein	1
	*Die Eingabe soll verschleiert (‘ausgepunktet’) werden.	1
	*Die Betätigung des Login Buttons soll nur möglich sein, wenn Werte für Email und Passwort eingegeben wurden.	2
	*Nach Betätigung des Login Buttons sollen die eingegebenen Werte an einen Server übermittelt und dort überprüft werden.	4
	*Die Überprüfung soll asynchron erfolgen.	3
	*Schlägt die Überprüfung fehl, wird eine Fehlermeldung ausgegeben.	2
	*Bei Neueingabe eines der beiden Felder verschwindet die Fehlermeldung.	2
	*Bei erfolgreicher Überprüfung der eingegebenen Werte soll die Anzeige der Todos erfolgen.	1
##Todoliste##	
###Die Anzeige der Todoliste soll eine Übersicht über alle Todos darstellen und die Erstellung neuer Todos ermöglichen.	3
	*Sie soll für jedes Todo die folgende Information darstellen:	
	*den Namen	1
	*das Fälligkeitsdatum	1
	*das Erledigsein/Nicht-Erledigtsein	1
	*die Wichtigkeit	1
	*Sie soll es dem Nutzer außerdem ermöglichen, sich für jedes Todo dessen Details anzeigen zu lassen.	1
	*Änderungen der Todoliste, die in der Detailansicht eines Todos getätigt werden können, sollen bei Rückkehr in der Übersicht angezeigt werden.	2
	*Die Information zum Erledigtsein/Nicht-Erledigtsein bzw. zur Wichtigkeit soll modifiziert werden können, ohne die Detailanzeige anzufordern.	3
	*Todos sollen grundsätzlich nach Erledigt/Nichterledigt sortiert sein und dann wahlweise nach Wichtigkeit+Datum oder nach Datum+Wichtigkeit.	3
	*Dem Nutzer soll es möglich sein, die Anzeige nach Datum+Wichtigkeit vs. Wichtigkeit+Datum mit einer minimalen Klickanzahl auszuwählen.	2
	*Überfällige Todos – d.h. Todos mit abgelaufenem Fälligkeitsdatum – sollen visuell besonders hervorgehoben werden.	2
##Detailansicht##
###Die Detailansicht soll alle durch ein Todo repräsentierten Daten darstellen.	4
	*Sie soll außerdem die Änderung zumindest der folgenden Daten eines Todo ermöglichen:	
	*Name	1
	*Beschreibung	1
	*Fälligkeitsdatum und Uhrzeit	2
	*Erledigtsein	1
	*Für die Einstellung von Datum und Uhrzeit sollen die für diesen Zweck durch Android bereitgestellten UI Bedienelemente verwendet werden.	4
	*Das Löschen eines Todos soll ebenfalls über die Detailansicht ermöglicht werden.	2
##Erweiterung##	
###Erlauben Sie auf Ebene des Datenmodells die Assoziation eines Todo mit einer Menge von Kontakten.	1
	*Erlauben Sie dem Nutzer, Todos optional mit einer Menge von Kontakten zu verknüpfen.	2
	*Die Auswahl der Kontakte soll auf Grundlage einer Liste aller verfügbaren Kontakte erfolgen.	3
	*Zeigen Sie die verknüpften Kontakte in der Detailansicht für Todos an.	2
	*Ermöglichen Sie das Hinzufügen und Entfernen von Kontakten zur Liste der verknüpften Kontakte	2
	*Ermöglichen Sie außerdem, dass dem Nutzer für jeden Kontakt die Möglichkeit der Kontaktaufnahme per Mail oder SMS gegeben wird, falls eine Mailadresse oder Mobilfunknummer vorhanden sind.	2
	*Bei Kontaktaufnahme sollen die Mailadresse/Mobilfunknummer, der Titel und die Beschreibung des Todos der jeweils verwendeten Android App übermittelt werden.	2
	*Zeigen Sie alle Kontakte an, mit denen Todos assoziiert sind.	3
	*Zeigen Sie bei Auswahl eines Kontakts alle Todos an, die mit dem Kontakt assoziiert sind.	2
	*Zeigen Sie bei Auswahl eines Todos die Details des Todos an.	1
