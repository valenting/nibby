
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
            side = 1 - side;
        }
        System.out.println(command);
        System.out.flush();

    }
    
    public void chSide() {
    	side = 1 - side;
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
            if (lastMove.equals("new")) {
                side = WHITE;
                engine = BLACK;
                return true;
            }
            if (lastMove.equals("xboard")){
            	System.out.println();
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
                System.out.println("feature done=0 myname=\"nibbyEngine 0.1\" usermove=1 san=1 sigint=0 done=1");
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


