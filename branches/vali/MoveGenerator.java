import java.io.FileWriter;
import java.io.IOException;


public class MoveGenerator {
	//TODO
	// toti vectorii de mutari posibile

	static void printBoard(long board){
		String rep0=Long.toBinaryString(board);
		String zeros="0000000000000000000000000000000000000000000000000000000000000000";
		rep0=zeros.subSequence(0, 64-rep0.length())+rep0;
		for (int i=0;i<8;i++)
			System.out.println(new StringBuffer(rep0.subSequence(i*8, (i+1)*8)).reverse());
		System.out.println();

	}

	static void genMoves() throws Exception{

		// orizontale
		long[] lines = new long[8];
		lines[0]=0x00000000000000FFL;
		for (int i=1;i<8;i++)
			lines[i]=lines[i-1] << 8;
		//return lines;

		// verticale
		long[] vert = new long[8];
		vert[0] = 0x0101010101010101L;
		for (int i=1;i<8;i++)
			vert[i]=vert[i-1] << 1;


		long[] wPawns = new long[64];
		for (int i=8;i<64;i++)
			wPawns[i]=(1L<<i)<<8;
		for (int i=8;i<16;i++)
			wPawns[i]|=(1L<<i)<<16;


		long[] wPawnTakes=new long[64];
		for (int i=8;i<64;i++) {
			wPawnTakes[i]=((1L<<i)<<7)|((1L<<i)<<9);
			if (i%8==0)
				wPawnTakes[i] &= ~vert[7];
			if (i%8==7)
				wPawnTakes[i] &= ~vert[0];
		}
		
		FileWriter f=new FileWriter("Moves.java");
		f.write("public class Moves{\n");
		
		//printBoards(wPawnTakes);
		writeArray("wPawns", wPawns, f);
		writeArray("wPawnTakes", wPawnTakes, f);
		writeArray("bPawns", invert(wPawns),f);
		writeArray("bPawnTakes",invert(wPawnTakes),f);
		
		f.write("static long[][] Pawns={wPawns,bPawns};\n");
		f.write("static long[][] PawnTakes={wPawnTakes,bPawnTakes};\n");
		
		//W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6; // CONSTANTE ALB
		//public static final byte           B_PAWN=9, B_ROOK=10, B_KNIGHT=11, B_BISHOP=12, B_QUEEN=13, B_KING=14; // CONSTANTE NEGR
		
		f.write("static long[][] all={ {0}, wPawns, {0}, {0}, {0}, {0}, {0}, {0}, {0}, " +
											"bPawns, {0}, {0}, {0}, {0}, {0} };\n" );
		
		f.write("}\n");
		f.close();
		
	}
	
	public static void printBoards(long[] arr){
		for (int i=0;i<arr.length;i++){
			System.out.print(i+":\n");
			printBoard(arr[i]);
		}
	}

	public static void writeArray(String arrayName, long[] array, FileWriter f) throws Exception{
		f.write("static long[] "+arrayName+"={");
		for (int i=0;i<array.length;i++)
			f.write("0x"+Long.toHexString(array[i])+"L,");
		f.write("0x0};");
		f.write("// Size: "+array.length+"++");
		f.write("\n");
	}
	
	static long[] invert(long[] arr){
		long[] v=new long[arr.length];
		for (int i=0;i<arr.length;i++)
			v[i]=Long.reverseBytes(arr[(7-i/8)*8+i%8]);
		return v;
	}


	public static void main(String args[]) throws Exception {
		genMoves();
	}





}
