import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//Asta random este doar de test
import java.util.Random;


public class Board {
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
	public static final byte columnPosition[] = new byte[64];
	public static final byte rowPosition[] = new byte[64]; 
	private byte enPassantWhite = 9;
	private byte enPassantBlack = 9;
	
	byte types[];
	
	long[] attacksTo;
	long[] attacksFrom;
	
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
		int pos=0;
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++){
				columnPosition[pos] = (byte)j;
				rowPosition[pos] = (byte)i;
				pos++;
			}
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
	
	long movesOfWhiteKing(long piece,byte row,byte column){
		return 0L;
		//	TO DO
	}
	
	long movesOfBlackKing(long piece,byte row,byte column){
		return 0L;
		//	TO DO
	}
	
	long movesOfRook(long piece,byte row,byte column){
		return 0L;
		//	TO DO
	}
	
	long movesOfKnight(int position){
		return 0L;
		//	TO DO
	}
	
	long movesOfBishop(long piece,byte row,byte column){
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
		
		enPassantWhite = 7;
		table = 0xFFFF000080205FFFL;
		color[0] = 0x0000000080205FFFL;
		printBoard(color[0]);
		printBoard(movesOfBlackPawn(piece,(byte)i,(byte)j));
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
		Board brd=new Board();
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

