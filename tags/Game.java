import java.util.*;
import java.io.*;
import java.io.RandomAccessFile;
import java.util.Random;

/* Author: Nibblonians
 * Etapa 1: 
 * 			implementare comenzi xboard
 * 			mutare pion cat timp are mutari valide
 */

public class Game {
    public static Board brd=new Board();
    
    
    public static void main(String []args) throws Exception{
       	Random r = new Random();
    	RandomAccessFile rf = new RandomAccessFile("Andrei "+r.nextInt(60)+".txt","rw");
        XBoard xboard = new XBoard();
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
            		rf.writeBytes(brd.printAndreiBoard());
            		rf.writeBytes("Promovare la "+brd.promotion+"\r\n");
                }     
            else if (xboard.lastMove.startsWith("new"))
            		brd = new Board();
        }
    }
}
    
 