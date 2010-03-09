/*	
 *	Versiunea 1.5 9.3.2010 ora
 *	Probabil versiunea de test finala pentru ca nu mai am idei; din punctul meu de
 *	vedere favorita ramane varianta 2.2 ce foloseste metoda less_weird_call), urmata 
 *	de varianta 3.3 care foloseste mutarePozitiva, ca totate variantele 3.x, si in 
 *	mod deosebit metoda mutareNegativa_proprie.
 *
 *	Sugerez ca fisierul sa fie folosit ca fisier de test al performantelor pentru
 *	generarea mutarilor posibile. Suntt implementate 3 metode de generare a mastii 
 *	de pozitii, primele 2 ambele constand in constructia progresiva a mastii prin 
 *	calcul dinamic pe biti si repectiv prin folosirea a 64 de masti create static,
 *	iar cea de-a treia foloseste masti de mutari pe table libere si construieste
 *	dinamic masca de mutari prin raportare la masca de pozitii ocupate(eu o sa-i
 *	spun metoda Vali, fiindca ele a propus-o).
 *	
 *	Am modificat un break care functiona ciudat in ciclurile for si am scris in loc
 *	un ciclu while la "less_weird_call", varianta care este momentan cea mai rapida.
 *
 *	SUCCES la facut TESTE!
 *	
 *	WARNING!
 *	Apropo, chestiile astea de aici folosesc codificarea MightyVali(adica ai initiala
 *	in care numaram incepand cu a8 deci nu puteti muta direct in alte clase)!!!!!!!
 */

import java.util.Random;

class speed_test{
	static long prim,secund,maska;
	final static int rep = 400000;		//	numarul de repetitii al calculului mastii de mutari
	
	static long[] mask = new long[64];

	static byte diag[][][] = new byte[64][4][8];
	
	
	static byte NW[][] = new byte[64][8];
	static byte NE[][] = new byte[64][8];
	static byte SW[][] = new byte[64][8];
	static byte SE[][] = new byte[64][8];
	
	static long diagNE[] = new long[64];
	static long diagNW[] = new long[64];
	static long diagSE[] = new long[64];
	static long diagSW[] = new long[64];
	
	//static long mainDiag[][] = new long 64
	
	
	
	
	public static void printMask(long tabla){
		//	Metoda de afisare a unei masti de biti
		
		long maska;
		int i,j;
		
		System.out.println();
		
		maska = 128L << 8*7;
		if((maska & tabla)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
		maska = 128L >> 1 << 8*7;
		for( j=1;j<=7;j++){
				if((maska & tabla)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			maska = maska >>1;
			}
			
			System.out.println();
		
		for( i=7;i>=1;i--){
			maska = 128L << 8*(i-1);
			
			for( j=1;j<=8;j++){
				if((maska & tabla)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			maska = maska >>1;
			}
			
			System.out.println();
			
		}
		System.out.println();
	}
	
		//	........................................................................................
	
								//	Functii ce tin de implementarea 1
	
	public static void varianta1_1(){
		
		prim = System.nanoTime();
		long tabla;
		
			
		for(int i=0;i<rep;i++)
			//	pentru pozitia D4
				tabla = calc_board((byte)4,(byte)4,0L);
			//	pentru o pozitie oarecare
			//	tabla = calc_board((byte)linie,(byte)col,full_board);

		
		//		printMask(tabla);
		
		secund = System.nanoTime();
		
		//	timpul de executie in nanosecunde
		System.out.println("Prima metoda:\n"+prim+" "+secund+" "+(secund-prim));
	}
	
	
	static long calc_board(byte i, byte j,long full_board){	// e declarata static pentru ca am apelat-o din main
		long start = 128L;
		start = start >> (j-1) << 8*(i-1);
		long shift;
		long sup = 0L;
		byte pozi,pozj;
		/*	
		 *	Am generat toate mutarile posibile pentru un nebun de coordonate date.
		 *	Nu am facut diferentierea de culoare intre piese, astfel incat nebunul
		 *	sa ia o adversa dar nu una de aceeasi culoare.
		 *
		 *	Probabil va trebui sa facem ceva modificari in randul variabilelor long
		 *	pe care le avem pastrate in memorie, pentru ca ar fi mai util sa avem
		 *	o masca a pieselor albe si una a pieselor negre, fiindca folosim mai des
		 *	mascaAlba/mascaNeagra decat mascaTotala.
		 */
		
		//	Se completeaza masca pentru mutari diagonale inspre h8(coltul dreapta sus)						
		pozi = i;pozj = j;
		shift = start;
		while(pozi<8 && pozj<8){
			shift = shift << 7;
			if((shift & full_board)!=0) //pozitie ocupata
				break;
			sup = sup + shift;
			pozi++;pozj++;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a8(coltul stanga sus)	
		pozi = i;pozj = j;	
		shift = start;
		while(pozi<8 && pozj>1){
			shift = shift << 9;
			if((shift & full_board)!=0) //pozitie ocupata
				break;
			sup = sup + shift;
			pozi++; pozj--;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a1(coltul stanga jos)	
		pozi = i;pozj = j;
		shift = start;
		while(pozi>1 && pozj>1){
			shift = shift >>9;
			if((shift & full_board)!=0) //pozitie ocupata
				break;
			sup = sup + shift;
			pozi--; pozj--;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a8(coltul dreapta jos)
		pozi = i;pozj = j;
		shift = start;
		while(pozi>1 && pozj<8){
			shift = shift >> 7;
			if((shift & full_board)!=0) //pozitie ocupata
				break;
			sup = sup + shift;
			pozi--; pozj++;
		}
		
		return sup;
	}
	

	//	........................................................................................
	
								//	Functii ce tin de implementarea 2
	
	public static void varianta2_1(){
		
		long tabla;
		//	Se incepe cronometrarea executie metodei statice
		//	Apropo, chestia asta pica din start, pentru ca dupa cum veti vedea
		//	este mai lenta
		
		prim = System.nanoTime();
					
		createStatic();
			
		for(int i=0;i<rep;i++)
			tabla = weird_call((byte)4,(byte)4);
	
	
		//		printMask(tabla);
	
		
		secund = System.nanoTime();
		//	timpul de executie in nanosecunde
		System.out.println("Metoda 2(ce foloseste weird_call):\n"+prim+" "+secund+" "+(secund-prim));
	}
	
	static long weird_call(byte lin, byte col){
		/*	
		 *	Metoda ineficienta asupra careia nu voi mai reveni dupa versiunea 1.2
		 *	
		 *	Cunoscand pozitia curenta a piesei, parcurg cei 4 vectori de mutari
		 *	corespunzatori celor 4 directii de deplasare si creez masca de
		 *	mutari ca in cazul precedent, presupunand ca tabla este goala: nu
		 *	verific eventuale suprapuneri cu alte piese(pentru ca am vazut ca
		 *	nu e foarta eficienta metoda).
		 */
		
	
		byte poz = (byte)(8*lin - col);
		long maska = 0L;
		
		for(byte i=0;i<4;i++)
			for(byte j=1;j<=diag[poz][i][0];j++){
				maska = maska + mask[diag[poz][i][j]];
			//	System.out.println(Long.toBinaryString(mask[diag[poz][i][j]]));
			}
		
		return maska;
		 
	}
	
	public static void varianta2_2(long full_board){
		
		/*	Varianta foloseste o metoda de memorare a mastilor progresive mai eficienta
		 *	limitandu-se la a folosi un tablou bidimensional, nu unul unidimensional ca
		 *	anterior; acesta poate fi interpretat astfel: pentru fiecare din cele 4 
		 *	orientari(NW, NE, SW, SE) asociem cate un tablou care pe fiecare din cele 64
		 *	de linii are o structura de forma: numarul de pozitii ce pot fi ocupate pe
		 *	directia respectiva, urmat de indicii casutelor in ordinea.
		 */ 
		
		long tabla = 0L;
		prim = System.nanoTime();
		
		for(int i=0;i<rep;i++)
			tabla = less_weird_call((byte)4,(byte)4,full_board);
		secund = System.nanoTime();
		System.out.println("Ceva mai eficient bazat pe metoda2(less_weird_call):\n"+prim+" "+secund+" "+(secund-prim));
		
		
			printMask(tabla);
	}
	
	static long less_weird_call(byte lin, byte col,long tabla){
		/*	
		 *	Metoda care construieste masca de pozitii pentru cele 4 directii inacelasi
		 *	timp, folosindu-se de 4 vectori ce retin progresiv mastile casutelor in care
		 *	poate muta piesa de la linia i coloana j
		 *
		 */
		
		byte poz = (byte)(8*lin - col);
		long maska = 0L,temp;
		byte j;
		
		j=1;
		while(j<=NW[poz][0]){	//ciclul se executa doar pentru atatea pozitii cate sunt disponibile
				maska = maska + mask[NW[poz][j]];	//	prima pozitie se aduaga neaparat
				//printMask(mask[NW[poz][j]] & tabla);
				if((mask[NW[poz][j]] & tabla)==0L)	//se verifica o eventuala coliziune cu alta piesa
					j++;	// daca nu exista alta piesa se continua cu urmatoarea pozitie memorata
				else
					break;	//	daca s-a detectat intersectie cu alta piesa stop ciclu
		}
		
					// si analog...
		
		j=1;
		while(j<=NE[poz][0]){
				maska = maska + mask[NE[poz][j]];
				//printMask(mask[NE[poz][j]] & tabla);
				if((mask[NE[poz][j]] & tabla)==0L)
					j++;
				else
					break;
		}
		
		j=1;
		while(j<=SW[poz][0]){
				maska = maska + mask[SW[poz][j]];
				//printMask(mask[SW[poz][j]] & tabla);
				if((mask[SW[poz][j]] & tabla)==0L)
					j++;
				else
					break;
		}
		
		j=1;
		while(j<=SE[poz][0]){
				maska = maska + mask[SE[poz][j]];
			//	printMask(mask[SE[poz][j]] & tabla);
				if((mask[SE[poz][j]] & tabla)==0L)
					j++;
				else
					break;
		}
		 
		 return maska;
	}
	
	public static void createStatic(){
		/*
		 *	Face parte dintr-o abordarea ineficienta asupra careia nu voi mai reveni
		 *	incepand cu versiunea 1.2
		 *
		 *	Se creeaza cele 64 de masti care corespund fiecarui patrat al tablei de sah
		 */
		
		mask[0] = 0x0000000000000001L;
		mask[1] = 0x0000000000000002L;
		mask[2] = 0x0000000000000004L;
		mask[3] = 0x0000000000000008L;
		mask[4] = 0x0000000000000010L;
		mask[5] = 0x0000000000000020L;
		mask[6] = 0x0000000000000040L;
		mask[7] = 0x0000000000000080L;
		mask[8] = 0x0000000000000100L;
		mask[9] = 0x0000000000000200L;
		mask[10] = 0x0000000000000400L;
		mask[11] = 0x0000000000000800L;
		mask[12] = 0x0000000000001000L;
		mask[13] = 0x0000000000002000L;
		mask[14] = 0x0000000000004000L;
		mask[15] = 0x0000000000008000L;
		mask[16] = 0x0000000000010000L;
		mask[17] = 0x0000000000020000L;
		mask[18] = 0x0000000000040000L;
		mask[19] = 0x0000000000080000L;
		mask[20] = 0x0000000000100000L;
		mask[21] = 0x0000000000200000L;
		mask[22] = 0x0000000000400000L;
		mask[23] = 0x0000000000800000L;
		mask[24] = 0x0000000001000000L;
		mask[25] = 0x0000000002000000L;
		mask[26] = 0x0000000004000000L;
		mask[27] = 0x0000000008000000L;
		mask[28] = 0x0000000010000000L;
		mask[29] = 0x0000000020000000L;
		mask[30] = 0x0000000040000000L;
		mask[31] = 0x0000000080000000L;
		mask[32] = 0x0000000100000000L;
		mask[33] = 0x0000000200000000L;
		mask[34] = 0x0000000400000000L;
		mask[35] = 0x0000000800000000L;
		mask[36] = 0x0000001000000000L;
		mask[37] = 0x0000002000000000L;
		mask[38] = 0x0000004000000000L;
		mask[39] = 0x0000008000000000L;
		mask[40] = 0x0000010000000000L;
		mask[41] = 0x0000020000000000L;
		mask[42] = 0x0000040000000000L;
		mask[43] = 0x0000080000000000L;
		mask[44] = 0x0000100000000000L;
		mask[45] = 0x0000200000000000L;
		mask[46] = 0x0000400000000000L;
		mask[47] = 0x0000800000000000L;
		mask[48] = 0x0001000000000000L;
		mask[49] = 0x0002000000000000L;
		mask[50] = 0x0004000000000000L;
		mask[51] = 0x0008000000000000L;
		mask[52] = 0x0010000000000000L;
		mask[53] = 0x0020000000000000L;
		mask[54] = 0x0040000000000000L;
		mask[55] = 0x0080000000000000L;
		mask[56] = 0x0100000000000000L;
		mask[57] = 0x0200000000000000L;
		mask[58] = 0x0400000000000000L;
		mask[59] = 0x0800000000000000L;
		mask[60] = 0x1000000000000000L;
		mask[61] = 0x2000000000000000L;
		mask[62] = 0x4000000000000000L;
		mask[63] = 0x8000000000000000L;
	
		/*	Se completeaza vectorii ce corespund deplasarii nebunului aflat la D4
		 *	pe cele 4 directii, memorand numarul de ordine al patratelor parcurse
		 *	progresive pe fiecare directie.
		 */
		
		// spre A8
		diag[28][0][0] = 3;
		diag[28][0][1] = (byte)37;
		diag[28][0][2] = (byte)46;
		diag[28][0][3] = (byte)55;
		
		//	spre A1
		diag[28][1][0] = 3;
		diag[28][1][1] = (byte)21;
		diag[28][1][2] = (byte)14;
		diag[28][1][3] = (byte)7;
		
		//	spre H8
		diag[28][2][0] = 4;
		diag[28][2][1] = (byte)35;
		diag[28][2][2] = (byte)42;
		diag[28][2][3] = (byte)49;
		diag[28][2][4] = (byte)56;
		
		//	spre H1
		diag[28][3][0] = 3;
		diag[28][3][1] = (byte)19;
		diag[28][3][2] = (byte)10;
		diag[28][3][3] = (byte)1;
	
		//	NW
		NW[28][0] = 3;
		NW[28][1] = (byte)37;
		NW[28][2] = (byte)46;
		NW[28][3] = (byte)55;
		
		//	SW
		SW[28][0] = 3;
		SW[28][1] = (byte)21;
		SW[28][2] = (byte)14;
		SW[28][3] = (byte)7;
		
		//	NE
		NE[28][0] = 4;
		NE[28][1] = (byte)35;
		NE[28][2] = (byte)42;
		NE[28][3] = (byte)49;
		NE[28][4] = (byte)56;
		
		//	SE
		SE[28][0] = 3;
		SE[28][1] = (byte)19;
		SE[28][2] = (byte)10;
		SE[28][3] = (byte)1;
		
		
		
		//	NW
		NW[56][0] = 0;
		
		//	SW
		SW[56][0] = 7;
		SW[56][1] = (byte)49;
		SW[56][2] = (byte)42;
		SW[56][3] = (byte)35;
		SW[56][4] = (byte)28;
		SW[56][5] = (byte)21;
		SW[56][6] = (byte)14;
		SW[56][7] = (byte)7;
		
		//	NE
		NE[56][0] = 0;

		//	SE
		SE[56][0] = 0;
		
		//Le-am adunat pe toate aici ca sa pot sa le creez dintr-un singur apel
		diagNE[28] = genNE((byte)4,(byte)4);
		diagNE[56] = genNE((byte)8,(byte)8);
		diagNW[28] = genNW((byte)4,(byte)4);
		diagNW[56] = genNW((byte)8,(byte)8);
		diagSE[28] = genSE((byte)4,(byte)4);
		diagSW[28] = genSW((byte)4,(byte)4);
		diagSE[56] = genSE((byte)8,(byte)8);
		diagSW[56] = genSW((byte)8,(byte)8);
	}
	
	
	
	
	//	........................................................................................
	
								//	Functii ce tin de implementarea 3
	
	/*	Functii de tin de cea de-a treia implementare.
	 *
	 *	Primele 4 genereaza mastile de deplasare pentru o tabla libera,
	 *	pentru un nebun. In implementare nu vom folosi asemenea functii
	 *	decat o data, sau nici macar o data; probabil vom folosi o
	 *	insiruire de masti gen depNW[0] = cod0; depNW[1] = cod1;...
	 *	
	 *	Urmatoarele 4 sunt cele care calculeaza mutarile pe o tabla ce
	 *	contine si alte piese.
	 */
	 
	 
	static long genNW(byte i, byte j){
		long shift = 128L >> (j-1) << 8*(i-1);
		long sup = 0L;
		
		while(i<8 && j>1){
			shift = shift << 7<<2;
			sup = sup + shift;
			i++;j--;
		
			}
		
		
		return sup;		
	}
	
	
	
	static long genNE(byte i, byte j){
		long shift = 128L >> (j-1) << 8*(i-1);
		long sup = 0L;
		
		while(i<8 && j<8){
			shift = shift << 7;
			sup = sup + shift;
			i++;j++;
		}
		return sup;	
	}
	
	static long genSW(byte i, byte j){
		long shift = 128L >> (j-1) << 8*(i-1);
		long sup = 0L;
		
		while(i>1 && j>1){
			shift = shift >>7;
			sup = sup + shift;
			i--;j--;
		}
		return sup;		
	}
	
	static long genSE(byte i, byte j){
		long shift = 128L >> (j-1) << 8*(i-1);
		long sup = 0L;
		
		while(i>1 && j<8){
			shift = shift >> 9;
			sup = sup + shift;
			i--;j++;
		}
		return sup;		
	}
	
	/*	
	 *	mutNE si mutNW sunt mutari pozitive cu rezultat dupa formula o^(o-2r)
	 *	unde o - occupancy este dat de full_board si r - masca de mutari
	 */
	
	static long mutarePozitiva(byte i,byte j,long full_board,long mv){
		long piece = 128L >> (j-1) << 8*(i-1);

		long pb = full_board & mv;
		pb -= piece;
		long flip = pb ^ full_board;
		
											//printMask( flip & mv);
											//System.out.println();
		
		return flip & mv;
	}
	
	
	static long mutareNegativa_proprie(byte i,byte j,long full_board,long mv){
		/*
		 * Voi explica cel mai bine pe un exemplu:
		 *		pozitie					mv				full_board & mv 
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0	
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 * 	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 1 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 0 1 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 1 0 0 0 0 0 0		0 1 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			1 0 0 0 0 0 0 0		1 0 0 0 0 0 0 0
		 *
		 *	pozitia piesei			mutari negative		intersectia mutarilor cu tabla
		 *
		 *	Cu Long.highestOneBit aplicat pe ultima tabla, obtin:
		 * Long.highestOneBit		pozitie-Long.hob	tabla de mutari
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0	
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 * 	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 0 0 0 1 1 1 1		0 0 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			1 1 1 1 1 1 1 1		0 0 1 0 0 0 0 0
		 *	0 1 0 0 0 0 0 0			1 1 0 0 0 0 0 0		0 1 0 0 0 0 0 0
		 *	0 0 0 0 0 0 0 0			0 0 0 0 0 0 0 0		0 0 0 0 0 0 0 0
		 *
		 *	tabla de mutari se obtin facans si intre rezultatul scaderii pozitie - Long.hob
		 *	si facnd si logic cu masca de mutari maximale
		 *
		 *	Observatie: daca intersectia dintre: mv si full_board este 0 se intoarce
		 *	pur si simplu masca de mutari maximale.
		 */		
													//	System.out.println("Incep analiza");
			
		long piece = 128L >> (j-1) << 8*(i-1);		//	printMask(piece);System.out.println();
		long intersectie = mv & full_board;			//	printMask(intersectie);System.out.println();
		if(intersectie==0)
			return mv;				
		
		long primul = Long.highestOneBit(intersectie);//	printMask(primul);System.out.println();
													//	printMask(piece-primul);System.out.println();
		return (piece-primul)&mv;

	}
	
	
	static long mutareNegativa_fast(byte i,byte j,long full_board,long mv){
		/*
		 *	Metoda pe care am incercat sa o imbunatatesc pe baza mutarilor pozitive
		 *	reducand operatiile de inversare si folosind masti de mutari pozitive
		 *	in loc sa inversez niste masti de mutari negative, ceea ce ar aduce si
		 *	o scadere a numarului de masti ce trebuie sa fie memorate.
		 *
		 */
		
		
		long piece = 128L >> (j-1) << 8*(i-1) ;
		long fb = Long.reverse(full_board);
		
		long pb = fb & mv;
		pb -= piece;
		long flip = pb ^ fb;
		
										//Inca nu am incredere in metoda asta, asa ca ramane
										//afisajul
		
										//	printMask( fw );
										//	System.out.println();
		
		return Long.reverse(flip & mv);
	}
	
	static long mutareNegativa(byte i,byte j,long full_board,long mv){
		/*
		 *	Metoda preluata cu nesimtire de pe site
		 *	
		 *	slider e pozitia nebunului
		 *	linemask - longul de atac
		 *
		 *	forward  = occ & lineMask; // also performs the first subtraction by clearing the s in o
   			reverse  = byteswap( forward ); // o'-s'
   			forward -=         ( slider  ); // o -2s
   			reverse -= byteswap( slider  ); // o'-2s'
   			forward ^= byteswap( reverse );
   			return forward & lineMask;      // mask the line again
		*/
				
		long piece = 128L >> (j-1) << 8*(i-1);
		long fw = full_board & mv;
		long rev = Long.reverse(fw);
		fw -= piece ;
		rev -= Long.reverse(piece);
		fw ^= Long.reverse(rev);
		
		return fw & mv;
	}
	
	public static void varianta3_1(long full_board){
		
		long tabla;
		
		prim = System.nanoTime();
				
		diagNE[28] = genNE((byte)4,(byte)4);
		diagNE[56] = genNE((byte)8,(byte)8);
		diagNW[28] = genNW((byte)4,(byte)4);
		diagNW[56] = genNW((byte)8,(byte)8);
		diagSE[28] = genSE((byte)4,(byte)4);
		diagSW[28] = genSW((byte)4,(byte)4);
		diagSE[56] = genSE((byte)8,(byte)8);
		diagSW[56] = genSW((byte)8,(byte)8);
		
	//	printMask(diagNW[35]);
								//	System.out.println();
	//	printMask(diagNE[35]);
								//	System.out.println();
	//	printMask(diagSW[28]);
								//	System.out.println();
	//	printMask(diagSE[28]);
	
		for(int i=0;i<rep;i++){
			tabla = mutarePozitiva((byte)4,(byte)4,full_board,diagNE[28]);
			tabla |= mutarePozitiva((byte)4,(byte)4,full_board,diagNW[28]);
			tabla |= mutareNegativa((byte)4,(byte)4,full_board,diagSE[28]);
			tabla |= mutareNegativa((byte)4,(byte)4,full_board,diagSW[28]);
		}
									
	//								printMask(tabla);			System.out.println();
		
		secund = System.nanoTime();
		//	timpul de executie in nanosecunde
	System.out.println("Metoda 3 clasica(mutareNegativa):\n"+prim+" "+secund+" "+(secund-prim));
	}
	
	
	
	public static void varianta3_2(long full_board){
		
		long tabla;
		
		prim = System.nanoTime();
	
		tabla = 0L;
		for(int i=0;i<rep;i++){
				tabla = mutarePozitiva((byte)4,(byte)4,full_board,diagNE[28]);
				tabla |= mutarePozitiva((byte)4,(byte)4,full_board,diagNW[28]);
				tabla |= mutareNegativa_fast((byte)4,(byte)4,full_board,diagNE[35]);
				tabla |= mutareNegativa_fast((byte)4,(byte)4,full_board,diagNW[35]);
	
			}
		
		secund = System.nanoTime();
		System.out.println("Metoda 3 imbunatatita(mutareNegativa_fast):\n"+prim+" "+secund+" "+(secund-prim));
	}
	
	public static void varianta3_3(long full_board){
		
		long tabla;
		
		prim = System.nanoTime();
	
		tabla = 0L;
		for(int i=0;i<rep;i++){
				tabla = mutarePozitiva((byte)4,(byte)4,full_board,diagNE[28]);
				tabla |= mutarePozitiva((byte)4,(byte)4,full_board,diagNW[28]);
				tabla |= mutareNegativa_proprie((byte)4,(byte)4,full_board,diagSE[28]);
				tabla |= mutareNegativa_proprie((byte)4,(byte)4,full_board,diagSW[28]);
			}
		
		secund = System.nanoTime();
		System.out.println("Metoda 3 dezvoltata de mine(mutareNegativa_proprie):\n"+prim+" "+secund+" "+(secund-prim));	
		
		printMask(tabla);			System.out.println();
	}

	//	........................................................................................
	
													//	MAIN
	
	public static void main(String arg[]){
		Random ran = new Random();		
		
		
		//	o tabla generata random, eventual cu prea multe/putine piese
		long full_board = ran.nextLong();	
		long tabla = 0L;
		
	//	printMask(full_board);
		
					int linie = 4;//ran.nextInt(8)+1;
					int col = 4;//ran.nextInt(8)+1;
					long pozitie = 128L >> (col-1) << 8*(linie-1);
					full_board = full_board | pozitie;
		
		createStatic();
		
		System.out.println("Tabla inintial");
		printMask(full_board);
		
		
		
		varianta1_1();
		varianta2_1();
		varianta2_2(full_board);
		varianta3_1(full_board);
		varianta3_2(full_board);
		varianta3_3(full_board);
		
	}
	
}