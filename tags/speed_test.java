/*	Sugerez ca fisierul sa fie folosit ca fisier de test al performantelor pentru
 *	generarea mutarilor posibile. Suntt implementate 3 metode de generare a mastii 
 *	de pozitii, primele 2 ambele constand in constructia progresiva a mastii prin 
 *	calcul dinamic pe biti si repectiv prin folosirea a 64 de masti create static,
 *	iar cea de-a treia foloseste masti de mutari pe table libere si construieste
 *	dinamic masca de mutari prin raportare la masca de pozitii ocupate(eu o sa-i
 *	spun metoda Vali, fiindca ele a propus-o).
 *	
 *	Versiunea 1.2
 */

import java.util.Random;

class speed_test{
	static long[] mask = new long[64];

	static byte diag[][][] = new byte[64][4][8];
	
	static long diagNE[] = new long[64];
	static long diagNW[] = new long[64];
	static long diagSE[] = new long[64];
	static long diagSW[] = new long[64];
	
	
	
	
	
	
	public static void printMask(long tabla){
		//	Metoda de afisare a unei masti de biti
		
		long maska;
		for(int i=8;i>=1;i--){
			maska = 128L << 8*(i-1);
			
			for(int j=1;j<=8;j++){
				if((maska & tabla)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			maska = maska >>1;
			}
			
			System.out.println();
		}
	}
	
		//	........................................................................................
	
								//	Functii ce tin de implementarea 1
	
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
			shift = shift << 9;
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
	
	static long mutNE(byte i,byte j,long full_board,long mv){
		long piece = 128L >> (j-1) << 8*(i-1);
		
	
		
		long pb = full_board & mv;
		long dif = pb - piece;
		long flip = dif ^ full_board;
		printMask( flip & mv);
		System.out.println();
		
		return flip & mv;
		//return full_board ^ (full_board -piece -piece);	// asta nu prea merge
		//return (full_board - piece) ^ (full_board | piece);
	}
	
	//	........................................................................................
	
													//	MAIN
	
	public static void main(String arg[]){
		Random ran = new Random();		
		long prim,secund,maska;
		
		//	o tabla generata random, eventual cu prea multe/putine piese
		long full_board = ran.nextLong();	
		long tabla = 0L;
		
	//	printMask(full_board);
		
		int rep = 400000;		//	numarul de repetitii al calculului mastii de mutari
		
		
			/*
			 *	Calculez separat pozitia random ca sa nu afecteze timpul de executie a
			 *	secventei de calcul
			 */
			 
			 
					int linie = 4;//ran.nextInt(8)+1;
					int col = 4;//ran.nextInt(8)+1;
					long pozitie = 128L >> (col-1) << 8*(linie-1);
					full_board = full_board | pozitie;
		
		
		System.out.println("Tabla inintial");
		printMask(full_board);
		
		// ..................................................................................
			
										//	START analiza METODA 1
		
		prim = System.nanoTime();
			
		for(int i=0;i<rep;i++)
			//	pentru pozitia D4
				tabla = calc_board((byte)4,(byte)4,0L);
			//	pentru o pozitie oarecare
			//	tabla = calc_board((byte)linie,(byte)col,full_board);

		
		//		printMask(tabla);
		
		secund = System.nanoTime();
		
		//	timpul de executie in nanosecunde
		System.out.println(""+prim+" "+secund+" "+(secund-prim));
		
										//	SFARSIT analiza METODA 1
		
		// .....................................................................................
		
										//	START analiza METODA 2
		
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
		System.out.println(""+prim+" "+secund+" "+(secund-prim));

										//	SFARSIT analiza METODA 2
										
		// .....................................................................................
		
										//	START analiza METODA 3
		
		prim = System.nanoTime();
		
		diagNE[28] = genNE((byte)4,(byte)4);
		diagNW[28] = genNW((byte)4,(byte)4);
		diagSE[28] = genSE((byte)4,(byte)4);
		diagSW[28] = genSW((byte)4,(byte)4);
		
	//	printMask(diagNW[28]);
								//	System.out.println();
	//	printMask(diagNE[28]);
								//	System.out.println();
	//	printMask(diagSW[28]);
								//	System.out.println();
	//	printMask(diagSE[28]);
								//	System.out.println();
		
		
		System.out.println();
		System.out.println("Tabla mutari lung");
		tabla = mutNE((byte)4,(byte)4,full_board,diagNE[28]);
	//	tabla |= mutNW((byte)4,(byte)4,full_board);
	//	tabla |= mutSE((byte)4,(byte)4,full_board);
	//	tabla |= mutSW((byte)4,(byte)4,full_board);
									printMask(diagNE[28]);			System.out.println();
									printMask(tabla);			System.out.println();
	}
	
}