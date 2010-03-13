import java.util.*;
import java.io.*;

/* Author: Nibblonians
 * Etapa 1: 
 * 			implementare comenzi xboard
 */

public class Game {
	public static BufferedWriter outWriter;       
	public static int i = 0, j = 0;
	public static Board brd=new Board();
	 public static String sir = "abcdefgh";
	   
	public static void main(String []args) throws Exception{

		 int i = 0, j = 0;
	        XBoard xboard = new XBoard();
	        
	        
	        while(xboard.isAlive()) {
	            boolean citit = xboard.read();
	            if(xboard.isTurn()) {
	            if (xboard.engine == xboard.WHITE) {
	                    if (i < 8) {
	                        xboard.sendToXBoard("move " + sir.charAt(i) + "2" + sir.charAt(i) + "4");
	                        i++;
	                    }
	                }
	                if (xboard.engine == xboard.BLACK) {
	                    if (j < 8) {
	                        xboard.sendToXBoard("move " + sir.charAt(j) + "7" + sir.charAt(j) + "5");
	                        j++;
	                    }
	                }
	            }
	        if (i==8 || j==8) xboard.close();
                
	        }
	        xboard.close();
	        }

	}
