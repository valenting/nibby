package chess;

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
    public static int FORCE = 2;

    public boolean on=false;
    public int side = WHITE;
    public int engine = BLACK;

    public String lastMove = "";
    public int time;
    public int otim;
    

    public XBoard() throws IOException {
        inPipe = new BufferedReader(new InputStreamReader(System.in));
        on=true;
        log = new PrintWriter("out" + (int) (Math.random() * 20) + ".txt");
    }

    public boolean isTurn() {
        return side == engine;
    }
    public boolean isAlive() {
        return on;
    }
   
    public void sendToXBoard(String command) {
        if (command.startsWith("move")) {
            side = (side + 1) % 2;
        }
        System.out.println(command);
        System.out.flush();

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
                side = (side + 1) % 2;
                return true;

            }
            if (lastMove.equals("new")) {
                side = WHITE;
                engine = BLACK;
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
                engine = FORCE;
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
                side = WHITE;
                engine = BLACK;
                return true;
            }
            if (lastMove.equals("black")) {
                side = BLACK;
                engine = WHITE;
                return true;
            }
            if (lastMove.equals("go")) {
                engine = side;
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
            if (xboard.engine == WHITE) {
                    if (i < 8) {
                        xboard.sendToXBoard("move " + sir.charAt(i) + "2" + sir.charAt(i) + "4");
                        i++;
                    }
                }
                if (xboard.engine == BLACK) {
                    if (j < 8) {
                        xboard.sendToXBoard("move " + sir.charAt(j) + "7" + sir.charAt(j) + "5");
                        j++;
                    }
                }
            }
            

        }

    }
}

