import java.util.*;
import java.io.*;

/* Author: Nibblonians
 * Etapa 1: 
 * 			implementare comenzi xboard
 */

public class Game {
	public static BufferedWriter outWriter;       
    public static int WHITE = 0;
    public static int BLACK = 1;
    public static int FORCE = 2;
    public static int side = WHITE, engine = BLACK;
    public static int i = 0, j = 0;
    public static Board2 brd=new Board2();
    
    public static void main(String []args) throws Exception{
		
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		// variabile ce reprezinta comanda primita si cea data
		String command = "";
		String retCommand = "";
		String sir = "abcdefgh";
		//fisier folosit la debugging
		outWriter = new BufferedWriter(new FileWriter("out.txt"));
		while (true){
			System.out.flush();
			if (side == engine) {
				String myMove = "";
                if (engine == WHITE) {
                    if (i < 8) {
                        myMove = "move " + sir.charAt(i) + "2" + sir.charAt(i) + "4";                    
                        i++;
                    }
                   // else myMove = "resign";
                }
                if (engine == BLACK) {
                    if (j < 8) {
                        myMove = "move " + sir.charAt(j) + "7" + sir.charAt(j) + "5";
                        j++;
                    }
                   // else myMove = "resign";
                }
                 side = (side + 1) % 2;
                  
                 Move m = new Move(myMove.substring(5));
                 
                 if (brd.isValidMove(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12()))){
 					brd.movePiece(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12()));
 					System.out.println(myMove);
                 }
 					
 			else 
 				System.out.println("resign");
                 
                 //System.out.println(myMove);
            }
			command = inReader.readLine();
			if (command.length() > 0)
				outWriter.write(command+"\n");
			retCommand = evaluateCommand(command);
			outWriter.flush();
			if (retCommand.equals("exit")){
				outWriter.close();
				inReader.close();
				return;
			}
			if (retCommand.length() > 0)
				System.out.println(retCommand);
		}
		
    }
    
    private static String evaluateCommand(String command){
		
    	//verificam mai intai comenzile cu probabilitate mai mare de aparitie
    	// precum mutarile,time si otim iar restul comenzilor le verificam in rodinea aparitiei lor
    	
    	if (command.startsWith("usermove")){
    		String mutare = command.substring(8);
    		//verificare mutare valida
    		//modificare tabla cu comanda data de el
    		//boolean ok = verificareMutare(board,mutare);
    		//board = newBoard(board,mutare);
    		Move m = new Move(mutare);
    		
    		if (brd.isValidMove(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12())))
				brd.movePiece(m.getP11(), m.getP12(), m.getP21(), m.getP22(), brd.getPieceType(m.getP11(), m.getP12()));
		else 
			System.out.println("NotValid");
    		
    		side = (side + 1)%2;
    		return "";
    		
    	}
    	if (command.startsWith("time") || command.startsWith("otim")){
    		return "";
    	}
    	if (command.equals("xboard")){
    		System.out.println();
    		return "";
    	}
    	if (command.startsWith("protover")){
    		return "feature usermove=1";
    	}
    	if (command.equals("new")){
    		side = WHITE;
    		engine = BLACK;
    		return "";
    	}
    	if (command.equals("computer") || command.equals("hard") || command.equals("easy") || 
    			command.startsWith("level") || command.startsWith("accepted") || command.equals("post")
    			|| command.equals("random"))
    		return "";
    	if (command.equals("force")){
    		engine = FORCE;
    		return "";
    	}
    	if (command.equals("white")){
    		side = WHITE;
            engine = BLACK;
            return "";
    	}
    	if (command.equals("black")) {
            side = BLACK;
            engine = WHITE;
            return "";
        }
    	if (command.equals("go")){
    		engine = side;
    		return "";
    	}
    	if (command.equals("quit")){
    		return "exit";
    	}
    	//pentru orice alte comenzi care au probabilitate 
    	//mai mica de aparitie nu ne intereseaza sa le evaluam si intoarcem sirul vid 
    	return "";
    }
}
