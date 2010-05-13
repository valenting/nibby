import java.util.HashMap;


public class Usual {
	
	public static int depth, maxDepth, eval;
	
	public static void setVars(int d, int mD, int e){
		depth = d;
		maxDepth = mD;
		eval = e;
	}
	public static long boardMask(String pos) {
		pos=pos.toLowerCase();
		return 1L << ( ( pos.charAt(1) - '1' ) << 3 | ( pos.charAt(0) - 'a' ) );
	}
	
	public static int position(String pos) {
		pos=pos.toLowerCase();
		return ( pos.charAt(1) - '1' ) << 3 | ( pos.charAt(0) - 'a' );
	}
	
	public static int pos1(String move){
		move=move.toLowerCase();
		return ( move.charAt(1) - '1' ) << 3 | ( move.charAt(0) - 'a' );
	}
	
	public static int pos2(String move){
		move=move.toLowerCase();
		return ( move.charAt(3) - '1' ) << 3 | ( move.charAt(2) - 'a' );
	}
	
	public static String stringPos(int pos) {
		return ((char)('a'+(pos&7)))+""+((pos>>3)+1)+"";
	}
	
	public static void printBoard(long board){
		String rep0=Long.toBinaryString(board);
		String zeros="0000000000000000000000000000000000000000000000000000000000000000";
		rep0=zeros.subSequence(0, 64-rep0.length())+rep0;
		for (int i=0;i<8;i++)
			System.out.println(new StringBuffer(rep0.subSequence(i*8, (i+1)*8)).reverse());
		System.out.println();
	}
	
}
