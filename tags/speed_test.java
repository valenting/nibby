/*	Sugerez ca fisierul sa fie folosit ca fisier de test al performantelor pentru
 *	generarea mutarilor posibile. Deocamdata sunt implementate 2 metode de
 *	generare a mastii de pozitii, ambele constand in constructia progresiva a
 *  mastii prin calcul dinamic pe biti si repectiv prin folosirea a 64 de masti
 *	create static.
 *	
 *	Testul actual, sa-i zicem versiunea 1.0 calculeaza toate mutarile posibile
 *	pentru un nebun(oricare ar fi pozitionarea sa pe tabla pentru situatia dinamica
 *	si pentru pozitia D4 - ce are numarul 28 in numerotarea "Vali" a tablei de sah -
 *	in cazul static).
 */


import java.util.Date;

class speed_test{
	static long[] mask = new long[64];

	static byte diag[][][] = new byte[64][4][8];
	
	
	
	
	
	static long calc_board(byte i, byte j){	// e declarata static pentru ca am apelat-o din main
		long start = 128L;
		start = start >> (j-1) << 8*(i-1);
		long shift;
		long sup = 0L;
		byte pozi,pozj;
		/*	Am generat toate mutarile posibile pentru un nebun de coordonate date
		 *  facand abstractie de eventuale pozitii ocupate pe tabla. Partea cu
		 *  pozitiile ocupate se putea rezolva usor facand un "si logic" pentru
		 *  fiecare noua pozitie calculata(care e reprezentata printr-o masca)
		 *  ceea ce putea ajuta si la intrerupere ciclurilor de generare a mastii
		 *  de mutari;
		 */
		
		//	Se completeaza masca pentru mutari diagonale inspre h8(coltul dreapta sus)						
		pozi = i;pozj = j;
		shift = start;
		while(pozi<8 && pozj<8){
			shift = shift << 7;
			sup = sup + shift;
			pozi++;pozj++;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a8(coltul stanga sus)	
		pozi = i;pozj = j;	
		shift = start;
		while(pozi<8 && pozj>1){
			shift = shift << 9;
			sup = sup + shift;
			pozi++; pozj--;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a1(coltul stanga jos)	
		pozi = i;pozj = j;
		shift = start;
		while(pozi>1 && pozj>1){
			shift = shift >>9;
			sup = sup + shift;
			pozi--; pozj--;
		}
		
		//	Se completeaza masca pentru mutari diagonale inspre a8(coltul dreapta jos)
		pozi = i;pozj = j;
		shift = start;
		while(pozi>1 && pozj<8){
			shift = shift >> 7;
			sup = sup + shift;
			pozi--; pozj++;
		}
		
		return sup;
	}
	
	static long weird_call(byte lin, byte col){
		/*	Cunoscand pozitia curenta a piesei, parcurg cei 4 vectori de mutari
		 *	corespunzatori celor 4 directii de deplasare si creez masca de
		 *	mutari ca in cazul precedent, cu aceeasi precizare ca s-ar putea 
		 *	introduce si aici verificarea de suprapunere cu o alta piesa pentru
		 *	a opri adaugarea de pozitii invalide.
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
	
	public static void main(String arg[]){
		long prim,secund;
		

		prim = System.nanoTime();	
		int rep = 40000;		//	numarul de repetitii al calculului mastii de mutari
		
		long tabla;
		
		for(int i=0;i<rep;i++)
			tabla = calc_board((byte)4,(byte)4);
		long maska ;
		
		//	Verificarea corectitudinii mastii calculate prin afisaj(ultima masca determinata)
		
		/*
									for(int i=8;i>=1;i--){
										maska = 128L << 8*(i-1);
										for(int j=1;j<=8;j++){
											if((maska & tabla)!=0)
												System.out.print("1 ");
											else
												System.out.print("0 ");
											//mask = mask << 1;
											maska = maska >>1;
										}
										System.out.println();
									}
								System.out.println();
								System.out.println();
		*/
		secund = System.nanoTime();
		
		//	timpul de executie in nanosecunde
		System.out.println(""+prim+" "+secund+" "+(secund-prim));
		
		
		//	Se incepe cronometrarea executie metodei statice
		
	prim = System.nanoTime();
		
		//	Se creeaza cele 64 de masti care corespund fiecarui patrat al tablei de sah
		
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

	/*	Se ompleteaza vectorii ce corespund deplasarii nebunului aflat la D4
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
	
	
	for(int i=0;i<rep;i++)
			tabla = weird_call((byte)4,(byte)4);
	
		/*
									tabla = weird_call((byte)4,(byte)4);
										//long mask = 0x8000000000000000L;
										for(int i=8;i>=1;i--){
											maska = 128L << 8*(i-1);
											for(int j=1;j<=8;j++){
												if((maska & tabla)!=0)
													System.out.print("1 ");
												else
													System.out.print("0 ");
												//mask = mask << 1;
												maska = maska >>1;
											}
											System.out.println();
										}
		*/
	
	
		secund = System.nanoTime();
		//	timpul de executie in nanosecunde
		System.out.println(""+prim+" "+secund+" "+(secund-prim));
	
	}
	
}