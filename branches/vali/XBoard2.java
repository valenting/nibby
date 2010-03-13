import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class XBoard2{
	BufferedReader inReader;
	BufferedWriter log; // log file
	
	public static int WHITE = 0;
    public static int BLACK = 1;
    
    int yourColor;
    int toMove=WHITE;
	
    String lMove;
    
    int time;
    int otim;
    
	public XBoard2(String outf) throws Exception{
		inReader = new BufferedReader(new InputStreamReader(System.in));
		log = new BufferedWriter(new FileWriter(outf));
	}
	
	public XBoard2() throws Exception{
		inReader = new BufferedReader(new InputStreamReader(System.in));
		log = new BufferedWriter(new FileWriter("out.txt"));
	}
	
	// initializeaza conexiunea
	// se asteapta mutarea
	public void init() throws Exception {
		// while ( System.in.available() )
		// init culori
		// init time
		// totul pana la GO
		String read="";
		while (inReader.ready() && !read.contains("go")){
			read=inReader.readLine();
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
				lMove=read.substring(8); // you are white/computer black - set lMoves
				toMove = BLACK;
			}
			if (read.contains("go"))
				break; // make move; init() stops  -> break;
			if (read.contains("force"))
				; // ??
		}
	}
	public void close() throws IOException {
		if (log!=null)
			log.close();
	}
	
	public String lastMove() {
		return lMove;
	}
	
	public void write(String command) throws Exception{
		System.out.println(command);
		log.write("_"+command+"\n");
		toMove=(toMove+1)&1;
	}
	
	public void readAndLog() throws Exception {
		String read="";
		while ( inReader.ready()) {
			read=inReader.readLine();
			log.write(read+"\n");
			
			if (read.contains("usermove")) {
				lMove=read.substring(8); // ??
				toMove=(toMove+1)&1;
			}
			if (read.equals("quit")){
	    		;
	    	}
	    	// ce face??
	    	if (read.equals("force")){
	    		;
	    	}
	    	if (read.contains("time")){
	    		time = Integer.parseInt(read.substring(8));//
	    	}
	    	if (read.contains("otim")){
	    		otim = Integer.parseInt(read.substring(8));//
	    	}
		}
	}
}
