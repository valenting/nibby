package chess;

import java.io.*;

/**
 *
 * @author Costin
 */
public class implMinimala {

    public static int WHITE = 0;
    public static int BLACK = 1;
    public static int FORCE = 2;

    public static void main(String[] args) throws FileNotFoundException {
        // stdin
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        //salvare comenzi
        PrintWriter pr = new PrintWriter("out" + (int) (Math.random() * 20) + ".txt");

        String s = "";
        String sir = " abcdefgh";
        // programul muta pionii la rand
        int i = 1;
        int j = 1;

        int side = WHITE;
        int engine = BLACK;

        while (true) {
            System.out.flush();
            // muta daca a venit randul tau
            if (side == engine) {
                if (engine == WHITE) {
                    if (i < 9) {
                        System.out.println("move " + sir.charAt(i) + "2" + sir.charAt(i) + "4");
                        side = (side + 1) % 2;
                        i++;
                    }
                }
                if (engine == BLACK) {
                    if (j < 9) {
                        System.out.println("move " + sir.charAt(j) + "7" + sir.charAt(j) + "5");
                        side = (side + 1) % 2;
                        j++;
                    }
                }
            }

            // prelucreaza urmatoarea comanda
            try {
                s = in.readLine();
            } catch (IOException er) {
            }
            if (s.length() > 0) {
                pr.println(s);

                if (s.matches("[a-h][1-8][a-h][1-8]")) {
                    side = (side + 1) % 2;
                    continue;
                }
                if (s.equals("new")) {
                    side = WHITE;
                    engine = BLACK;
                    continue;
                }
                if (s.equals("force")) {
                    engine = FORCE;
                    continue;
                }
                if (s.equals("white")) {
                    side = WHITE;
                    engine = BLACK;
                    continue;
                }
                if (s.startsWith("protover")) {
                    System.out.println("feature done=0");
                    System.out.println("feature myname=\"nibbyEngine 0.1\"");
                    System.out.println("feature done=1");
                    continue;

                }
                if (s.equals("black")) {
                    side = BLACK;
                    engine = WHITE;
                    continue;
                }
                if (s.equals("go")) {
                    engine = side;
                    continue;
                }
                if (s.equals("ping")) {
                    System.out.println("pong");
                    continue;
                }
                if (s.equals("quit")) {
                    return;
                }


                pr.flush();
            }

        }
    }
}
