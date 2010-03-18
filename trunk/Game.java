import java.util.*;
import java.io.*;

/* Author: Nibblonians
 * Etapa 1: 
 * 			implementare comenzi xboard
 */

public class Game {
    public static Board brd=new Board();
    
    public static void main(String []args) throws Exception{
    	int i = 0, j = 0;
        XBoard xboard = new XBoard();
        while(xboard.isAlive()) {
            boolean citit = xboard.read();
            if (xboard.lastMove.startsWith("printb")){
        		brd.printBoard();
        	}
            if(xboard.isTurn()) {
            	xboard.sendToXBoard("move "+brd.nextMove((byte)xboard.side));
            }
            else 
            	if (xboard.lastMove.startsWith("usermove")){
                	String mutare = xboard.lastMove.substring(9);
            		brd.SAN(mutare,xboard.side);
            		brd.move(brd.pos1,brd.pos2);
                }

        }
    }
}
    
 