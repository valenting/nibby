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
    public static int FORCE = 2;

    public boolean on=false;
    public int side = WHITE;
    public int engine = BLACK;

    public String lastMove = "";
    public int time;
    public int otim;
    

    public XBoard() throws IOException {
        inPipe = new BufferedReader(new InputStreamReader(System.in));
        on = true;
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
        String line="";
        System.out.flush();
        try {
            line = inPipe.readLine();
            log.println(line);
            log.flush();
      
        } catch (IOException er) {
            log.println("Eroare citire din pipe");
        }
        
        if (line.length() > 0) {
            if (line.startsWith("usermove")) {
            	lastMove=line.substring(8);
                side = (side + 1) % 2;
                return true;

            }
            if (line.equals("new")) {
                side = WHITE;
                engine = BLACK;
                return true;
            }
            if (line.equals("xboard")){
            	System.out.println();
            	return true;
            }
            if (line.startsWith("time")) {
                time = Integer.parseInt(line.substring(5));
                return true;
            }
            if (line.startsWith("otim")) {
                otim = Integer.parseInt(line.substring(5));
                return true;
            }
            if (line.equals("force")) {
                engine = FORCE;
                return true;

            }
            if (line.startsWith("protover")) {
                System.out.println("feature done=0 myname=\"nibbyEngine 0.1\" usermove=1 san=1 done=1");
                return true;
            }
            if (line.equals("white")) {
                side = WHITE;
                engine = BLACK;
                return true;
            }
            if (line.equals("black")) {
                side = BLACK;
                engine = WHITE;
                return true;
            }
            if (line.equals("go")) {
                engine = side;
                return true;
            }
            if (line.equals("ping")) {
                System.out.println("pong");
                return true;
            }
            if (line.equals("quit")) {
                try {
                    inPipe.close();
                    log.close();
                    on = false;
                    return true;

                } catch (IOException ex) {
                    log.println("Eroare inchidere fisier");    
                }
               

            }  
            return true;
        }
        return false;
    }
}


