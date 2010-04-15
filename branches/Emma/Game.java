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
       	Board.generateStatic();
        while(xboard.isAlive()) {       	
        	if(xboard.isTurn()) {
            	xboard.sendToXBoard(brd.nextMove((byte)xboard.side));
            }
        	boolean citit = xboard.read();
            if (xboard.lastMove.startsWith("usermove") && xboard.side!=xboard.engine){
                	String mutare = xboard.lastMove.substring(9);
            		brd.SAN(mutare,xboard.side);					
            		brd.updateBoard(brd.pos1,brd.pos2,brd.promotion);	
            		xboard.chSide();
            		
                }     
            else if (xboard.lastMove.startsWith("new"))
            		brd = new Board();
        }
    }
}
    
 