import java.io.FileWriter;
import java.io.IOException;


public class MoveGenerator {
	//TODO
	// toti vectorii de mutari posibile
	
	static long invLines(long board){
		 return (board >> 8*7 & 0xFFL) | (board >> 8*5 & 0xFF00L) | (board >> 8*3 & 0xFF0000L) | (board >> 8 & 0xFF000000L) | (board << 8 & 0xFF00000000L) | (board << 8*3 & 0xFF0000000000L) | (board << 8*5 & 0xFF000000000000L) | ( board << 8*7 & 0xFF00000000000000L); 
	}
	
	static void printBoard(long board){
		String rep0=Long.toBinaryString(board);
		String zeros="0000000000000000000000000000000000000000000000000000000000000000";
		rep0=zeros.subSequence(0, 64-rep0.length())+rep0;
		for (int i=0;i<8;i++)
			System.out.println(rep0.subSequence(i*8, (i+1)*8));
		System.out.println();
	}

	static void genMoves() throws Exception{

		// orizontale
		long[] lines = new long[9];
		lines[1]=0x00000000000000FFL;
		for (int i=2;i<=8;i++)
			lines[i]=lines[i-1] << 8;
		//return lines;

		// verticale
		long[] vert = new long[9];
		vert[8] = 0x0101010101010101L;
		for (int i=7;i>=1;i--)
			vert[i]=vert[i+1] << 1;

		//diagonale principale triunghi superior
		long[] pdiagH = new long[9];
		pdiagH[1] = 0x8040201008040201L; 
		for (int i=2;i<=8;i++)
			pdiagH[i]=(pdiagH[i-1] >> 1) & ~pdiagH[i-1] & ~lines[i-2]; 

		//diagonale principale triunghiul inferior
		long[] pdiagL = new long[9];
		pdiagL[1] = 0x8040201008040201L; 
		pdiagL[2] = (pdiagL[1] << 1) & ~pdiagL[1];
		for (int i=3;i<=8;i++)
			pdiagL[i]=(pdiagL[i-1] << 1) & ~pdiagL[i-1] & ~lines[8-i+3]; 

		//diagonale secundare triunghiul superior
		long[] sdiagH = new long[9];
		sdiagH[1] = 0x0102040810204080L; 
		for (int i=2;i<=8;i++)
			sdiagH[i]=(sdiagH[i-1] << 1) & ~sdiagH[i-1] & ~vert[8]; 

		//diagonale secundare triunghiul inferior
		long[] sdiagL = new long[9];
		sdiagL[1] = 0x0102040810204080L; 
		for (int i=2;i<=8;i++)
			sdiagL[i]=(sdiagL[i-1] >> 1) & ~sdiagL[i-1] & ~vert[1]; 

		
		FileWriter f=new FileWriter("Moves.java");
		f.write("public class Moves{\n");
		//f.write("long[] wpawnMoves={");
		
		long[] wpawn = new long[65];
		for (int i=0;i<8;i++)
			wpawn[i+1]=0L;
		for (int i=0;i<=7;i++){
			long mask = (128L << 8) >> (i%8); 
			mask = mask << 8 | mask << 16;
			wpawn[8+i+1]=mask;
		}
		for (int i=16;i<56;i++){
			long mask = 128L >> (i%8) << 8*(i/8);
			mask = mask << 8;
			wpawn[i+1]=mask;
		}
		
		
		// wpawn[8*(i-1)+j] = mutarile posibile din pozitia (i,j) 
		MoveGenerator.writeArrayH("wpawn", wpawn, f);
		
		
		// bpawn[8*(i-1)+j] = mutarile posibile din pozitia (i,j) 
		MoveGenerator.writeArrayH("bpawn", MoveGenerator.inv(wpawn), f);
		
		
		long[] wpawnTake= new long[65];
		for (int i=0;i<=64;i++){
			wpawnTake[i]=wpawn[i] | (wpawn[i] << 1) | (wpawn[i] >> 1);
			int lin=i/8;
			if (i%8!=0)
				lin++;
			int col=i%8;
			if (col==0)
				col=8;
			if (col==1)
				wpawnTake[i]=wpawnTake[i] ^ (wpawn[i] << 1);
			if (col==8)
				wpawnTake[i]=wpawnTake[i] ^ (wpawn[i] >> 1);
			wpawnTake[i]=wpawnTake[i] & ~vert[col];
			if (lin!=8)
				wpawnTake[i] &= lines[lin+1];
		}
		
//		for (int i=1;i<=64;i++){
//			int lin=i/8;
//			if (i%8!=0)
//				lin++;
//			int col=i%8;
//			if (col==0)
//				col=8;
//			System.out.println("lin:"+lin+"col:"+col);
//			printBoard(wpawnTake[i]);
//		}
		
		MoveGenerator.writeArrayH("wpawnTakes", wpawnTake, f);
		
		MoveGenerator.writeArrayH("bpawnTakes", inv(wpawnTake), f);
		
		f.write("static long[][] all={ {0x0L}, wpawn, bpawn};");
		
		f.write("\n}\n");
		f.close();
		//return null;
	}
	
	public static void writeArrayH(String arrayName, long[] array, FileWriter f) throws Exception{
		f.write("static long[] "+arrayName+"={");
		for (int i=0;i<array.length;i++)
			f.write("0x"+Long.toHexString(array[i])+"L,");
		f.write("0x0L};");
		f.write("// Size: "+array.length+" +1 (final: 0x0L)");
		f.write("\n");
		}
		
	public static long[] inv(long[] arr){
		long[] nou=new long[arr.length];
		for (int i=1;i<=8;i++) {
			for (int j=1;j<=8;j++)
				nou[(i-1)*8+j]=invLines(arr[(8-i)*8+j]);
		}
		return nou;
	}
	
	public static void main(String args[]) throws Exception {
		genMoves();
	}

	
	
	
	
}
