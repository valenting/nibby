#################################################
###           Nibblonians                     ###
###           Etapa 3                         ###
#################################################

Membri:
     [322CA] MIRICA Emma (capitan)
     [322CA] TIGORA Andrei
     [322CA] MUSCA  Constantin
     [322CA] GOSU   Valentin
	 
	 
Rularea programului:
	Pentru Windows:
        1. Se compileaza fisierele .java cu scriptul build.bat
		2. Se ruleaza jocul cu scriptul run.bat 
		3. Pentru stergerea fisierelor .class si out*.txt se ruleaza scriptul clean.bat
		SAU
		1. make build -f Makefile.win
		2. make run -f Makefile.win
		3. make clean -f Makefile.win
	Pentru Linux:
        1. Se compileaza fisierele .java cu 'make build'
		2. Se ruleaza jocul cu 'make run'
		3. Se sterg fisierelor .class si out*.txt cu 'make clean'
		
	Pentru teste, echipa noastra a folosit Windows, si recomandam ca testarea sa se realizeze pe Windows.
    A fost testat totusi cu succes si pe Ubuntu.
	
Fisiere:
    Board.java
        Aceasta sursa contine implementarea tablei de sah, si a pieselor continute, sub forma de Bitboard. Tot aici se face parsarea mutarilor in format SAN, deoarece acestea depind de 
		pozitia pieselor pe tabla. De asemenea, aici este implementata si functia de evaluare.
    Usual.java
        Aceasta sursa contine niste functii statice folosite in cadrul proiectului.
    XBoard.java
        Aceasta sursa contine implementarea protocolului Xboard.
        La executie creaza un fisier 'out[0-20].txt' ce contine output-ul Winboard-ului.
    Game.java
        Aceasta sursa contine programul principal.
	Moves.java
		Contine clasele: Move, Node, MoveTree si Openings
		Clasa Move este folosita pentru a defini o mutare oarecare, data prin indicele de start, indicele de final.
		Clasele Node si MoveTree sunt folosite pentru a construi arborele cu mutari de deschidere.
		Clasa Openings contine metode statice apelate pentru obtinerea mutarilor de deschidere si updatarea pozitiei in arbore.
	NegaMax.java
		Aceasta sursa contine implementarea algoritmului negamax. Functia returnBestMove va returna cea mai buna mutare determinata cu algoritmul negamax.
	AlphaBeta.java
		Aceasta sursa contine implementarea algoritmului alphabeta. Ca si in cazul sursei NegaMax, functia returnBestMove intoarce cea mai buna mutare determinata de alphabeta.
		De asemenea, am considerat ca in cazul unei promovari a unui pion, engine-ul nostru va cere promovarea la regina.
	NegaScout.java
		Contine algoritm negascout fara quiescence search.
	NegaScout2.java
		Contine algoritm negascout cu quiescence search.
	BitWise Tricks.java
		Contine functia bitScanForward care intoarce pozitia primul bit 1 dintr-un long.
		
Implementat pentru etapa 3:
		Pentru aceasta etapa au fost implementati algoritmul Negamax si Alphabeta. Deschiderile au fost implementate cu un arbore si la fiecare mutare aflata in arbore se actualizeaza 
	pozitia curenta (actualizare in arbore cand se ia mutarea de la adversar si actualizare pe tabla cand se ia mutarea din arborele de deschideri). Functia nextMove din Board.java este 
	cea care intoarce mutarea engine-ului. Mutarea va fi luata din arbore daca este inceputul jocului si cat timp gaseste o secventa de mutari din arbore, altfel mutarea este obtinuta
	cu algoritmul alphaBeta/NegaMax (am preferat sa folosim alphaBeta caci este mai rapid). Am luat in considerare situatia in care mutarea intoarsa de alphaBeta este nula. Acest lucru 
	nu se va intampla decat cand primim sah mat si atunci jocul este incheiat, este doar o masura de precautie. Functia de evaluare este pe baza de costuri ale pieselor, cu bonusuri si 
	penalizari.
	
Imbunatatiri aduse pentru etapa a 4-a:
		Pentru aceasta etapa au fost implementati algoritmii NegaScout cu quiescence search si fara, si de asemenea si iterative deepening. Deoarece algoritmul negascout este mai eficient 
	atunci cand mutarile sunt sortate dupa anumite prioritati, ar fi fost mult mai eficient implementat si cu ajutorul unei functii de hash si TranspositionTable. In lipsa de timp, am preferat
	sa folosim in IterativeDeepening algoritmul AlphaBeta. O alta imbunatatire importanta a fost inlocuirea functiei Long.getNumberOfTrailingZeros cu functia bitScanForward definita in clasa 
	BitWise Tricks. 
        
OBSERVATII:
		Fisierele de deschideri pulsarCrazyWhite.txt si pulsarCrazyBlack.txt trebuie sa fie neaparat in acelasi folder cu fisierele .class, altfel va da eroare deoarece nu gaseste aceste 
	fisiere. 
