
// Generic data type

public class Move {
	private static char table[]={'a','b','c','d','e','f','g','h'};
	
	private int pos1,pos2;
	private int type;
	
	
	Move(int i, int j, int i2, int j2){
		pos1=i*8+j;
		pos2=i2*8+j;
	}
	
	Move(String move){
		/* a2a4 */
		move=move.toLowerCase();
		//pos1=((move.charAt(1)-'1') << 3) + move.charAt(0)-'a'; //?
		pos1=(move.charAt(1)-'1')*8+move.charAt(0)-'a';
		pos2=(move.charAt(3)-'1')*8+move.charAt(2)-'a';
	}
	
	Move(int ps1, int ps2){
		this.pos1=ps1;
		this.pos2=ps2;	
	}
	
	public String strMove() {
		return ""+table[pos1%8]+pos1/8+table[pos2%8]+pos2/8;
	}
	
	public int getPos1(){
		return this.pos1;
	}
	
	public int getPos2(){
		return this.pos2;
	}
		
	// Static methods :)
	public static String strMove(int i, int j, int i2, int j2){
		return (new Move(i,j,i2,j2)).strMove();
	}
	
	public static String strMove(int pos1, int pos2){
		return (new Move(pos1,pos2)).strMove();
	}
	
	public static int getPos1(String move){
		return (new Move(move)).getPos1();
	}
	
	public static int getPos2(String move){
		return (new Move(move)).getPos2();
	}
	
	public String toSAN(Board b) {
		
		return null;
	}
}
