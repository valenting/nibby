import java.util.*;
import java.io.*;

/* Author: Nibblonians
 * Etapa 1: 
 * 			implementare comenzi xboard
 * 			mutare pion cat timp are mutari valide
 */

public class Game {
    public static Board brd=new Board();
    
    public static void main(String []args) throws Exception{
        XBoard xboard = new XBoard();
        while(xboard.isAlive()) {       	
        	if(xboard.isTurn()) {
            	xboard.sendToXBoard(brd.nextMove((byte)xboard.side));
            																		brd.printBoard();brd.printBoard(brd.getValidMoves(4));
            }
        	boolean citit = xboard.read();
            if (xboard.lastMove.startsWith("usermove") && xboard.side!=xboard.engine){
                	String mutare = xboard.lastMove.substring(9);
            		brd.SAN(mutare,xboard.side);
            		brd.updateBoard(brd.pos1,brd.pos2,brd.promotion);	brd.printBoard();brd.printBoard(brd.getValidMoves(4));
            															//	System.out.println(brd.enPassantBlack+ " si white " + brd.enPassantWhite);
            		xboard.chSide();
                }                                    
        }
    }
}
    
 