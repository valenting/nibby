import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Board2 {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=3, W_KNIGHT=5, W_BISHOP=7, W_QUEEN=9, W_KING=11; // CONSTANTE ALB
	public static final byte           B_PAWN=2, B_ROOK=4, B_KNIGHT=6, B_BISHOP=8, B_QUEEN=10, B_KING=12; // CONSTANTE NEGRU
	//private long white;
	private long table;
	private long black;
	//	private long pawns;
	//	private long kings;
	//	private long queens;
	//	private long rooks;
	//	private long bishops;
	//	private long knights;
	private long[] pieces = new long[7]; // pieces[1] = pion, pieces[2] = rook... etc 
	public Board2(){
		// la Start:

		/* 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 | 0000 0000 */ 
		/* a8 ->> h8 | a7 ->> h7 | ...                                                       | a1 ->> h1 */
		table = 0xFFFF00000000FFFFL;
		black   = 0xFFFF000000000000L;

		pieces[1]   = 0x00FF00000000FF00L;
		pieces[2]   = 0x8100000000000081L;
		pieces[3] = 0x4200000000000042L;
		pieces[4] = 0x2400000000000024L;
		//orientation dependent
		pieces[5]  = 0x1000000000000010L;
		pieces[6]   = 0x0800000000000008L;

		//white   = 0x000000000000FFFFL;

	}

	public Board getCopy(){
		try {
			return (Board)this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/* i (linia) - 1 .. 8 */
	/* j (coloana) - 1.. 8 */

	public byte getPieceType(int i,int j){
		byte type=0;

		long mask = 128L >> (j-1) << 8*(i-1); //??needs checked ??//
		//long mask = Moves.masks[(i-1)*8+j]; 

		if ((mask & table)==0L)
			return _EMPTY;
		for (byte k=1;k<=6;k++){
			if ((mask & pieces[k])!=0L) {
				type=(byte) (2*k-1);
				if ((mask&black)!=0L)
					type++;
				return type;
			}
		}
		return _EMPTY; // ERROR?
	}

	boolean isValidMove(int i, int j, int i2, int j2, byte type){
		if (( getValidMoves(i,j,type) & Moves.masks[(i2-1)*8+j2]) != 0L)
			return true;
		return false;
	}

	long getValidMoves(int i, int j, byte type){
		long mov=Moves.all[type][(i-1)*8+j];

		if (type/2+type%2==1){ // e pion
			if (type%2==1){
				if ((Moves.masks[i*8+j]&table)!=0L)
					mov=0L;
			}
			else
				if ((Moves.masks[(i-2)*8+j]&table)!=0L)
					mov=0L;

			mov=mov & ~table;
			if (type%2==0)
				mov=mov | (Moves.pawnTakes[type][(i-1)*8+j] & (table&~black)); // & white
			else 
				mov= mov | (Moves.pawnTakes[type][(i-1)*8+j] & black);
		}
		return mov;
	}


	int movePiece(int i, int j, int i2, int j2, byte type) {

		long mask1 = 128L >> (j-1) << 8*(i-1); 
		long mask2 = 128L >> (j2-1) << 8*(i2-1); 

		byte type2=getPieceType(i2,j2);
		if (type2 !=_EMPTY) {
			pieces[type2/2+type2%2] = pieces[type2/2+type2%2] ^ mask2;
			if (type2%2==0 && type2!=_EMPTY)
				black=black^mask2;
		}
		table = table & ~mask1 | mask2;
		pieces[type/2+type%2] = pieces[type/2+type%2] & ~mask1 | mask2;
		
		if (type%2==0 && type!=_EMPTY)
			black = black & ~mask1 | mask2;

		
		//TODO
		// si restul tablelor

		return 0;

	}


	void printBoard() {
		char type[]={'-','p','P','r','R','n','N','b','B','q','Q','k','K'};
		//String chars=" abcdefgh"; 
		for (int i=0;i<=8;i++)
			System.out.print(i+"\t");
		System.out.println();
		for (int i=8;i>0;i--) {
			System.out.print(i+"\t");
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
		Board2 brd=new Board2();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String move="";
		
		while (true){
			brd.printBoard();
			try {
				move=in.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Move m=new Move(move);
			if (brd.isValidMove(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12())))
					brd.movePiece(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12()));
			else 
				System.out.println("NotValid");
		}

	}
}

