
// Generic data type
public class Move {
	private static String table=" abcdefgh";
	private String strMove="";
	private int p11, p12, p21, p22;
	private int pos1,pos2;
	
	Move(int i, int j, int i2, int j2){
		this.p11=i;
		this.p12=j;
		this.p21=i2;
		this.p22=j2;
		this.strMove=table.charAt(j)+i+table.charAt(j2)+i2+"";
		this.pos1=(i-1)*8+j;
		this.pos2=(i2-1)*8+j;
	}
	Move(String move){
		this.strMove=move;
		this.p11=move.charAt(1)-'0';
		this.p21=move.charAt(3)-'0';
		this.p12=move.toLowerCase().charAt(0)-'a'+1;
		this.p22=move.toLowerCase().charAt(2)-'a'+1;
		this.pos1=(this.p11-1)*8+this.p12;
		this.pos2=(this.p21-1)*8+this.p22;
	}
	Move(int ps1, int ps2){
		this.pos1=ps1;
		this.pos2=ps2;
		int i=ps1/8;
		int j=ps1%8;
		if (j==0)
			j=8;
		else 
			i++;
		this.p11=i;
		this.p12=j;
		int i2=ps2/8;
		int j2=ps2%8;
		if (j2==0)
			j2=8;
		else 
			i2++;
		this.p21=i2;
		this.p22=j2;
		this.strMove=table.charAt(j)+i+table.charAt(j2)+i2+"";
	}
	
	public String strMove() {
		return this.strMove;
	}
	
	public int getPos1(){
		return this.pos1;
	}
	
	public int getPos2(){
		return this.pos2;
	}
	
	public int getP11(){
		return this.p11;
	}
	
	public int getP12(){
		return this.p12;
	}
	
	public int getP21(){
		return this.p21;
	}
	
	public int getP22(){
		return this.p22;
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
	
}
