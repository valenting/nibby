
public class Board {
	public final byte _EMPTY=0, W_PAWN=1, W_ROOK=3, W_KNIGHT=5, W_BISHOP=7, W_QUEEN=9, W_KING=11; // CONSTANTE ALB
	public final byte           B_PAWN=2, B_ROOK=4, B_KNIGHT=6, B_BISHOP=8, B_QUEEN=10, B_KING=12; // CONSTANTE NEGRU
	private long table;
	private long white;
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
		
		
		
		table = 0xFF000000000000FFL;

		pawns   = 0x0F000000000000F0L;
		rooks   = 0x8100000000000081L;
		knights = 0x4200000000000042L;
		bishops = 0x2400000000000024L;
		//orientation dependent
		kings   = 0x0800000000000008L;
		queens  = 0x1000000000000010L;

		white   = 0x00000000000000FFL;
		black   = 0xFF00000000000000L;

	}
	
	/* i (linia) - 1 .. 8 */
	/* j (coloana) - 1.. 8 */
	public byte getPieceType(int i,int j){
		byte type=0;
		long mask = 1 << (8*(i-1))+(j-1);
		if ((mask & table)!=0)
			return this._EMPTY;
		if ((mask & pawns)!=0) {
			type=this.W_PAWN;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		if ((mask & rooks)!=0) {
			type=this.W_ROOK;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		if ((mask & bishops)!=0) {
			type=this.W_BISHOP;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		if ((mask & kings)!=0) {
			type=this.W_KING;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		if ((mask & queens)!=0) {
			type=this.W_QUEEN;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		if ((mask & knights)!=0) {
			type=this.W_KNIGHT;
			if ((mask & black)!=0)
				type++;
			return type;
		}
		return _EMPTY; // ERROR?
	}
		
	int movePiece(int i, int j, int i2, int j2) {
		return 0;
	}
	
	int getMoves(int i, int j, byte type){
		return 0;
	}
	
	
	
	
}
