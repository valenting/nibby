
public class Board {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=3, W_KNIGHT=5, W_BISHOP=7, W_QUEEN=9, W_KING=11; // CONSTANTE ALB
	public static final byte           B_PAWN=2, B_ROOK=4, B_KNIGHT=6, B_BISHOP=8, B_QUEEN=10, B_KING=12; // CONSTANTE NEGRU
	private long table;
	//private long white;
	private long black;
	private long pawns;
	long kings;
	long queens;
	long rooks;
	long bishops;
	long knights;
	public Board(){
		// la Start:

		/* 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 */ 
		/* a8 ->> h8 | a7 ->> h7 | ...                                                       | a1 ->> h1 */


		table = 0xFFFF00000000FFFFL;

		pawns   = 0x00FF00000000FF00L;
		rooks   = 0x8100000000000081L;
		knights = 0x4200000000000042L;
		bishops = 0x2400000000000024L;
		//orientation dependent
		kings   = 0x0800000000000008L;
		queens  = 0x1000000000000010L;

		//white   = 0x00000000000000FFL;
		black   = 0xFFFF000000000000L;

	}

	/* i (linia) - 1 .. 8 */
	/* j (coloana) - 1.. 8 */
	public byte getPieceType(int i,int j){
		byte type=0;

		long mask = 128L >> (j-1) << 8*(i-1); //??needs checked ??//

		if ((mask & table)==0L)
			return _EMPTY;
		if ((mask & pawns)!=0L) {
			type=W_PAWN;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		if ((mask & rooks)!=0L) {
			type=W_ROOK;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		if ((mask & bishops)!=0L) {
			type=W_BISHOP;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		if ((mask & kings)!=0L) {
			type=W_KING;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		if ((mask & queens)!=0L) {
			type=W_QUEEN;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		if ((mask & knights)!=0L) {
			type=W_KNIGHT;
			if ((mask & black)!=0L)
				type++;
			return type;
		}
		return _EMPTY; // ERROR?
	}

	int movePiece(int i, int j, int i2, int j2) {
		return 0;//TODO
	}

	long getMoves(int i, int j, byte type){
		return 0L;//TODO
	}

	
	void printBoard() {
		char type[]={'-','p','P','r','R','n','N','b','B','q','Q','k','K'};
		for (int i=8;i>0;i--) {
			for (int j=1;j<=8;j++)
				System.out.print(type[getPieceType(i,j)]+"\t");
				//System.out.print("");
			System.out.println();
		}
		
		
	}
	
	void printNumbers(){
		for (int i=8;i>0;i--) {
			for (int j=1;j<=8;j++)
				System.out.print(getPieceType(i,j)+"\t");			
			System.out.println();
		}
	}

	public static void main(String args[]){
		Board brd=new Board();
		
		//brd.printNumbers();
		brd.printBoard();
		
	}

}
