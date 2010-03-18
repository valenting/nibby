import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *	In aceasta forma revizuita in care mutarile unei piese sunt verificate daca lasa
 *	sau nu regele in sah, mutarile nu au fost verificate complet in practica
 *	
 *	Este posibil sa fie anumite scapari la anumite functii desi asta va mai avea nevoie
 *	de niste teste intensive.
 *
 *	Pentru pioni, varianta curenta este suficienta, dar pentru alte piese, ar trebui revazut
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
	private boolean canCastleWhite = true;
	private boolean canCastleBlack = true;
	public static char columnChar[] = {'A','B','C','D','E','F','G','H'};
	public static char rowChar[] = {'1','2','3','4','5','6','7','8'};
	
	public static final long longCastlingWhite = 0x00000000000000CL;
	public static final long shortCastlingWhite = 0x0000000000000060L;
	public static final long longCastlingBlack = 0xC00000000000000L;
	public static final long shortCastlingBlack = 0x6000000000000000L;
	
	byte types[];
	
	long[] attacksTo;
	long[] attacksFrom;
	
	public Board(){
		// la Start:

		/* 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 */ 
		/* h8 ->> a8 | h7 ->> a7 | ...                                                       | h1 ->> a1 */
		
		table = 0xFFFF00000000FFFFL;
		
		color[0] = 0x000000000000FFFFL; //white
		color[1] = 0xFFFF000000000000L; //black
		
		pieces[1] = 0x00FF00000000FF00L; // pawn
		pieces[2] = 0x8100000000000081L; // rook
		pieces[3] = 0x4200000000000042L;   // knight
		pieces[4] = 0x2400000000000024L;   // bishop
		pieces[5] = 0x0800000000000008L;//Usual.boardMask("d1") | Usual.boardMask("d8");  // queen
		pieces[6] = 0x1000000000000010L;//Usual.boardMask("e1") | Usual.boardMask("e8");  // king	
		
		types=initTypes();
		
		//	Adaugat de Andrei - preferabil sa nu se initialize astea statice aici
		//generateStatic();
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
			color[type2>>>3]^=mask2;
		}
		
		table  = table ^ mask1 | mask2;
		pieces[type&7] ^= mask1 | mask2;//(pieces[type&7] ^ mask1) | mask2;
		color[type>>>3] ^= mask1 | mask2;
	
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
			move = Moves.Pawns[type>>>3][pos1];

			// Modify!
			if (type==W_PAWN){
				if ((1L<<(pos1+8)&table)!=0L)
					move=0L;
			}
			else
				if ((1L<<(pos1-8)&table)!=0L)
					move=0L;

			move &= ~table;
			move |= (Moves.PawnTakes[type>>>3][pos1] & (color[(type>>>3) ^ 1]));
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
	

	//transpunere a unei mutari primite din SAN in forma (poz_initiala, poz_finala) + informatii despre starea jocului
	public boolean check;
	public boolean checkmate;
	public byte promotion; //retine tipul piesei la care a fost promovat pionul
	public boolean captured;
	public byte piece_type; //retine tipul piesei care a fost mutata
	public byte castling; //retine tipul piesei pe partea careia se face rocada
	public int pos1,pos2;
	
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
				if ((getValidMoves(poz, piece_type)&(1L<<pos2))!= 0) {
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
				if ((getValidMoves(poz, piece_type)&(1L<<pos2))!= 0) {
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
				if ((getValidMoves(poz, piece_type)&(1L<<pos2))!= 0) {
					pos1 = poz;
					break;
				}
			}
		}
		
	}
	
	
}

