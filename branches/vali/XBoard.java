//package chess;

import java.io.*;

/**
 *
 * @author Costin
 */
public class XBoard {

    BufferedReader inPipe = null;
    PrintWriter log = null;
    public static String sir = "abcdefgh";
    public static int WHITE = 0;
    public static int BLACK = 1;
    public static int FORCE = -1;
 
    public boolean on = false;
    public int toMove = WHITE;
    public int myColor = BLACK;

    public String lastMove = "";
    public int time;
    public int otim;
    

    public XBoard() throws IOException {
    	
        inPipe = new BufferedReader(new InputStreamReader(System.in));
        on=true;
    }
    
    public XBoard(boolean log_messages) throws IOException{
    	inPipe = new BufferedReader(new InputStreamReader(System.in));
        on=true;
        if (log_messages)
        	log = new PrintWriter("D:/out" + (int) (Math.random() * 200) + ".txt");
    }

    public boolean isTurn() {
        return toMove == myColor;
    }
    public boolean isAlive() {
        return on;
    }
   
    public void sendToXBoard(String command) {
        if (command.startsWith("move")) {
            toMove = (toMove + 1) % 2;
        }
        System.out.println(command);
        System.out.flush();

    }
    
    public void close(){
    	log.close();
    }

    public boolean read() {
       
        System.out.flush();
        try {
            lastMove = inPipe.readLine();
            log.println(lastMove);
            log.flush();
      
        } catch (IOException er) {
            log.println("Eroare citire din pipe");
        }
        
        if (lastMove.length() > 0) {
            if (lastMove.startsWith("usermove")) {
                toMove = (toMove + 1) % 2;
                return true;

            }
            if (lastMove.equals("new")) {
                toMove = WHITE;
                myColor = BLACK;
                return true;
            }
            if (lastMove.startsWith("time")) {
                time = Integer.parseInt(lastMove.substring(5));
                return true;
            }
            if (lastMove.startsWith("otim")) {
                otim = Integer.parseInt(lastMove.substring(5));
                return true;
            }
            if (lastMove.equals("force")) {
                myColor = FORCE;
                return true;

            }
            if (lastMove.startsWith("protover")) {
                System.out.println("feature done=0");
                System.out.println("feature myname=\"nibbyEngine 0.1\"");
                System.out.println("feature usermove=1");
                System.out.println("feature done=1");
                return true;
            }
            if (lastMove.equals("white")) {
                toMove = WHITE;
                myColor = BLACK;
                return true;
            }
            if (lastMove.equals("black")) {
                toMove = BLACK;
                myColor = WHITE;
                return true;
            }
            if (lastMove.equals("go")) {
                myColor = toMove;
                return true;
            }
            if (lastMove.equals("ping")) {
                System.out.println("pong");
                return true;
            }
            if (lastMove.equals("quit")) {
                try {
                     inPipe.close();
                     log.close();
                     on=false;
                     return true;

                } catch (IOException ex) {
                    log.println("Eroare inchidere fisier");    
                }
               

            }

            
            
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws IOException {
        int i = 0, j = 0;
        XBoard xboard = new XBoard();
        
        
        while(xboard.isAlive()) {
            boolean citit = xboard.read();
            if(xboard.isTurn()) {
            if (xboard.myColor == WHITE) {
                    if (i < 8) {
                        xboard.sendToXBoard("move " + sir.charAt(i) + "2" + sir.charAt(i) + "4");
                        i++;
                    }
                }
                if (xboard.myColor == BLACK) {
                    if (j < 8) {
                        xboard.sendToXBoard("move " + sir.charAt(j) + "7" + sir.charAt(j) + "5");
                        j++;
                    }
                }
            }
            

        }

    }
}


