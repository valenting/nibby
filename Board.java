import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 *	In aceasta forma revizuita in care mutarile unei piese sunt verificate daca lasa
 *	sau nu regele in sah, mutarile nu au fost verificate complet in practica
 *	
 *	Este posibil sa fie anumite scapari la anumite functii desi asta va mai avea nevoie
 *	de niste teste intensive.
 *
 *	Pentru pioni, varianta curenta este suficienta, dar pentru alte piese, ar trebui revazut
 */

//Asta random este doar de test
import java.util.Random;

public class Board {
	public static final byte _EMPTY=0, W_PAWN=1, W_ROOK=2, W_KNIGHT=3, W_BISHOP=4, W_QUEEN=5, W_KING=6; // CONSTANTE ALB
	public static final byte           B_PAWN=9, B_ROOK=10, B_KNIGHT=11, B_BISHOP=12, B_QUEEN=13, B_KING=14; // CONSTANTE NEGRU
	private long table;
	//private long black;
	//private long white;
	
	private long[] pieces = new long[7]; // pieces[1] = pion, pieces[2] = rook... etc 
	private long[] color = new long[2]; // white.. black
	
	//	Variabile adaugate de Andrei columnPosition,rowPosition - ca sa nu mai fac calcule
	//	de asemenea enPassantWhite - pe ce coloana se poate face captura en passant la 
	//	piesa White si analog enPassantBlack
	public static long knightMasks[] = new long[64];
	public static long kingMasks[] = new long[64];
	public static final byte columnPosition[] = new byte[64];
	public static final byte rowPosition[] = new byte[64]; 
	private byte enPassantWhite = 9;
	private byte enPassantBlack = 9;
	private boolean canCastleWhite = true;
	private boolean canCastleBlack = true;
	public static char columnChar[] = {'A','B','C','D','E','F','G','H'};
	public static char rowChar[] = {'1','2','3','4','5','6','7','8'};
	
	public static final long longCastlingWhite = 0x00000000000000CL;
	public static final long shortCastlingWhite = 0x0000000000000060L;
	public static final long longCastlingBlack = 0xC00000000000000L;
	public static final long shortCastlingBlack = 0x6000000000000000L;
	
	byte types[];
	
	long[] attacksTo;
	long[] attacksFrom;
	
	public Board(){
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
		
		//	Adaugat de Andrei - preferabil sa nu se initialize astea statice aici
		generateStatic();
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
	//.............................................................................................
	/*
	 *	Functii adaugate de Andrei (Observati cum vorbesc despre mine la persoana
	 *	a treia!).
	 *
	 */
	
	public byte getColor(long piece){
		if((piece & color[0])!=0L)
			return (byte)0;
		return (byte)1;
	}
	
	public void generateStatic(){
		knightMasks[0]=0x20400L;
		knightMasks[1]=0x50800L;
		knightMasks[2]=0xa1100L;
		knightMasks[3]=0x142200L;
		knightMasks[4]=0x284400L;
		knightMasks[5]=0x508800L;
		knightMasks[6]=0xa01000L;
		knightMasks[7]=0x402000L;
		knightMasks[8]=0x2040004L;
		knightMasks[9]=0x5080008L;
		knightMasks[10]=0xa110011L;
		knightMasks[11]=0x14220022L;
		knightMasks[12]=0x28440044L;
		knightMasks[13]=0x50880088L;
		knightMasks[14]=0xa0100010L;
		knightMasks[15]=0x40200020L;
		knightMasks[16]=0x204000402L;
		knightMasks[17]=0x508000805L;
		knightMasks[18]=0xa1100110aL;
		knightMasks[19]=0x1422002214L;
		knightMasks[20]=0x2844004428L;
		knightMasks[21]=0x5088008850L;
		knightMasks[22]=0xa0100010a0L;
		knightMasks[23]=0x4020002040L;
		knightMasks[24]=0x20400040200L;
		knightMasks[25]=0x50800080500L;
		knightMasks[26]=0xa1100110a00L;
		knightMasks[27]=0x142200221400L;
		knightMasks[28]=0x284400442800L;
		knightMasks[29]=0x508800885000L;
		knightMasks[30]=0xa0100010a000L;
		knightMasks[31]=0x402000204000L;
		knightMasks[32]=0x2040004020000L;
		knightMasks[33]=0x5080008050000L;
		knightMasks[34]=0xa1100110a0000L;
		knightMasks[35]=0x14220022140000L;
		knightMasks[36]=0x28440044280000L;
		knightMasks[37]=0x50880088500000L;
		knightMasks[38]=0xa0100010a00000L;
		knightMasks[39]=0x40200020400000L;
		knightMasks[40]=0x204000402000000L;
		knightMasks[41]=0x508000805000000L;
		knightMasks[42]=0xa1100110a000000L;
		knightMasks[43]=0x1422002214000000L;
		knightMasks[44]=0x2844004428000000L;
		knightMasks[45]=0x5088008850000000L;
		knightMasks[46]=0xa0100010a0000000L;
		knightMasks[47]=0x4020002040000000L;
		knightMasks[48]=0x400040200000000L;
		knightMasks[49]=0x800080500000000L;
		knightMasks[50]=0x1100110a00000000L;
		knightMasks[51]=0x2200221400000000L;
		knightMasks[52]=0x4400442800000000L;
		knightMasks[53]=0x8800885000000000L;
		knightMasks[54]=0x100010a000000000L;
		knightMasks[55]=0x2000204000000000L;
		knightMasks[56]=0x4020000000000L;
		knightMasks[57]=0x8050000000000L;
		knightMasks[58]=0x110a0000000000L;
		knightMasks[59]=0x22140000000000L;
		knightMasks[60]=0x44280000000000L;
		knightMasks[61]=0x88500000000000L;
		knightMasks[62]=0x10a00000000000L;
		knightMasks[63]=0x20400000000000L;
		
		rowPosition[0]=0;
		columnPosition[0]=0;
		rowPosition[1]=0;
		columnPosition[1]=1;
		rowPosition[2]=0;
		columnPosition[2]=2;
		rowPosition[3]=0;
		columnPosition[3]=3;
		rowPosition[4]=0;
		columnPosition[4]=4;
		rowPosition[5]=0;
		columnPosition[5]=5;
		rowPosition[6]=0;
		columnPosition[6]=6;
		rowPosition[7]=0;
		columnPosition[7]=7;
		rowPosition[8]=1;
		columnPosition[8]=0;
		rowPosition[9]=1;
		columnPosition[9]=1;
		rowPosition[10]=1;
		columnPosition[10]=2;
		rowPosition[11]=1;
		columnPosition[11]=3;
		rowPosition[12]=1;
		columnPosition[12]=4;
		rowPosition[13]=1;
		columnPosition[13]=5;
		rowPosition[14]=1;
		columnPosition[14]=6;
		rowPosition[15]=1;
		columnPosition[15]=7;
		rowPosition[16]=2;
		columnPosition[16]=0;
		rowPosition[17]=2;
		columnPosition[17]=1;
		rowPosition[18]=2;
		columnPosition[18]=2;
		rowPosition[19]=2;
		columnPosition[19]=3;
		rowPosition[20]=2;
		columnPosition[20]=4;
		rowPosition[21]=2;
		columnPosition[21]=5;
		rowPosition[22]=2;
		columnPosition[22]=6;
		rowPosition[23]=2;
		columnPosition[23]=7;
		rowPosition[24]=3;
		columnPosition[24]=0;
		rowPosition[25]=3;
		columnPosition[25]=1;
		rowPosition[26]=3;
		columnPosition[26]=2;
		rowPosition[27]=3;
		columnPosition[27]=3;
		rowPosition[28]=3;
		columnPosition[28]=4;
		rowPosition[29]=3;
		columnPosition[29]=5;
		rowPosition[30]=3;
		columnPosition[30]=6;
		rowPosition[31]=3;
		columnPosition[31]=7;
		rowPosition[32]=4;
		columnPosition[32]=0;
		rowPosition[33]=4;
		columnPosition[33]=1;
		rowPosition[34]=4;
		columnPosition[34]=2;
		rowPosition[35]=4;
		columnPosition[35]=3;
		rowPosition[36]=4;
		columnPosition[36]=4;
		rowPosition[37]=4;
		columnPosition[37]=5;
		rowPosition[38]=4;
		columnPosition[38]=6;
		rowPosition[39]=4;
		columnPosition[39]=7;
		rowPosition[40]=5;
		columnPosition[40]=0;
		rowPosition[41]=5;
		columnPosition[41]=1;
		rowPosition[42]=5;
		columnPosition[42]=2;
		rowPosition[43]=5;
		columnPosition[43]=3;
		rowPosition[44]=5;
		columnPosition[44]=4;
		rowPosition[45]=5;
		columnPosition[45]=5;
		rowPosition[46]=5;
		columnPosition[46]=6;
		rowPosition[47]=5;
		columnPosition[47]=7;
		rowPosition[48]=6;
		columnPosition[48]=0;
		rowPosition[49]=6;
		columnPosition[49]=1;
		rowPosition[50]=6;
		columnPosition[50]=2;
		rowPosition[51]=6;
		columnPosition[51]=3;
		rowPosition[52]=6;
		columnPosition[52]=4;
		rowPosition[53]=6;
		columnPosition[53]=5;
		rowPosition[54]=6;
		columnPosition[54]=6;
		rowPosition[55]=6;
		columnPosition[55]=7;
		rowPosition[56]=7;
		columnPosition[56]=0;
		rowPosition[57]=7;
		columnPosition[57]=1;
		rowPosition[58]=7;
		columnPosition[58]=2;
		rowPosition[59]=7;
		columnPosition[59]=3;
		rowPosition[60]=7;
		columnPosition[60]=4;
		rowPosition[61]=7;
		columnPosition[61]=5;
		rowPosition[62]=7;
		columnPosition[62]=6;
		rowPosition[63]=7;
		columnPosition[63]=7;
		
		kingMasks[0]=0x302L;
		kingMasks[1]=0x705L;
		kingMasks[2]=0xe0aL;
		kingMasks[3]=0x1c14L;
		kingMasks[4]=0x3828L;
		kingMasks[5]=0x7050L;
		kingMasks[6]=0xe0a0L;
		kingMasks[7]=0xc040L;
		kingMasks[8]=0x30203L;
		kingMasks[9]=0x70507L;
		kingMasks[10]=0xe0a0eL;
		kingMasks[11]=0x1c141cL;
		kingMasks[12]=0x382838L;
		kingMasks[13]=0x705070L;
		kingMasks[14]=0xe0a0e0L;
		kingMasks[15]=0xc040c0L;
		kingMasks[16]=0x3020300L;
		kingMasks[17]=0x7050700L;
		kingMasks[18]=0xe0a0e00L;
		kingMasks[19]=0x1c141c00L;
		kingMasks[20]=0x38283800L;
		kingMasks[21]=0x70507000L;
		kingMasks[22]=0xe0a0e000L;
		kingMasks[23]=0xc040c000L;
		kingMasks[24]=0x302030000L;
		kingMasks[25]=0x705070000L;
		kingMasks[26]=0xe0a0e0000L;
		kingMasks[27]=0x1c141c0000L;
		kingMasks[28]=0x3828380000L;
		kingMasks[29]=0x7050700000L;
		kingMasks[30]=0xe0a0e00000L;
		kingMasks[31]=0xc040c00000L;
		kingMasks[32]=0x30203000000L;
		kingMasks[33]=0x70507000000L;
		kingMasks[34]=0xe0a0e000000L;
		kingMasks[35]=0x1c141c000000L;
		kingMasks[36]=0x382838000000L;
		kingMasks[37]=0x705070000000L;
		kingMasks[38]=0xe0a0e0000000L;
		kingMasks[39]=0xc040c0000000L;
		kingMasks[40]=0x3020300000000L;
		kingMasks[41]=0x7050700000000L;
		kingMasks[42]=0xe0a0e00000000L;
		kingMasks[43]=0x1c141c00000000L;
		kingMasks[44]=0x38283800000000L;
		kingMasks[45]=0x70507000000000L;
		kingMasks[46]=0xe0a0e000000000L;
		kingMasks[47]=0xc040c000000000L;
		kingMasks[48]=0x302030000000000L;
		kingMasks[49]=0x705070000000000L;
		kingMasks[50]=0xe0a0e0000000000L;
		kingMasks[51]=0x1c141c0000000000L;
		kingMasks[52]=0x3828380000000000L;
		kingMasks[53]=0x7050700000000000L;
		kingMasks[54]=0xe0a0e00000000000L;
		kingMasks[55]=0xc040c00000000000L;
		kingMasks[56]=0x203000000000000L;
		kingMasks[57]=0x507000000000000L;
		kingMasks[58]=0xa0e000000000000L;
		kingMasks[59]=0x141c000000000000L;
		kingMasks[60]=0x2838000000000000L;
		kingMasks[61]=0x5070000000000000L;
		kingMasks[62]=0xa0e0000000000000L;
		kingMasks[63]=0x40c0000000000000L;

	}
	
	//	Functie care afiseaza un long ca un bitboard
	public void printBoard(long n){
		long mask = 1L;
		System.out.println();
		for(int i=7;i>=0;i--){
			for(int j=0;j<8;j++){
				mask = 1L << 8*i << j;
				if((mask & n)!=0)
					System.out.print("1 ");
				else
					System.out.print("0 ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	long movesOfWhitePawn(long piece,byte row,byte column,boolean checkPass){
		long forward = 0L,hostile=0L,oneMove;
		boolean diag1=false,diag2=false;
		oneMove = piece << 8;
		if((oneMove & table)==0L){	// nu este coliziune
										//System.out.println("printez si aici");printBoard(oneMove & table);
			forward |= oneMove;
			if(row==1){
				oneMove = oneMove << 8;
				if((oneMove & table)==0L)
					forward |= oneMove;
			}
		}
		
									//System.out.println("prim_hostile");printBoard(hostile);
		
		if(forward != 0L)
			if(checkPass && leavesOwnKingInCheck(piece,forward))
				forward = 0L;	//	se anuleaza mutarea in fata pentru ca lasa regele in sah
		
		
		if(column<7){	//	nu este pe coloana ultima
			oneMove = piece << 9;
			oneMove &= color[1];				
		}
		else
			oneMove = 0L;
		if(oneMove != 0L)	//	exista piesa pe care o poate captura
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))//	daca mutarea nu lasa regele propriu in sah
					hostile |= oneMove;
		
															//System.out.println("secund_hostile");printBoard(hostile);
		oneMove = 0L;	 
		if(column>0){	//	nu este pe prima coloana
			oneMove = piece << 7;
			oneMove &= color[1];
		}
		else
			oneMove = 0L;
		if(oneMove != 0L)	//	exista piesa pe care o poate captura
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))//	daca mutarea nu lasa regele propriu in sah
					hostile |= oneMove;


															///System.out.println("tert_hostile");printBoard(hostile);

		if(row==4){	//are potential sa faca en Passant
			if(column+1==enPassantBlack){System.out.println("passant in dreapta");
				oneMove = piece << 9;
				if(checkPass && leavesOwnKingInCheck(piece,oneMove))// lasa regele in sah
					hostile |= oneMove;	//	se elimina mutarea propusa
			}
			else if(column-1==enPassantBlack){System.out.println("passant in stanga");
				oneMove = piece << 7;
				hostile |= oneMove;
				if(checkPass && leavesOwnKingInCheck(piece,oneMove))// lasa regele in sah
					hostile ^= oneMove;	//	se elimina mutarea propusa
			}
		}
														//	System.out.println("hostile");printBoard(hostile);
		return hostile | forward;
	}
	
	long movesOfBlackPawn(long piece,byte row,byte column,boolean checkPass){
		long forward = 0L,hostile=0L,oneMove;
		boolean diag1=false,diag2=false;
		oneMove = piece >>> 8;
		if((oneMove & table)==0){	// nu este coliziune
			forward |= oneMove;
			if(row==6){
				oneMove = oneMove >>> 8;
				if((oneMove & table)==0)
					forward |= oneMove;
			}
		}
		if(checkPass && leavesOwnKingInCheck(piece,forward))
			forward = 0L;	//	se anuleaza mutarea in fata pentru ca lasa regele in sah
		
		if(column<7){	//	nu este pe coloana ultima
			oneMove = piece >>> 7;;
			oneMove &= color[0];
		}
		else
			oneMove = 0L;
		if(oneMove != 0L)	//	exista piesa pe care o poate captura
			if(checkPass){	//	daca se verifica lasarea in sah
				if(!leavesOwnKingInCheck(piece,oneMove))//	daca mutarea nu lasa regele propriu in sah
					hostile |= oneMove;
			} 
		
			 
		if(column>0){	//	nu este pe prima coloana
			oneMove = piece >>> 9;
			oneMove &= color[0];
		}
		else
			oneMove = 0L;
		if(oneMove != 0L)	//	exista piesa pe care o poate captura
			if(checkPass){	//	daca se verifica lasarea in sah
				if(!leavesOwnKingInCheck(piece,oneMove))//	daca mutarea nu lasa regele propriu in sah
					hostile |= oneMove;
			} 

		if(row==3){	//are potential sa faca en Passant
			if(column+1==enPassantBlack){System.out.println("passant in dreapta");
				oneMove = piece >>> 7;
				hostile |= oneMove;
				if(checkPass && leavesOwnKingInCheck(piece,oneMove))// lasa regele in sah
					hostile ^= oneMove;	//	se elimina mutarea propusa
			}
			else if(column-1==enPassantBlack){System.out.println("passant in stanga");
				oneMove = piece >>> 9;
				hostile |= oneMove;
				if(checkPass && leavesOwnKingInCheck(piece,oneMove))// lasa regele in sah
					hostile ^= oneMove;	//	se elimina mutarea propusa
			}
		}
		
		return hostile | forward;
	}
	
	long movesOfRook(long piece,byte row,byte column,boolean checkPass){
		long mask = 0L,oneMove;
		byte i ;
		i = column;
		//	Mutari orizontale spre coloana A
		oneMove = piece;
		if(i>0){
			oneMove = oneMove >>> 1;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(i>0){
					mask |= oneMove;
					if((oneMove & table)==0){
						i--;
						oneMove = oneMove >>> 1;
					}
					else
						break;
				}
		}
		//	Mutari orizontale spre coloana H
		i = column;
		oneMove = piece;
		if(i<7){
			oneMove = oneMove << 1;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(i<7){
					mask |= oneMove;
					if((oneMove & table)==0){
						i++;
						oneMove = oneMove << 1;
					}
					else
						break;
				}
		}
		//	Mutari verticale spre linia 8
		i = row;
		oneMove = piece;
		if(i<7){
			oneMove = oneMove << 8;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(i<7){
						mask |= oneMove;
						if((oneMove & table)==0){
							i++;
							oneMove = oneMove << 8;
						}
						else
							break;
				}
		}
		//	Mutari verticale spre linia 1
		i = row;
		oneMove = piece;
		if(i>0){
			oneMove = oneMove >>> 8;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(i>0){
					mask |= oneMove;
					if((oneMove & table)==0){
						i--;
						oneMove = oneMove >>> 8;
					}
					else
						break;
				}
		}
		
		if((piece & pieces[1])!=0)
			i = 1;	//	piesa este BLACK
		else
			i = 0;	//	piesa este WHITE
		return mask ^ (mask & color[i]);	//se elimina mutarile peste piese proprii
	}
	
	long movesOfBishop(long piece,byte row,byte column,boolean checkPass){
		long mask = 0L,oneMove;
		byte minusRow = row,plusRow = (byte)(7 - row);
		byte minusColumn = column,plusColumn = (byte)(7 - column); 
		
		byte shifts;
		
		//	Deplasari spre A1
		if(minusRow<minusColumn)
			shifts = minusRow;
		else
			shifts = minusColumn;
		oneMove = piece;
		if(shifts>0){
			oneMove = oneMove >>> 9;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(shifts>0){
					mask |= oneMove;
					if((oneMove & table)==0){
						shifts--;
						oneMove = oneMove >>> 9;
					}
					else
						break;
				}
		}
		//	Deplasari spre H1
		if(minusRow<plusColumn)
			shifts = minusRow;
		else
			shifts = plusColumn;
		oneMove = piece;
		if(shifts>0){
			oneMove = oneMove >>> 7;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
			while(shifts>0){
				mask |= oneMove;
				if((oneMove & table)==0){
					shifts--;
					oneMove = oneMove >>> 7;//printBoard(oneMove);
				}
				else
					break;
			}
		}
		//	Deplasari spre A8
		if(plusRow<minusColumn)
			shifts = plusRow;
		else
			shifts = minusColumn;
		oneMove = piece;
		if(shifts>0){
			oneMove = oneMove << 7;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))	
				while(shifts>0){
					mask |= oneMove;
					if((oneMove & table)==0){
						shifts--;
						oneMove = oneMove << 7;
					}
					else
						break;
				}
		}
		//	Deplasari spre H8
		if(plusRow<plusColumn)
			shifts = plusRow;
		else
			shifts = plusColumn;
		oneMove = piece;
		if(shifts>0){
			oneMove = oneMove << 9;
			if(!checkPass || !leavesOwnKingInCheck(piece,oneMove))
				while(shifts>0){
					mask |= oneMove;
					if((oneMove & table)==0){
						shifts--;
						oneMove = oneMove << 9;
					}
					else
						break;
				}
		}
		
		if((piece & pieces[1])!=0)
			shifts = 1;	//	piesa este BLACK
		else
			shifts = 0;	//	piesa este WHITE
		return mask ^ (mask & color[shifts]);	//se elimina mutarile peste piese proprii
	}
	
	long movesOfKnight(long piece,int position,boolean checkPass){
		byte side = (byte)((types[position] & 8) >> 3);
		if(checkPass && leavesOwnKingInCheck(piece,0L))
			return 0L;
		return knightMasks[position] ^ (knightMasks[position] & color[side]);
	}
	
	long attackAreaBlack(){
		//TO DO
		//determina pozitiile atacate de piesele negre
		//functie care se foloseste tot mutarile standard
		return 0L;
	}
	
	long attackAreaWhite(){
		//TO DO
		return 0L;
	}
	
	long movesOfWhiteKing(long piece,int pozitie){
		/*
		 *	Functia asta va primi si ea un  parametru boolean care va determina daca
		 *	se adauga la masca mutarile de rocada; mutarile se adauga doar daca se iau
		 *	in considerare potentiale mutari, nu si daca se calculeaza zona de atac a regelui 
		 */
		
		long mask = kingMasks[pozitie];
		mask = mask ^ (mask & color[0]);	//zone acoperite de rege prin mutari simple
		
		if(canCastleWhite){
			long att=attackAreaBlack();
			mask = mask ^ (mask & att);	//eliminarea patratelor ce il duc pe rege in sah
			if((att & shortCastlingWhite)==0L)	//Se poate face rocada mica
				mask |= 0x0000000000000040L;
			if((att & longCastlingWhite)==0L)	//Se poate face rocada mare
				mask |= 0x0000000000000004L;
			
		}
		return mask;
		//	TO DO
	}
	
	long movesOfBlackKing(long piece,int position){
		return 0L;
		//	TO DO
	}
	
	boolean leavesOwnKingInCheck(long start,long end){
		//	Functia verifica daca mutarea analizata lasa regele in sah
		//	Functia va fi modificata pentru a utiliza updateBoard si restorePieceOnBoard
		//	varianta curenta nu face toate modificarile posibile asupra tablei de sah
		long att;
		boolean result;
		if((color[0]&start)!=0L){//muta WHITES
			color[0] ^= start;
			color[0] |= end;
			att = attackAreaBlack();
			if((color[0] & pieces[6] & att)!=0)
				result = true;
			else
				result = false;
			color[0] |= start;
			color[0] ^= end;
		}
		else{//muta WHITES
			color[1] ^= start;
			color[1] |= end;
			att = attackAreaWhite();
			if((color[1] & pieces[6] & att)!=0)
				result = true;
			else
				result = false;
			color[1] |= start;
			color[1] ^= end;
		}
		return result;
	}
	
	public long potentialMovesBoard(long piece){
		//	Face selectia intre diferitele piese, pe baza mastii date ca parametru
		
		int pozitie = Long.numberOfTrailingZeros(piece);
		byte elem = types[pozitie];
		
		
		if(elem==W_PAWN) return movesOfWhitePawn(piece,rowPosition[pozitie],columnPosition[pozitie],true);
		if(elem==B_PAWN) return movesOfBlackPawn(piece,rowPosition[pozitie],columnPosition[pozitie],true);
		
		if(elem==W_KING) return movesOfWhiteKing(piece,pozitie);
		if(elem==B_KING) return movesOfBlackKing(piece,pozitie);
		
		elem &= 7;
		
		switch(elem){
			case W_KNIGHT : return movesOfKnight(piece,pozitie,true);
			case W_ROOK : return movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie],true);
			case W_BISHOP : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie],true);
			default : return movesOfBishop(piece,rowPosition[pozitie],columnPosition[pozitie],true) 
								| movesOfRook(piece,rowPosition[pozitie],columnPosition[pozitie],true);	
		}
	}
	
	public void restorePieceOnBoard(long piece,byte elementType){
		//Seteaza o piesa la o pozitie data
		//Functi va fi apelata de leavesOwnKingInCheck, pentru a repozitiona o piesa
		//dupa o captura virtuala
		//TO DO
	}
	
	public void updateBoard(long start,long end){
		/*
		 *	Modifica board-ul in conformitate cu ultima mutare executata:
		 *		table,color[0],color[1],type[] si pieces[]
		 */
		
		byte side = getColor(start);
		byte oppositeSide = (byte)((side+1) & 1);
		byte elementType;// = getPieceType(position);
		
		//update-ul intregii placi de sah
		table ^= start;
		table |= end;
		//update-ul placii de culoare
		color[side] ^= start;
		color[side] |= end;	
		//update-ul placii de culoare adversa
		if((color[oppositeSide] & end)!=0L)	{//este captura
			//update placa de culoare opusa
			color[oppositeSide] ^= end;
			//update placa de piese
			elementType = (byte)(getPieceType(Long.numberOfTrailingZeros(end)) & 7);
			pieces[elementType] ^= end; 
		}
													//	printBoard(end);
		//update tabla de coduri
		elementType = getPieceType(Long.numberOfTrailingZeros(start));
		types[Long.numberOfTrailingZeros(end)] = elementType;
		types[Long.numberOfTrailingZeros(start)] = _EMPTY;
		//update tabla piesa ce muta
		elementType &= 7;
		pieces[elementType] ^= start;
		pieces[elementType] |= end;
		
		
	}
	
	public String getSANMove(long start,long end){
		/*
		 *	Intoarcea SAN-ul unei mutari reprezentate prin masca sursei si masca destinatiei
		 *	Momentan functia nu face traducerile decat pentru pioni; Negrul nu a fost verificat
		 *	inca in teste
		 */
		int numberStart = Long.numberOfTrailingZeros(start);
		int numberEnd = Long.numberOfTrailingZeros(end);
		
		byte type = getPieceType(numberStart);
		byte oppositeSide = (byte)((getColor(start)+1) & 1);
		
		
		StringBuilder sb = new StringBuilder();
		switch(type){
			case W_PAWN : {
					if((end & color[oppositeSide])!=0){//captura piesa
						sb.append(columnChar[columnPosition[numberStart]]);
						sb.append('x');
						}
					
					sb.append(columnChar[columnPosition[numberEnd]]);
					sb.append(rowChar[rowPosition[numberEnd]]);
					return sb.toString();
					}
			case B_PAWN : {
					if((end & color[oppositeSide])!=0){//captura piesa
						sb.append(columnChar[columnPosition[numberStart]]);
						sb.append('x');
						}
					
					sb.append(columnChar[columnPosition[numberEnd]]);
					sb.append(rowChar[rowPosition[numberEnd]]);
					return sb.toString();
					}
			default: return " ";
		}
	}
	
	public String nextMove(byte side){
		/*
		 *	Functie folosita pentru a selecta mutarea urmatoare(cel putin pentru etapa 1);
		 *	Functia intoarce un string cu mutarea transpusa in SAN
		 */
		
		long piece = color[side] & pieces[1];
		String moveCode = "";
									
		long endPosition;
		int number;
		if(side==0)	{//WHITE
			piece = Long.highestOneBit(piece);
			number = Long.numberOfTrailingZeros(piece);
			endPosition = movesOfWhitePawn(piece,rowPosition[number],columnPosition[number],false);
			endPosition = Long.highestOneBit(endPosition);
			
		
			
			moveCode = getSANMove(piece,endPosition);
			updateBoard(piece,endPosition);
			return moveCode;
		}
		else{		//Black
			piece = Long.lowestOneBit(piece);
			number = Long.numberOfTrailingZeros(piece);
			movesOfWhitePawn(piece,rowPosition[number],columnPosition[number],false);
			endPosition = movesOfBlackPawn(piece,rowPosition[number],columnPosition[number],false);
			endPosition = Long.lowestOneBit(endPosition);
			moveCode = getSANMove(piece,endPosition);
			updateBoard(piece,endPosition);
			return moveCode;
		}
		
	}
	
	void testare(){
		
		
	//	int i=4,j=6;	//linie si coloana
	//	long piece = 1L <<8*i<<j;
	//	printBoard(piece);
	//	color[0] |= piece;
		
	//	enPassantBlack = 7;
	//	table = 0xFFFF000080205FFFL;
	//	color[0] = 0x000000000000FFFFL;
	//	printBoard(color[0]);
	//	printBoard(movesOfWhitePawn(piece,(byte)i,(byte)j,false));
		
		System.out.println(nextMove((byte)0));
		printBoard();
	/*	System.out.println("pioni");
		printBoard(pieces[1]);
		System.out.println("board");
		printBoard(table);
		System.out.println("color[0]");
		printBoard(color[0]);
	*/	System.out.println(nextMove((byte)0));
	printBoard();
		System.out.println(nextMove((byte)0));
		printBoard();
		System.out.println(nextMove((byte)0));
		printBoard();
		System.out.println(nextMove((byte)0));
		printBoard();
	}
	
	//						Aici se termina ce a implementat Andrei
	//.............................................................................................

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
		Board brd=new Board();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String move="";
		
		brd.testare();
		
		/*
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
		*/
	}
}

