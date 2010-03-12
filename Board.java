import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//Asta random este doar de test
import java.util.Random;


public class Board2 {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6; // CONSTANTE ALB
	public static final byte           B_PAWN=9, B_ROOK=10, B_KNIGHT=11, B_BISHOP=12, B_QUEEN=13, B_KING=14; // CONSTANTE NEGRU
	private long table;
	//private long black;
	//private long white;
	
	private long[] pieces = new long[7]; // pieces[1] = pion, pieces[2] = rook... etc 
	private long[] color = new long[2]; // white.. black
	
	//	Variabile adaugate de Andrei columnPosition,rowPostion - ca sa nu mai fac calcule
	//	de asemenea enPassantWhite - pe ce coloana se poate face captura en passant la 
	//	piesa White si analog enPassantBlack
	public static long knightMasks[] = new long[64];
	public static final byte columnPosition[] = new byte[64];
	public static final byte rowPosition[] = new byte[64]; 
	private byte enPassantWhite = 9;
	private byte enPassantBlack = 9;
	
	byte types[];
	
	long[] attacksTo;
	long[] attacksFrom;
	
	public Board2(){
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
	
	public void move(int pos1, int pos2, byte type){
		long mask1 = 1L << pos1;
		long mask2 = 1L << pos2;
		
		byte type2= getPieceType(pos2);
		
		if (type2 !=_EMPTY) {
			pieces[type2&7]^= mask2;
			color[type2>>3]^=mask2;
		}
		
		table  = table ^ mask1 | mask2;
		pieces[type&7] ^= mask1 | mask2;//(pieces[type&7] ^ mask1) | mask2;
		color[type>>3] ^= mask1 | mask2;
	
		types[pos2]=types[pos1];
		types[pos1]=_EMPTY;
	}
	
	public boolean isValidMove(int pos1,int pos2, byte type){
		if (( getValidMoves(pos1,type) & (1L << pos2)) != 0L)
			return true;
		return false;
	}
	
	public long getValidMoves(int pos1, byte type){
		long move=Moves.all[type][pos1];
		if (type==W_PAWN || type==B_PAWN){
			//move = 1L << pos1;
			move = Moves.Pawns[type>>3][pos1];

			// Modify!
			if (type==W_PAWN){
				if ((1L<<(pos1+8)&table)!=0L)
					move=0L;
			}
			else
				if ((1L<<(pos1-8)&table)!=0L)
					move=0L;

			move &= ~table;
			move |= (Moves.PawnTakes[type>>3][pos1] & (color[(type>>3) ^ 1]));
		}
		Usual.printBoard(move);
		return move;
	}
	
	public void move(int pos1, int pos2){
		this.move(pos1, pos2, this.getPieceType(pos1));
	}
	
	public boolean isValidMove(int pos1, int pos2){
		return isValidMove(pos1, pos2, this.getPieceType(pos1));
	}
	//.............................................................................................
	/*
	 *	Functii adaugate de Andrei (Observati cum vorbesc despre mine la persoana
	 *	a treia!).
	 *
	 */
	
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
	}
	
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
	
	long movesOfWhitePawn(long piece,byte row,byte column){
		long forward = 0L,hostile=0L,oneMove;
		oneMove = piece << 8;
		if((oneMove & table)==0){	// nu este coliziune
			forward |= oneMove;
			if(row==1){
				oneMove = oneMove << 8;
				if((oneMove & table)==0)	//
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
			hostile |= oneMove;printBoard(hostile);
		}
		if(column>0){	//	nu este pe pima coloana
			oneMove = piece >>> 9;
			hostile |= oneMove;printBoard(hostile);
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
			mask += oneMove;
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
			mask += oneMove;
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
			mask += oneMove;
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
			mask += oneMove;
			if((oneMove & table)==0)
				i--;
			else
				break;
		}
		
		if((piece & pieces[1])!=0)
			i = 1;	//	piesa este BLACK
		else
			i = 0;	//	piesa este WHITE
		return mask - (mask & color[i]);	//se elimina mutarile peste piese proprii
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
			mask += oneMove;
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
			oneMove = oneMove >>> 7;//printBoard(oneMove);
			mask += oneMove;
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
			mask += oneMove;
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
			mask += oneMove;
			if((oneMove & table)==0)
				shifts--;
			else
				break;
		}
		
		if((piece & pieces[1])!=0)
			shifts = 1;	//	piesa este BLACK
		else
			shifts = 0;	//	piesa este WHITE
		return mask - (mask & color[shifts]);	//se elimina mutarile peste piese proprii
	}
	
	long movesOfKnight(int position){
		byte side = (byte)((types[position] & 8) >> 3);
		return knightMasks[position] - (knightMasks[position] & color[side]);
	}
	
	long movesOfWhiteKing(long piece,byte row,byte column){
		return 0L;
		//	TO DO
	}
	
	long movesOfBlackKing(long piece,byte row,byte column){
		return 0L;
		//	TO DO
	}
	
	public long potentialMovesBoard(long piece){
		//	Face selectia intre diferitele piese
		
		int pozitie = Long.numberOfTrailingZeros(piece);
		byte elem = types[pozitie];
		
		
		if(elem==W_PAWN) return movesOfWhitePawn(piece,rowPosition[pozitie],columnPosition[pozitie]);
		if(elem==B_PAWN) return movesOfBlackPawn(piece,rowPosition[pozitie],columnPosition[pozitie]);
		
		if(elem==W_KING) return movesOfWhiteKing(piece,rowPosition[pozitie],columnPosition[pozitie]);
		if(elem==B_KING) return movesOfBlackKing(piece,rowPosition[pozitie],columnPosition[pozitie]);
		
		elem &= 7;
		
		switch(elem){
			case W_KNIGHT : return movesOfKnight(pozitie);
			case W_ROOK : return movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie]);
			case W_BISHOP : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie]);
			default : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie]) 
								| movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie]);	
		}
	}
	
	void testare(){
		
		
		int i=3,j=6;	//linie si coloana
		long piece = 1L <<8*i<<j;
		printBoard(piece);
		color[0] |= piece;
		
		enPassantWhite = 7;
		//table = 0xFFFF000080205FFFL;
		//color[0] = 0x0000000080205FFFL;
		printBoard(color[0]);
		printBoard(movesOfKnight(Long.numberOfTrailingZeros(piece)));
		
		
		
	}
	
	//						Aici se termina ce a implementat Andrei
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

	void attacks() {
		long[] attack1= new long[64];
		for (int i=0;i<64;i++)
			attack1[i]=getValidMoves(i, getPieceType(i)) & table & ~color[getPieceType(i)>>3];
		attacksTo=attack1;
		
	}
	
	public static void main(String args[]){
		Board2 brd=new Board2();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String move="";
		
		brd.testare();
		
		/*
		while (true){
			brd.printBoard();
			try {
				move=in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (brd.isValidMove(Usual.pos1(move),Usual.pos2(move)))
					brd.move(Usual.pos1(move),Usual.pos2(move));
			else 
				System.out.println("NotValid");
		
		}
		*/
	}
}

