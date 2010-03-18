import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *	Forma simplificata la insistentele lui Vali
 *	Am mai modificat ceva si acum muta toti pionii
 *	Functioneaza corect pentru mutarea pionilor albi si negri (la nivelul etapei I)
 *
 *	Am implementat si o functie de determinare a situatiei de sah-mat
 */

//Asta random este doar de test
import java.util.Random;

public class Board {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6; // CONSTANTE ALB
	public static final byte           B_PAWN=9, B_ROOK=10, B_KNIGHT=11, B_BISHOP=12, B_QUEEN=13, B_KING=14; // CONSTANTE NEGRU
	private long table;


	private long[] pieces = new long[7]; // pieces[1] = pion, pieces[2] = rook... etc 
	private long[] color = new long[2]; // white.. black

	//	Variabile adaugate de Andrei columnPosition,rowPosition - ca sa nu mai fac calcule
	//	de asemenea enPassantWhite - pe ce coloana se poate face captura en passant la 
	//	piesa White si analog enPassantBlack
	public static long knightMasks[] = new long[64];
	public static long kingMasks[] = new long[64];
	public static final byte columnPosition[] = new byte[64];
	public static final byte rowPosition[] = new byte[64]; 
	private byte enPassantWhite = 9;
	private byte enPassantBlack = 9;
	private boolean canLongCastleWhite = true;
	private boolean canShortCastleWhite = true;
	private boolean canShortCastleBlack = true;
	private boolean canLongCastleBlack = true;
	public static char columnChar[] = {'a','b','c','d','e','f','g','h'};
	public static char rowChar[] = {'1','2','3','4','5','6','7','8'};
	public static char pieceForSAN[] = {' ',' ','R','N','B','Q','K'};

	public static final long longCastlingWhite = 0x00000000000000EL;
	public static final long shortCastlingWhite = 0x0000000000000060L;
	public static final long longCastlingBlack = 0xE00000000000000L;
	public static final long shortCastlingBlack = 0x6000000000000000L;

	byte types[];


	public Board(){
		// la Start:

		/* 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 */ 
		/* h8 ->> a8 | h7 ->> a7 | ...                                                       | h1 ->> a1 */

		table = 0xFFFF00000000FFFFL;

		color[0]= 0x000000000000FFFFL; //white
		color[1]= 0xFFFF000000000000L; //black

		pieces[1] = 0x00FF00000000FF00L; // pawn
		pieces[2] = 0x8100000000000081L; // rook
		pieces[3] = 0x4200000000000042L;   // knight
		pieces[4] = 0x2400000000000024L;   // bishop
		pieces[5] = 0x0800000000000008L;//Usual.boardMask("d1") | Usual.boardMask("d8");  // queen
		pieces[6] = 0x1000000000000010L;//Usual.boardMask("e1") | Usual.boardMask("e8");  // king	

		types=initTypes();

		//	Adaugat de Andrei - preferabil sa nu se initialize astea statice aici
		generateStatic();
	}

	byte [] initTypes() {
		byte[] tips={2,3,4,5,6,4,3,2,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,9,9,9,9,9,9,9,9,10,11,12,13,14,12,11,10};
		return tips;
	}

	public Board getCopy(){
		try {
			return (Board)this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte getPieceType(int pos){
		return types[pos];
	}

	//.............................................................................................
	/*
	 *	Functii adaugate de Andrei 
	 *
	 */

	public byte getColor(long piece){
		if((piece & color[0])!=0L)
			return (byte)0;
		return (byte)1;
	}


	public void move(int pos1, int pos2){
		this.move(pos1, pos2, this.getPieceType(pos1));
	}


	public void generateStatic(){
		knightMasks[0]=0x20400L;
		knightMasks[1]=0x50800L;
		knightMasks[2]=0xa1100L;
		knightMasks[3]=0x142200L;
		knightMasks[4]=0x284400L;
		knightMasks[5]=0x508800L;
		knightMasks[6]=0xa01000L;
		knightMasks[7]=0x402000L;
		knightMasks[8]=0x2040004L;
		knightMasks[9]=0x5080008L;
		knightMasks[10]=0xa110011L;
		knightMasks[11]=0x14220022L;
		knightMasks[12]=0x28440044L;
		knightMasks[13]=0x50880088L;
		knightMasks[14]=0xa0100010L;
		knightMasks[15]=0x40200020L;
		knightMasks[16]=0x204000402L;
		knightMasks[17]=0x508000805L;
		knightMasks[18]=0xa1100110aL;
		knightMasks[19]=0x1422002214L;
		knightMasks[20]=0x2844004428L;
		knightMasks[21]=0x5088008850L;
		knightMasks[22]=0xa0100010a0L;
		knightMasks[23]=0x4020002040L;
		knightMasks[24]=0x20400040200L;
		knightMasks[25]=0x50800080500L;
		knightMasks[26]=0xa1100110a00L;
		knightMasks[27]=0x142200221400L;
		knightMasks[28]=0x284400442800L;
		knightMasks[29]=0x508800885000L;
		knightMasks[30]=0xa0100010a000L;
		knightMasks[31]=0x402000204000L;
		knightMasks[32]=0x2040004020000L;
		knightMasks[33]=0x5080008050000L;
		knightMasks[34]=0xa1100110a0000L;
		knightMasks[35]=0x14220022140000L;
		knightMasks[36]=0x28440044280000L;
		knightMasks[37]=0x50880088500000L;
		knightMasks[38]=0xa0100010a00000L;
		knightMasks[39]=0x40200020400000L;
		knightMasks[40]=0x204000402000000L;
		knightMasks[41]=0x508000805000000L;
		knightMasks[42]=0xa1100110a000000L;
		knightMasks[43]=0x1422002214000000L;
		knightMasks[44]=0x2844004428000000L;
		knightMasks[45]=0x5088008850000000L;
		knightMasks[46]=0xa0100010a0000000L;
		knightMasks[47]=0x4020002040000000L;
		knightMasks[48]=0x400040200000000L;
		knightMasks[49]=0x800080500000000L;
		knightMasks[50]=0x1100110a00000000L;
		knightMasks[51]=0x2200221400000000L;
		knightMasks[52]=0x4400442800000000L;
		knightMasks[53]=0x8800885000000000L;
		knightMasks[54]=0x100010a000000000L;
		knightMasks[55]=0x2000204000000000L;
		knightMasks[56]=0x4020000000000L;
		knightMasks[57]=0x8050000000000L;
		knightMasks[58]=0x110a0000000000L;
		knightMasks[59]=0x22140000000000L;
		knightMasks[60]=0x44280000000000L;
		knightMasks[61]=0x88500000000000L;
		knightMasks[62]=0x10a00000000000L;
		knightMasks[63]=0x20400000000000L;

		rowPosition[0]=0;
		columnPosition[0]=0;
		rowPosition[1]=0;
		columnPosition[1]=1;
		rowPosition[2]=0;
		columnPosition[2]=2;
		rowPosition[3]=0;
		columnPosition[3]=3;
		rowPosition[4]=0;
		columnPosition[4]=4;
		rowPosition[5]=0;
		columnPosition[5]=5;
		rowPosition[6]=0;
		columnPosition[6]=6;
		rowPosition[7]=0;
		columnPosition[7]=7;
		rowPosition[8]=1;
		columnPosition[8]=0;
		rowPosition[9]=1;
		columnPosition[9]=1;
		rowPosition[10]=1;
		columnPosition[10]=2;
		rowPosition[11]=1;
		columnPosition[11]=3;
		rowPosition[12]=1;
		columnPosition[12]=4;
		rowPosition[13]=1;
		columnPosition[13]=5;
		rowPosition[14]=1;
		columnPosition[14]=6;
		rowPosition[15]=1;
		columnPosition[15]=7;
		rowPosition[16]=2;
		columnPosition[16]=0;
		rowPosition[17]=2;
		columnPosition[17]=1;
		rowPosition[18]=2;
		columnPosition[18]=2;
		rowPosition[19]=2;
		columnPosition[19]=3;
		rowPosition[20]=2;
		columnPosition[20]=4;
		rowPosition[21]=2;
		columnPosition[21]=5;
		rowPosition[22]=2;
		columnPosition[22]=6;
		rowPosition[23]=2;
		columnPosition[23]=7;
		rowPosition[24]=3;
		columnPosition[24]=0;
		rowPosition[25]=3;
		columnPosition[25]=1;
		rowPosition[26]=3;
		columnPosition[26]=2;
		rowPosition[27]=3;
		columnPosition[27]=3;
		rowPosition[28]=3;
		columnPosition[28]=4;
		rowPosition[29]=3;
		columnPosition[29]=5;
		rowPosition[30]=3;
		columnPosition[30]=6;
		rowPosition[31]=3;
		columnPosition[31]=7;
		rowPosition[32]=4;
		columnPosition[32]=0;
		rowPosition[33]=4;
		columnPosition[33]=1;
		rowPosition[34]=4;
		columnPosition[34]=2;
		rowPosition[35]=4;
		columnPosition[35]=3;
		rowPosition[36]=4;
		columnPosition[36]=4;
		rowPosition[37]=4;
		columnPosition[37]=5;
		rowPosition[38]=4;
		columnPosition[38]=6;
		rowPosition[39]=4;
		columnPosition[39]=7;
		rowPosition[40]=5;
		columnPosition[40]=0;
		rowPosition[41]=5;
		columnPosition[41]=1;
		rowPosition[42]=5;
		columnPosition[42]=2;
		rowPosition[43]=5;
		columnPosition[43]=3;
		rowPosition[44]=5;
		columnPosition[44]=4;
		rowPosition[45]=5;
		columnPosition[45]=5;
		rowPosition[46]=5;
		columnPosition[46]=6;
		rowPosition[47]=5;
		columnPosition[47]=7;
		rowPosition[48]=6;
		columnPosition[48]=0;
		rowPosition[49]=6;
		columnPosition[49]=1;
		rowPosition[50]=6;
		columnPosition[50]=2;
		rowPosition[51]=6;
		columnPosition[51]=3;
		rowPosition[52]=6;
		columnPosition[52]=4;
		rowPosition[53]=6;
		columnPosition[53]=5;
		rowPosition[54]=6;
		columnPosition[54]=6;
		rowPosition[55]=6;
		columnPosition[55]=7;
		rowPosition[56]=7;
		columnPosition[56]=0;
		rowPosition[57]=7;
		columnPosition[57]=1;
		rowPosition[58]=7;
		columnPosition[58]=2;
		rowPosition[59]=7;
		columnPosition[59]=3;
		rowPosition[60]=7;
		columnPosition[60]=4;
		rowPosition[61]=7;
		columnPosition[61]=5;
		rowPosition[62]=7;
		columnPosition[62]=6;
		rowPosition[63]=7;
		columnPosition[63]=7;

		kingMasks[0]=0x302L;
		kingMasks[1]=0x705L;
		kingMasks[2]=0xe0aL;
		kingMasks[3]=0x1c14L;
		kingMasks[4]=0x3828L;
		kingMasks[5]=0x7050L;
		kingMasks[6]=0xe0a0L;
		kingMasks[7]=0xc040L;
		kingMasks[8]=0x30203L;
		kingMasks[9]=0x70507L;
		kingMasks[10]=0xe0a0eL;
		kingMasks[11]=0x1c141cL;
		kingMasks[12]=0x382838L;
		kingMasks[13]=0x705070L;
		kingMasks[14]=0xe0a0e0L;
		kingMasks[15]=0xc040c0L;
		kingMasks[16]=0x3020300L;
		kingMasks[17]=0x7050700L;
		kingMasks[18]=0xe0a0e00L;
		kingMasks[19]=0x1c141c00L;
		kingMasks[20]=0x38283800L;
		kingMasks[21]=0x70507000L;
		kingMasks[22]=0xe0a0e000L;
		kingMasks[23]=0xc040c000L;
		kingMasks[24]=0x302030000L;
		kingMasks[25]=0x705070000L;
		kingMasks[26]=0xe0a0e0000L;
		kingMasks[27]=0x1c141c0000L;
		kingMasks[28]=0x3828380000L;
		kingMasks[29]=0x7050700000L;
		kingMasks[30]=0xe0a0e00000L;
		kingMasks[31]=0xc040c00000L;
		kingMasks[32]=0x30203000000L;
		kingMasks[33]=0x70507000000L;
		kingMasks[34]=0xe0a0e000000L;
		kingMasks[35]=0x1c141c000000L;
		kingMasks[36]=0x382838000000L;
		kingMasks[37]=0x705070000000L;
		kingMasks[38]=0xe0a0e0000000L;
		kingMasks[39]=0xc040c0000000L;
		kingMasks[40]=0x3020300000000L;
		kingMasks[41]=0x7050700000000L;
		kingMasks[42]=0xe0a0e00000000L;
		kingMasks[43]=0x1c141c00000000L;
		kingMasks[44]=0x38283800000000L;
		kingMasks[45]=0x70507000000000L;
		kingMasks[46]=0xe0a0e000000000L;
		kingMasks[47]=0xc040c000000000L;
		kingMasks[48]=0x302030000000000L;
		kingMasks[49]=0x705070000000000L;
		kingMasks[50]=0xe0a0e0000000000L;
		kingMasks[51]=0x1c141c0000000000L;
		kingMasks[52]=0x3828380000000000L;
		kingMasks[53]=0x7050700000000000L;
		kingMasks[54]=0xe0a0e00000000000L;
		kingMasks[55]=0xc040c00000000000L;
		kingMasks[56]=0x203000000000000L;
		kingMasks[57]=0x507000000000000L;
		kingMasks[58]=0xa0e000000000000L;
		kingMasks[59]=0x141c000000000000L;
		kingMasks[60]=0x2838000000000000L;
		kingMasks[61]=0x5070000000000000L;
		kingMasks[62]=0xa0e0000000000000L;
		kingMasks[63]=0x40c0000000000000L;

	}

	public void move(int pos1, int pos2, byte type){
		long mask1 = 1L << pos1;
		long mask2 = 1L << pos2;

		byte type2= getPieceType(pos2);

		if (type2 !=_EMPTY) {
			pieces[type2&7]^= mask2;
			color[type2>>>3]^=mask2;
		}

		table  = table ^ mask1 | mask2;
		pieces[type&7] ^= mask1 | mask2;//(pieces[type&7] ^ mask1) | mask2;
		color[type>>>3] ^= mask1 | mask2;

		types[pos2]=types[pos1];
		types[pos1]=_EMPTY;
	}

	//	Functie care afiseaza un long ca un bitboard, folosita la testare
	public void printBoard(long n){
		long mask = 1L;
		System.out.println();
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				mask = 1L << 8*i << j;
				if((mask & n)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/********************************************************************************************************
	 *																										*
	 *										Inceput metode de generare mutari								*
	 *																										*
	 ********************************************************************************************************/



	long movesOfWhitePawn(long piece,byte row,byte column){
		long forward = 0L,hostile=0L,oneMove;
		oneMove = piece << 8;
		if((oneMove & table)==0){	// nu este coliziune
			forward |= oneMove;
			if(row==1){
				oneMove = oneMove << 8;
				if((oneMove & table)==0)
					forward |= oneMove;
			}
		}
		if(column<7){	//	nu este pe coloana ultima
			oneMove = piece << 9;
			hostile |= oneMove;
		}
		if(column>0){	//	nu este pe prima coloana
			oneMove = piece << 7;
			hostile |= oneMove;
		}

		hostile = hostile & color[1];	//captura piesa neagra

		if(row==4){	//are potential sa faca en Passant
			if(column==enPassantBlack-1){System.out.println("passant in dreapta");
			oneMove = piece << 9;
			hostile |= oneMove;
			}
			else if(column==enPassantBlack+1){System.out.println("passant in stanga");
			oneMove = piece << 7;
			hostile |= oneMove;
			}
		}

		return hostile | forward;
	}

	long movesOfBlackPawn(long piece,byte row,byte column){
		long forward = 0L,hostile=0L,oneMove;
		oneMove = piece >>> 8;
			if((oneMove & table)==0){	// nu este coliziune
				forward |= oneMove;
				if(row==6){
					oneMove = oneMove >>> 8;
			if((oneMove & table)==0)	//
				forward |= oneMove;
				}
			}
			if(column<7){	//	nu este pe coloana ultima
				oneMove = piece >>> 7;
			hostile |= oneMove;//printBoard(hostile);
			}
			if(column>0){	//	nu este pe pima coloana
				oneMove = piece >>> 9;
		hostile |= oneMove;//printBoard(hostile);
			}

			hostile = hostile & color[0];	//	captura piessa alba

			if(row==3){	//are potential sa faca en Passant
				if(column+1==enPassantWhite){System.out.println("passant in dreapta");
				oneMove = piece >>> 7;
				hostile |= oneMove;
				}
				else if(column-1==enPassantWhite){System.out.println("passant in stanga");
				oneMove = piece >>> 9;
			hostile |= oneMove;
				}
			}

			return hostile | forward;
	}

	long movesOfRook(long piece,byte row,byte column){
		long mask = 0L,oneMove;
		byte i ;
		i = column;
		//	Mutari orizontale spre coloana A
		oneMove = piece;
		while(i>0){
			oneMove = oneMove >>> 1;
			mask |= oneMove;
			if((oneMove & table)==0)
				i--;
			else
				break;
		}
		//	Mutari orizontale spre coloana H
		i = column;
		oneMove = piece;
		while(i<7){
			oneMove = oneMove << 1;
			mask |= oneMove;
			if((oneMove & table)==0)
				i++;
			else
				break;
		}
		//	Mutari verticale spre linia 8
		i = row;
		oneMove = piece;
		while(i<7){
			oneMove = oneMove << 8;
			mask |= oneMove;
			if((oneMove & table)==0)
				i++;
			else
				break;
		}
		//	Mutari verticale spre linia 1
		i = row;
		oneMove = piece;
		while(i>0){
			oneMove = oneMove >>> 8;
		mask |= oneMove;
		if((oneMove & table)==0)
			i--;
		else
			break;
		}

		if((piece & pieces[1])!=0)
			i = 1;	//	piesa este BLACK
		else
			i = 0;	//	piesa este WHITE
		return mask ^ (mask & color[i]);	//se elimina mutarile peste piese proprii
	}

	long movesOfBishop(long piece,byte row,byte column){
		long mask = 0L,oneMove;
		byte minusRow = row,plusRow = (byte)(7 - row);
		byte minusColumn = column,plusColumn = (byte)(7 - column); 

		byte shifts;

		//	Deplasari spre A1
		if(minusRow<minusColumn)
			shifts = minusRow;
		else
			shifts = minusColumn;
		oneMove = piece;
		while(shifts>0){
			oneMove = oneMove >>> 9;
		mask |= oneMove;
		if((oneMove & table)==0)
			shifts--;
		else
			break;
		}
		//	Deplasari spre H1
		if(minusRow<plusColumn)
			shifts = minusRow;
		else
			shifts = plusColumn;
		oneMove = piece;
		while(shifts>0){
			oneMove = oneMove >>> 7;
			mask |= oneMove;
			if((oneMove & table)==0)
				shifts--;
			else
				break;
		}
		//	Deplasari spre A8
		if(plusRow<minusColumn)
			shifts = plusRow;
		else
			shifts = minusColumn;
		oneMove = piece;
		while(shifts>0){
			oneMove = oneMove << 7;
			mask |= oneMove;
			if((oneMove & table)==0)
				shifts--;
			else
				break;
		}
		//	Deplasari spre H8
		if(plusRow<plusColumn)
			shifts = plusRow;
		else
			shifts = plusColumn;
		oneMove = piece;
		while(shifts>0){
			oneMove = oneMove << 9;
			mask |= oneMove;
			if((oneMove & table)==0)
				shifts--;
			else
				break;
		}

		if((piece & pieces[1])!=0)
			shifts = 1;	//	piesa este BLACK
		else
			shifts = 0;	//	piesa este WHITE
		return mask ^ (mask & color[shifts]);	//se elimina mutarile peste piese proprii
	}

	long movesOfKnight(int position){
		byte side = (byte)((types[position] & 8) >> 3);
		return knightMasks[position] ^ (knightMasks[position] & color[side]);
	}

	long movesOfWhiteKing(long piece,int pozitie){
		long mask = kingMasks[pozitie];
		mask = mask ^ (mask & color[0]);	//zone acoperite de rege prin mutari simple

		if(canShortCastleWhite)
			if((shortCastlingWhite & table)==0L)	//Se poate face rocada mica
				mask |= 0x0000000000000040L;
		if(canLongCastleWhite)
			if((table & longCastlingWhite)==0L)	//Se poate face rocada mare
				mask |= 0x0000000000000004L;

		return mask;
	}

	long movesOfBlackKing(long piece,int pozitie){
		long mask = kingMasks[pozitie];
		mask = mask ^ (mask & color[1]);	//zone acoperite de rege prin mutari simple

		if(canShortCastleBlack)
			if((table & shortCastlingBlack)==0L)	//Se poate face rocada mica
				mask |= 0x4000000000000000L;
		if(canLongCastleBlack)
			if((table & longCastlingBlack)==0L)	//Se poate face rocada mare
				mask |= 0x0400000000000000L;

		return mask;
	}

	public long potentialMovesBoard(long piece){
		//	Face selectia intre diferitele piese, pe baza mastii date ca parametru

		int pozitie = Long.numberOfTrailingZeros(piece);
		byte elem = types[pozitie];


		if(elem==W_PAWN) return movesOfWhitePawn(piece,rowPosition[pozitie],columnPosition[pozitie]);
		if(elem==B_PAWN) return movesOfBlackPawn(piece,rowPosition[pozitie],columnPosition[pozitie]);

		if(elem==W_KING) return movesOfWhiteKing(piece,pozitie);
		if(elem==B_KING) return movesOfBlackKing(piece,pozitie);

		elem &= 7;

		switch(elem){
		case W_KNIGHT : return movesOfKnight(pozitie);
		case W_ROOK : return movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie]);
		case W_BISHOP : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie]);
		default : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie]) 
		| movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie]);	
		}
	}

	/*
	 *										Sfarsit metode de generare mutari
	 ********************************************************************************************************
	 ********************************************************************************************************/

	/********************************************************************************************************
	 *																										*
	 *											Metode de analiza mutarilor									*
	 *																										*
	 ********************************************************************************************************/

	long getValidMoves(int position){
		/*
		 *	Intoarce masca de mutari valide pentru piesa aflata in patratul cu numarul de ordine
		 *	dat ca parametru
		 */
		long mask = potentialMovesBoard(1L << position);
		return filterMoveMask(1L << position,mask);
	}

	long filterMoveMask(long start,long possibleMoves){
		/*
		 *	Functia filtreaza matricea de mutari posibile eliminandu-le pe
		 *	acele ce ar lasa regele propriu in sah.
		 */
		long oneMove = Long.highestOneBit(possibleMoves);
		long newMask = 0L;
		while(oneMove!=0L){
			if(isValidMove(start,oneMove))
				newMask |= oneMove;
			possibleMoves ^= oneMove;
			oneMove = Long.highestOneBit(possibleMoves);
		}
		return newMask;
	}

	boolean isValidMove(long start,long end){
		/*
		 *	Metoda modifica tabla pentru a corespunde noii situatii indicate de mutarea data
		 *	ca pozitie initiala - pozitie finala si determina daca noua asezare a pieselor
		 *	este valida din punctul de vedere al pozitiei de sah a regelui propriu.
		 *
		 *	Pentru rege se fac verificari suplimentare in cazul in care mutarea propusa spre
		 *	analiza este rocada.
		 */
		byte elementType = types[Long.numberOfTrailingZeros(start)];
		boolean result = false;
		if((elementType & 7)==W_KING){	//	se analizeaza situatia in care piesa mutata este rege
			if( (start>>>2 == end) && avoidCheckPosition((byte)(elementType>>>3))){	//mutare de rocada
				updateMoveOnBoard(start,start>>>1);
				result = avoidCheckPosition((byte)(elementType>>>3));//nu e in sah pe patratul imediat alaturat
				if(result){
					updateMoveOnBoard(start>>>1,end);
					result = avoidCheckPosition((byte)(elementType>>>3));
					updateMoveOnBoard(end,start);	//	aduce tabla la starea initiala
				}
				else
					updateMoveOnBoard(start>>>1,start);//	aduce tabla la starea initiala
			}
			else if( (start<<2 == end) && avoidCheckPosition((byte)(elementType>>>3))){	//mutare de rocada
				updateMoveOnBoard(start,start<<1);
				result = avoidCheckPosition((byte)(elementType>>>3));//nu e in sah pe patratul imediat alaturat
				if(result){
					updateMoveOnBoard(start<<1,end);
					result = avoidCheckPosition((byte)(elementType>>>3));
					updateMoveOnBoard(end,start);	//	aduce tabla la starea initiala
				}
				else
					updateMoveOnBoard(start<<1,start);//	aduce tabla la starea initiala
			}
			else{
				updateMoveOnBoard(start,end);
				result = avoidCheckPosition((byte)(elementType>>>3));
				updateMoveOnBoard(end,start);
			}
				
		}
		else{	//	se analizeaza mutarea unei piese diferite de rege
			elementType = types[Long.numberOfTrailingZeros(end)];
			updateMoveOnBoard(start,end);
			if((color[0] & end)!=0)	//se studiaza validitatea mutarii WHITE
				result = avoidCheckPosition((byte)0);
			else
				result = avoidCheckPosition((byte)1);
			updateMoveOnBoard(end,start);
			if(elementType != _EMPTY)
				restorePieceOnBoard(end,elementType);
		}	
		return result;
	}

	boolean avoidCheckPosition(byte defendingSide){
		/*	
		 *	Metoda intoarce true daca regele jucatorului(culoarea) dat ca parametru nu se
		 *	afla in sah si false in caz contrar.
		 */
		long allAttackingPieces = color[((defendingSide+1)&1)];
		long piece,king = color[defendingSide] & pieces[6];


		piece = Long.highestOneBit(allAttackingPieces);
		while(piece!=0L){

			if((potentialMovesBoard(piece)&king)!=0L)	//sah
				return false;
			allAttackingPieces ^= piece;
			piece = Long.highestOneBit(allAttackingPieces);
		}
		return true;
	}

	boolean isCheckMate(byte defendingSide){
		/*
		 *	Functia determina daca regele de culoare data ca parametru este in mat
		 */

		long allPieces = color[defendingSide];
		long onePiece = Long.highestOneBit(allPieces);
		while(onePiece!=0L){
			if(getValidMoves(Long.numberOfTrailingZeros(onePiece))!=0L)	//	exista mutari ce nu lasa regele propriu in sah
				return false;
			allPieces ^= onePiece;
			onePiece = Long.highestOneBit(allPieces);
		}
		return true;

	}

	/*
	 *										Sfarsit metode de analiza a mutarilor
	 ********************************************************************************************************
	 ********************************************************************************************************/
	/********************************************************************************************************
	 *																										*
	 *											Board update methods										*
	 *																										*
	 ********************************************************************************************************/

	public void boardIndicatorsUpdate(long start,long end){
		/*
		 *	Metoda modifica indicatorii tablei de sah in conformitate cu mutarea curenta(mutare deja efectuata):
		 *		-	enPassantWhite,enPassantBlack daca se muta pioni
		 *		-	canLongCastleWhite,canLongCastleBlack,canShortCastleWhite,canShortCastleBlack daca se
		 *				muta regi,ture sau regele advers este in sah
		 *		
		 */

		byte elementType = getPieceType(Long.numberOfTrailingZeros(start));
		enPassantBlack = 9;
		enPassantWhite = 9;

		switch(elementType){
		case W_PAWN : {
			if(start<<16 == end)//mutare initiala de 2 patrate
				enPassantWhite = columnPosition[Long.numberOfTrailingZeros(start)];
			break;
		}
		case B_PAWN : {
			if(start>>>16 == end)//mutare initiala de 2 patrate
				enPassantBlack = columnPosition[Long.numberOfTrailingZeros(start)];
			break;
		}
		case W_ROOK : {
			if(start==1L){	//	mutarea turei de la A1
				canLongCastleWhite = false;
				break;
			}else if(start==0x0000000000000080L){	//mutarea turei de la H1
				canShortCastleWhite = false;
				break;
			}
		}
		case B_ROOK : {
			if(start==0x0100000000000000L){	//	mutarea turei de la A8
				canLongCastleBlack = false;
				break;
			}else if(start==0x8000000000000000L){
				canShortCastleBlack = false;
				break;
			}
		}
		case W_KING : {
			canLongCastleWhite = false;
			canShortCastleWhite = false;
			break;
		}
		case B_KING : {
			canLongCastleBlack = false;
			canShortCastleBlack = false;
			break;
		}
		}

		if(((getColor(start) + 1 ) & 1) == 0){//piesele adverse sunt WHITE
			if(!avoidCheckPosition((byte)0)){	//	piesele albe sunt in sah
				canShortCastleWhite = false;
				canLongCastleWhite = false;
			}
		}
		else{//piesele adverse sunt BLACK
			if(!avoidCheckPosition((byte)1)){	//	piesele negrele sunt in sah
				canShortCastleBlack = false;
				canLongCastleBlack = false;
			}
		}

	}

	public void restorePieceOnBoard(long piece,byte elementType){
		/*
		 *	Repune o piesa pe tabla ( metoda folosita laolalta cu updateMoveOnBoard de catre
		 *	isValidMove pentru a reface pozitiile tablei de sah inainte de mutarea
		 *	analizata)
		 */
		pieces[elementType & 7] |= piece;	//modificare tabla piese
		color[elementType >>> 3] |= piece;	//modificare tabla culori
		table |= piece;
		types[Long.numberOfTrailingZeros(piece)] = elementType;
	}

	public void promotionUpdate(long piece,byte type){
		/*
		 *	Update-ul se face doar dupa ce pionul este mutat pe pozitia finala.
		 *
		 *	Face promovarea unui pion in type; daca nu vreti sa va pese de culoare
		 *	(sa nu faceti distinctie W_QUEEN/B_QUEEN) pot sa ma ocup aici de asta.
		 */
		type &= 7;
		pieces[type] |= piece;	//	aduagarea noii piese la tabla de piese de tip propriu
		if((color[0]&piece)==0) //	piesa BLACK
			type |= 8;

		pieces[1] ^= piece;	//	eliminarea pionului
		types[Long.numberOfTrailingZeros(piece)] = type;//	adaugarea pe tabla de tipuri
	}

	public void updateMoveOnBoard(long start,long end){
		//move(Long.numberOfTrailingZeros(start), Long.numberOfTrailingZeros(end));
		/*
		 *	Modifica board-ul in conformitate cu mutarea data:
		 *		table,color[0],color[1],type[] si pieces[]
		 */
		
		byte side = getColor(start);
		byte oppositeSide = (byte)((side+1) & 1);
		byte elementType;// = getPieceType(position);

		//update-ul intregii placi de sah
		table ^= start;
		table |= end;
		//update-ul placii de culoare
		color[side] ^= start;
		color[side] |= end;	
		//update-ul placii de culoare adversa
		if((color[oppositeSide] & end)!=0L)	{//este captura
			//update placa de culoare opusa
			color[oppositeSide] ^= end;
			//update placa de piese
			elementType = (byte)(getPieceType(Long.numberOfTrailingZeros(end)) & 7);
			pieces[elementType] ^= end; 
		}
													
		//update tabla de coduri
		elementType = getPieceType(Long.numberOfTrailingZeros(start));
		types[Long.numberOfTrailingZeros(end)] = elementType;
		types[Long.numberOfTrailingZeros(start)] = _EMPTY;
		//update tabla piesa ce muta
		elementType &= 7;
		pieces[elementType] ^= start;
		pieces[elementType] |= end;
		 
	}

	public void updateBoard(long start,long end,byte promotion){
		/*
		 *	Actualizeaza tabla de sah efectuand mutarea data ca parametru dar si alte
		 *	mutari implicite ce sunt asociate cu aceasta: rocada, enPassant, posibilitate
		 *	de a face rocade, etc.
		 *
		 *	Intentionez sa primesc promotion ca una din piese albe/negre si sa o aplic doar
		 *	daca mutarea o necesita. Implicit ar trebui sa primesc zero la promotion
		 */

		int number = Long.numberOfTrailingZeros(start);
		byte elementType = getPieceType(number);
		long extra = 0L;
		switch(elementType){
		case W_PAWN : {
			if(((end == start << 7) || (end == start << 9))&&((end & table)==0L)){	
				// este mutare de captura dar nu este piesa la destinatie(deci este enPassant)

				System.out.println("En Passant");

				extra = end>>8;	//	pozitia pionului capturat
			pieces[1] ^= extra;
			color[1] ^= extra;
			types[Long.numberOfTrailingZeros(extra)] = _EMPTY;

			updateMoveOnBoard(start,end);
			boardIndicatorsUpdate(start,end);
			break;
			}
			if(rowPosition[Long.numberOfTrailingZeros(end)]==7){	//	este in situatie de a face promovare
				updateMoveOnBoard(start,end);

				//pentru a aplica promotionUpdate este nevoie ca la destinatie sa fie pion propriu
				promotionUpdate(end,(byte)(promotion&7));
				boardIndicatorsUpdate(start,end);
			}
			else{
				updateMoveOnBoard(start,end);
				boardIndicatorsUpdate(start,end);

			}
			break;
		}
		case B_PAWN : {
			if(((end == start >>> 7) || (end == start >>> 9))&&((end & table)==0L)){ 

				extra = end<<8;
				pieces[0] ^= extra;
				color[0] ^= extra;
				types[Long.numberOfTrailingZeros(extra)] = _EMPTY;

				updateMoveOnBoard(start,end);
				boardIndicatorsUpdate(start,end);
				break;
			}
			if(rowPosition[Long.numberOfTrailingZeros(end)]==0){	//	este in situatie de a face promovare
				updateMoveOnBoard(start,end);
				promotionUpdate(end,(byte)(promotion|8));
				boardIndicatorsUpdate(start,end);
			}
			else{
				updateMoveOnBoard(start,end);
				boardIndicatorsUpdate(start,end);
			}
			break;
		}
		case W_KING : {
			if(start<<2 == end)	//rocada mica
				updateMoveOnBoard(start<<3,start<<1);//	se muta tura corespunzator
			else if(start>>2 == end)	//rocada nare
				updateMoveOnBoard(start>>>4,start>>>1);//	se muta tura corespunzator
			updateMoveOnBoard(start,end);
			boardIndicatorsUpdate(start,end);
			break;
		}
		case B_KING : {
			if(start<<2 == end)	//rocada mica
				updateMoveOnBoard(start<<3,start<<1);//	se muta tura corespunzator
			else if(start>>2 == end)	//rocada nare
				updateMoveOnBoard(start>>>4,start>>>1);//	se muta tura corespunzator
			updateMoveOnBoard(start,end);
			boardIndicatorsUpdate(start,end);
			break;
		}
		default:{
			updateMoveOnBoard(start,end);
			boardIndicatorsUpdate(start,end);
		}
		}


	}
	public void updateBoard(int poz1, int poz2, byte promotion){
		updateBoard(1L<<poz1,1L<<poz2,promotion);
	}

	/*
	 *										Sfarsit metode de update al baordului
	 ********************************************************************************************************
	 ********************************************************************************************************/

	public String futureGetNextMove(byte side){
		/*
		 *	Functia primeste ca parametru culoarea si alege o mutare care ii este
		 *	convenabila, urmand sa o transforme in SAN prin apelul unei functii
		 *	corespunzatoare din clasa definita de Emma
		 */

		long optimumStart = 0L;
		long optimumEnd = 0L;
		boolean isCheck,isCapture;
		byte castling = 0;	//1 daca este rocada mica, 2 daca este rocada mare
		byte elementType = 0;

		/*{
		 *	Un cod care apeleaza convenabil o functie astfel incat sa intoarca mutarea
		 *	cea mai onvenabila... ne vom love de el la minimx si la tot felul de optimizari
		 *	asa ca nu vreau sa scriu asta deocamdata, dar voi presupune ca in urma
		 *	prelucrarilor functia va considera drept solutie optima mutarea 
		 *		long optimumStart,long optimumEnd
		 *
		 *	Inainte sa scrie careva ceva aici(mai ales Vali) as sugera ca functia asta sa
		 *	semene ca structura cu functia recursiva... Din nou, scopul sugestiei este de a va
		 *	impiedica sa scrieti cod inainte de a discuta implementarea.
		 *
		 *}
		 */
		if((elementType | 7) == W_KING){
			if(optimumEnd == optimumStart << 2)	//	rocada mica
				castling = 1;
			else if(optimumEnd == optimumStart >>> 2)	//	rocada mare
				castling = 2;
		}
		else{
			elementType = getPieceType(Long.numberOfTrailingZeros(optimumEnd));
			updateMoveOnBoard(optimumStart,optimumEnd);
			isCheck = !avoidCheckPosition((byte)((side+1)&1));	//	verifica daca adversarul este in sah
			updateMoveOnBoard(optimumEnd,optimumStart);
			restorePieceOnBoard(optimumEnd,elementType);

			if(elementType!=0)	//	captura
				isCapture = true;
		}
		return "La Asta va fi ceva de lucru";
	}



	public String emmasToSAN(byte elementType,long endPosition,byte row,byte column,
			boolean checkPosition,boolean checkMate,boolean capture,byte castlingType,byte promotion){

		/*
		 *	
		 *	
		 *
		 *	Functia scrie traduce in SAN o mutare, conform parametrilor de apel; este posibil sa o introduc
		 *	in functia apelanta (intermediaryToSANMove) pentru ca primeste prea multi parametrii
		 *
		 *	elementType este de fapt unul din tipurile de piese de la 1 la 6, corespunzator mastilor
		 */

		StringBuffer sb = new StringBuffer();
		if(castlingType == 1)
			sb.append("O-O");
		else if(castlingType == 2)
			sb.append("O-O-O");
		else{
			if(elementType != W_PAWN)
				sb.append(pieceForSAN[elementType]);
			if(column<8)
				sb.append(columnChar[column]);
			if(row<8)
				sb.append(rowChar[row]);
			if(capture)
				sb.append("x");

			sb.append(columnChar[columnPosition[Long.numberOfTrailingZeros(endPosition)]]);
			sb.append(rowChar[rowPosition[Long.numberOfTrailingZeros(endPosition)]]);
			if(promotion!=0){
				sb.append("=");
				sb.append(pieceForSAN[promotion]);
			}
		}
		if(checkMate)
			sb.append("#");
		else if(checkPosition)
			sb.append("+");

		return sb.toString();
	}

	public String intermediaryToSANMove(long start,long end){
		/*
		 *	Functie intermediara care momentan calculeaza parametrii necesari crearii codificarii
		 *	SAN.
		 *	
		 *	Este posibil sa unesc functia asta cu functia propriu-zisa de generare a codificarii SAN
		 *	prezentata mai sus.
		 */


		byte castlingType = 0;
		boolean isCapture = false;
		boolean checkPosition = false;
		boolean checkMate = false;

		byte row = 9,column = 9,extra = 9;

		byte promotion = 0;
		byte elementType = getPieceType(Long.numberOfTrailingZeros(start));	//	piesa mutata
		byte side = (byte)(elementType >>> 3);	//	culoarea piesei mutate
		elementType = (byte)(elementType & 7);	//	tipul piesei

		if(elementType == W_KING){ //piesa este rege
			if((start >> 2) == end)	//rocada mare
				castlingType = (byte)2;
			else if((start << 2) == end)	//	rocada mica
				castlingType = (byte)1;
		}
		else{	//orice alta piesa
			byte number,initialRow,initialColumn;
			long allPieces = color[side] & pieces[elementType];
			long onePiece = Long.highestOneBit(allPieces);

			//	calculul destinatiei in termeni de coordonate carteziene
			number = (byte)Long.numberOfTrailingZeros(end);
			byte endRow = rowPosition[number];
			byte endColumn = columnPosition[number];

			if(elementType == W_PAWN)	//	daca este pion
				if(endRow == 0 || endRow==7)	//	pe ultimul rand
					promotion = W_QUEEN;

			while(onePiece != 0L){	//	mai exista piese de acest tip
				if(onePiece != start){
					number = (byte)Long.numberOfTrailingZeros(onePiece);
					if((getValidMoves(number) & end)!=0L){	//	mutari suprapuse
						if(columnPosition[number] == endColumn)	//	aceeasi coloana
							column = endColumn;
						else if(rowPosition[number] == endRow)	//	aceeasi linie
							row = endRow;
						else 
							extra = endColumn;
					}
				}
				allPieces ^= onePiece;
				onePiece = Long.highestOneBit(allPieces);
			}
		}

		if((end & table)!=0L){
			isCapture = true;
			if(elementType == W_PAWN)
				column = columnPosition[Long.numberOfTrailingZeros(start)];
		}

		//	Se face update-ul boardului in conformitate cu mutarea curenta
		updateBoard(start,end,(byte)(W_QUEEN | (side << 3)));
		//	Pentru noua configuratie a tablei de sah se determina daca adversarul e in sah
		checkPosition = !avoidCheckPosition((byte)((side+1)&1));

		//	adversarul este in sah-mat
		checkMate = isCheckMate((byte)((side+1)&1));

		if(extra<9)
			column = extra;

		return emmasToSAN(elementType,end,row,column,checkPosition,checkMate,isCapture,castlingType,promotion);

	}

	public String getSANMove(long start,long end){
		/*
		 *	Veche functie de conversie a mutarii pionilor in SAN
		 */
		int numberStart = Long.numberOfTrailingZeros(start);
		int numberEnd = Long.numberOfTrailingZeros(end);

		byte type = getPieceType(numberStart);
		byte oppositeSide = (byte)((getColor(start)+1) & 1);

		StringBuilder sb = new StringBuilder();
		switch(type){
		case W_PAWN : {
			if((end & color[oppositeSide])!=0){//captura piesa
				sb.append(columnChar[columnPosition[numberStart]]);
				sb.append('x');
			}

			sb.append(columnChar[columnPosition[numberEnd]]);
			sb.append(rowChar[rowPosition[numberEnd]]);
			if(rowPosition[numberEnd]==7)
				sb.append("=Q");
			return sb.toString();
		}
		case B_PAWN : {
			if((end & color[oppositeSide])!=0){//captura piesa
				sb.append(columnChar[columnPosition[numberStart]]);
				sb.append('x');
			}

			sb.append(columnChar[columnPosition[numberEnd]]);
			sb.append(rowChar[rowPosition[numberEnd]]);
			if(rowPosition[numberEnd]==0)
				sb.append("=Q");
			return sb.toString();
		}
		default: return " ";
		}
	}

	public String nextMove(byte side){
		/*
		 *	Functie folosita pentru a selecta mutarea urmatoare(cel putin pentru etapa 1);
		 *	Functia intoarce un string cu mutarea transpusa in SAN
		 *
		 *	mutarea se va lua din SAN dupa un apel de forma
		 *			
		 *		toSan(int sursa,int destinatie,boolean isCheck,boolean isCapture,byte castling); 
		 *
		 */

		long piece = color[side] & pieces[1];
		String moveCode = "";

		boolean isCheck=false,isCapture=false;
		byte rocada = 0;


		long endPosition;
		int number;
		if(side==0)	{//WHITE
			piece = Long.highestOneBit(piece);

			if(piece==0L)
				return "resign";

			number = Long.numberOfTrailingZeros(piece);
			endPosition = getValidMoves(number);
			endPosition = Long.highestOneBit(endPosition);

			if(endPosition==0L)
				return "resign";

			moveCode = intermediaryToSANMove(piece,endPosition);
			return "move " + moveCode;
		}
		else{		//Black
			piece = Long.lowestOneBit(piece);

			if(piece==0L)
				return "resign";

			number = Long.numberOfTrailingZeros(piece);
			endPosition = getValidMoves(number);
			endPosition = Long.lowestOneBit(endPosition);

			if(endPosition==0L)
				return "resign";

			moveCode = intermediaryToSANMove(piece,endPosition);
			return "move " + moveCode;
		}

	}

	//	
	//.............................................................................................

	void printBoard() {
		// W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6;
		char type[]={'-','p','r','n','b','q','k','-','-','P','R','N','B','Q','K'};
		String chars=" abcdefgh"; 
		System.out.print("\t");
		for (int i=1;i<=8;i++)
			System.out.print(chars.charAt(i)+"\t");
		System.out.println();
		for (int i=7;i>=0;i--) {
			System.out.print((i+1)+"\t");
			for (int j=0;j<8;j++)
				System.out.print(type[getPieceType(i<<3|j)]+"\t");
			System.out.println();
		}	
	}

	public boolean check;
	public boolean checkmate;
	public byte promotion; //retine tipul piesei la care a fost promovat pionul
	public boolean captured;
	public byte piece_type; //retine tipul piesei care a fost mutata
	public byte castling; //retine tipul piesei pe partea careia se face rocada
	public int pos1,pos2;

	// functia care interpreteaza mutarea data de adversar in SAN
	public void SAN(String mutare, int clr){
		int lin = -1, col = -1;
		check = false;
		checkmate = false;
		promotion = _EMPTY;
		captured = false;
		piece_type = _EMPTY;
		castling = _EMPTY;
		pos1 = -1; pos2 = -1;
		if (mutare.equals("O-O")){
			castling = W_KING; //rocada pe partea regelui
			pos1 = 4; pos2 = 6;
			if (clr == 1)
				castling = B_KING;
			pos1 = 60; pos2 = 62;
			return;
		}
		if (mutare.equals("O-O-O")){
			castling = W_QUEEN; //rocada pe partea reginei
			pos1 = 4; pos2 = 2;
			if (clr == 1)
				castling = B_QUEEN;
			pos1 = 60; pos2 = 58;
			return;
		}
		int i = mutare.length()-1;
		if (mutare.charAt(i)=='#'){
			checkmate = true;
			i--;
		}
		if (mutare.charAt(i)=='+'){
			check = true;
			i--;
		}
		if(mutare.indexOf("=")>0){
			promotion = (byte)"  RNBQ".indexOf(mutare.charAt(mutare.indexOf("=")+1));
			mutare = mutare.substring(0,i-2);
			i = i - 2;
		}
		pos2 = Usual.position(mutare.substring(i-1));
		i = i - 2;
		if (mutare.indexOf("x")>=0){
			captured = true;
			i = mutare.indexOf("x") - 1;
		}
		if (i>=0  && Character.isDigit(mutare.charAt(i))){
			lin = mutare.charAt(i)-'1';
			i-- ;
		}
		if (i>=0 && "abcdefgh".indexOf(mutare.charAt(i))>=0){
			col = mutare.charAt(i)-'a';
			i--;
		}
		if (i>=0 && "  RNBQK".indexOf(mutare.charAt(i))>=0)
			piece_type = (byte)"  RNBQK".indexOf(mutare.charAt(i));
		else 
			piece_type = 1;
			
															
		if (col != -1 && lin != -1){
			pos1 = lin*8 + col;
			return;
		}
		if (col != -1 && lin == -1){
			long bit = pieces[piece_type]&color[clr]; 
			long col1 = 0x0101010101010101L << col;
			bit &= col1;
			long firstb;
			int poz;
			if (clr == 1)
				piece_type |= 8;
			while (bit != 0L){
				firstb = Long.highestOneBit(bit);
				bit ^= firstb;
				poz = Long.numberOfTrailingZeros(firstb);
				if ((getValidMoves(poz)&(1L<<pos2))!= 0) {
					pos1 = poz;
					break;
				}
			}
			return;
		}
												
		if (col == -1 && lin != -1) {
			long bit = pieces[piece_type]&color[clr];
			long lin1 = 0xFFL << lin*8;
			bit &= lin1;
			long firstb;
			int poz;
			if (clr == 1)
				piece_type |= 8;
			while (bit != 0L){
				firstb = Long.highestOneBit(bit);
				bit ^= firstb;
				poz = Long.numberOfTrailingZeros(firstb);
				if ((getValidMoves(poz)&(1L<<pos2))!= 0) {
					pos1 = poz;
					break;
				}
			}
			return;
		}
													
		if (col == -1 && lin == -1){
			long bit = pieces[piece_type]&color[clr];
			long firstb;
			int poz;
			if (clr == 1)
				piece_type |=8;
			while (bit != 0L) {
				firstb = Long.highestOneBit(bit);		
				bit ^= firstb;
				poz = Long.numberOfTrailingZeros(firstb);
				if ((getValidMoves(poz)&(1L<<pos2))!= 0) {
					pos1 = poz;
					break;
				}
			}
		}

	}

}