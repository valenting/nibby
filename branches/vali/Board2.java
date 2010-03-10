import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Board2 {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6; // CONSTANTE ALB
	public static final byte           B_PAWN=9, B_ROOK=10, B_KNIGHT=11, B_BISHOP=12, B_QUEEN=13, B_KING=14; // CONSTANTE NEGRU
	private long table;
	//private long black;
	//private long white;
	
	private long[] pieces = new long[7]; // pieces[1] = pion, pieces[2] = rook... etc 
	private long[] color = new long[2]; // white.. black
	
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
	}
}

