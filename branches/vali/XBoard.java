import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class XBoard{
	BufferedReader inReader=null;
	BufferedWriter log=null; // log file

	public static int WHITE = 0;
	public static int BLACK = 1;
	public static int FORCE = -1;

	public int yourColor = FORCE;
	public int toMove = WHITE;

	public String lMove = "";

	public int time;
	public int otim;

	public XBoard(String outf) throws Exception{
		inReader = new BufferedReader(new InputStreamReader(System.in));
		log = new BufferedWriter(new FileWriter(outf));
	}

	public XBoard() throws Exception{
		inReader = new BufferedReader(new InputStreamReader(System.in));
		log = new BufferedWriter(new FileWriter("out.txt"));
	}

	public XBoard(boolean printLogs) throws Exception{
		inReader = new BufferedReader(new InputStreamReader(System.in));

		if (printLogs)
			log = new BufferedWriter(new FileWriter("D:/out.txt"));
	}

	// initializeaza conexiunea
	// se asteapta mutarea
	public void init() throws Exception {
		// while ( System.in.available() )
		// init culori
		// init time
		// totul pana la GO
		String read="";
		while (!read.contains("go")){
			read=inReader.readLine();

			if (log!=null)
				log.write(read+"\n");

			if (read.contains("xboard"))
				;
			if (read.contains("protover"))
				System.out.println("feature usermove=1");
			if (read.contains("new") || read.contains("random")
					|| read.contains("level") || read.contains("post") 
					|| read.contains("hard") || read.contains("easy"))
				;
			if (read.contains("white"))
				yourColor=WHITE;
			if (read.contains("black"))
				yourColor=BLACK;
			if (read.contains("usermove") && !read.contains("accepted")) {
				lMove = read.substring(8);
				yourColor = BLACK;
				toMove = BLACK;
			} // you are white/computer black - set lMoves
			if (read.contains("go")) {
				toMove = yourColor;
				return ;
			}// make move; init() stops  -> break;
			if (read.contains("force"))
				yourColor = FORCE; // ??
			if (read.contains("otim"))
				otim = Integer.parseInt(read.substring(4));
			if (read.contains("otim"))
				otim = Integer.parseInt(read.substring(4));
		}
		close();
		write("quit");
	}

	public void close() throws Exception {
		if (log!=null)
			log.close();

	}

	public String lastMove() {
		return lMove;
	}

	public void write(String command) throws Exception{
		System.out.println(command);
		if (log!=null)
			log.write("_"+command+"\n");
		if (command.contains("move"))
			toMove = (toMove+1)&1;
	}

	public void readAndLog() throws Exception {
		String read="";
		while ( System.in.available() > 0 ) {
			read=inReader.readLine();

			if (log!=null)
				log.write(read+"\n");

			if (read.contains("usermove")) {
				lMove=read.substring(8); // ??
				toMove = (toMove+1)&1;
			}
			if (read.equals("quit")){
				close();
			}
			// ce face??
			if (read.equals("force")){
				;
			}
			if (read.contains("time")){
				time = Integer.parseInt(read.substring(4));//
			}
			if (read.contains("otim")){
				otim = Integer.parseInt(read.substring(4));//
			}
		}
	}
}
