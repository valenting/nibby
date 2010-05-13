import java.util.*;
import java.io.*;

/* Author: Nibblonians
 * Etapa 4: 
 * 			implementare NegaMax si AlphaBeta
 * 			implementare NegaScout + quiescence search
 * 			implementare iterative deepening
 * 			optimizare deBruijn
 * 			deschideri
 */

public class Game {
    public static Board brd=new Board();
    
    
    public static void main(String []args) throws Exception{
       	XBoard xboard = new XBoard();
       	Board.generateStatic();
       	if (args.length>=2) {
       		Usual.setVars(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 0);
       	} else {
       		Usual.setVars(4, 4, 0);
       	}
        Openings.reset();
        while(xboard.isAlive()) {       	
        	if(xboard.isTurn()) {
        		if (!Openings.isSet())
        			Openings.init(0);
            	xboard.sendToXBoard(brd.nextMove((byte)xboard.side,xboard.time,xboard.otim));
            }
        	boolean citit = xboard.read();
            if (xboard.lastMove.startsWith("usermove") && xboard.side!=xboard.engine){
            		if (!Openings.isSet())
            			Openings.init(1);
                	String mutare = xboard.lastMove.substring(9);
            		brd.SAN(mutare,xboard.side);					
            		brd.updateBoard(brd.pos1,brd.pos2,brd.promotion);	
            		xboard.chSide();
            		Openings.makeMove(new Move(brd.pos1,brd.pos2));
                }     
            else if (xboard.lastMove.startsWith("new")) {
            		brd = new Board();
            		Openings.reset();
            }
            else if (xboard.lastMove.contains("printboard")){
            	brd.printBoard();
            	
            }
        }
    }
}
    
 