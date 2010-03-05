import java.io.FileWriter;
import java.io.IOException;


public class Moves {
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

		
		FileWriter f=new FileWriter("defMoves.java");
		f.write("public class defMoves{\n");
		f.write("long[] wpawnMoves={");
		
		long[] wpawn = new long[65];
		for (int i=0;i<8;i++)
			//f.write("0L,");
			wpawn[i+1]=0L;
		for (int i=0;i<=7;i++){
			long mask = (128L << 8) >> (i%8); 
			mask = mask << 8 | mask << 16;
			//f.write("0x"+Long.toHexString(mask)+"L,");
			wpawn[8+i+1]=mask;
			//printBoard(mask);
		}
		for (int i=16;i<56;i++){
			long mask = 128L >> (i%8) << 8*(i/8);
			mask = mask << 8;
			//f.write("0x"+Long.toHexString(mask)+"L,");
			wpawn[i+1]=mask;
			//printBoard(mask);
		}
		//f.write("0L};//ULTIMUL NU SE PUNE\n");
		
		
		// wpawnMoves[8*(i-1)+j] = mutarile posibile din pozitia (i,j) 
		f.write("0L,");
		for (int i=1;i<=64;i++){
			f.write("0x"+Long.toHexString(wpawn[i])+"L,");
			System.out.println(i+":");
			//printBoard(wpawn[i]);
		}
		f.write("0L}\n;");
		
		// bpawnMoves[8*(i-1)+j] = mutarile posibile din pozitia (i,j) 
		
		f.write("long[] bpawnMoves={");
		f.write("0L,");
		
		long []bpawn=new long[66];
		for (int i=1;i<=64;i++) {
			
			//f.write("0x"+Long.toHexString(invLines(wpawn[(8-i/8+1)+i%8]))+"L,");
			//printBoard(invLines(wpawn[(8-i/8+1)+i%8])); // wrong FORMULA !!!
		}
		
		for (int i=1;i<=8;i++) {
			for (int j=1;j<=8;j++)
				bpawn[(i-1)*8+j]=invLines(wpawn[(8-i)*8+j]);
		}
		for (int i=1;i<=64;i++){
			System.out.println(i+":\n");
			printBoard(bpawn[i]);
			f.write("0x"+Long.toHexString(bpawn[i])+"L,");
		}
		f.write("0L}\n;");
		
		f.write("\n}\n");
		f.close();
		//return null;
	}

	public static void main(String args[]) throws Exception {
		genMoves();
	}

	
	
	
	
}
