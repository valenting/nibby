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
            if(xboard.isTurn()) {
            	if (xboard.engine == xboard.WHITE) {
                    if (i < 8) {
                        xboard.sendToXBoard("move " + xboard.sir.charAt(i) + "4");
                        brd.move(8+i,24+i);
                        i++;
                    }
                    else xboard.sendToXBoard("resign");
                }
                if (xboard.engine == xboard.BLACK) {
                    if (j < 8) {
                        xboard.sendToXBoard("move " + xboard.sir.charAt(j) + "5");
                        brd.move(48+j, 32+j);
                        j++;
                    }
                    else xboard.sendToXBoard("resign");
                }
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
    
 