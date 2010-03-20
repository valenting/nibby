#################################################
###           Nibblonians                     ###
###           Etapa 1                         ###
#################################################

Membri:
     [322CA] MIRICA Emma (capitan)
	 [322CA] TIGORA Andrei
	 [322CA] MUSCA  Constantin
	 [322CA] GOSU   Valentin
	 
	 
Rularea programului:
	1. Se compileaza fisierele .java folosind comanda 'javac *.java' sau cu scriptul build.bat
		(( Eventual se ruleaza comanda cu calea completa in directorul cu sursele: 
			"C:\Program Files\Java\jdk1.6.0_16\bin\javac" *.java  ))
	2. Se deschide WinBoard si se ruleaza cu comanda
         /cp /fd="D:\cale\executabil" /fcp='java Game'
	Pentru teste, echipa noastra a folosit Windows, si recomandam ca testarea sa se realizeze pe Windows.
	A fost testat totusi cu succes si pe Ubuntu.
	
Fisiere:
	Board.java
		Aceasta sursa contine implementarea tablei de sah, si a pieselor continute, sub forma de Bitboard. Tot aici se face parsarea mutarilor in format SAN, deoarece acestea depind de pozitia pieselor pe tabla.
	Usual.java
		Aceasta sursa contine niste functii statice folosite in cadrul proiectului.
	XBoard.java
		Aceasta sursa contine implementarea protocolului Xboard.
	Game.java
		Aceasta sursa contine programul principal.
		
Implementat pentru etapa 1:
	Programul este capabil sa mute pioni. Au fost implementate inclusiv promovarile si miscarile en-passant.
	Jocul selecteaza la fiecare mutare primul pion. Daca acesta poate face mutari, se executa prima dintre ele. Daca pionul nu poate face nicio mutare, engine-ul da 'resign'.
	

	
