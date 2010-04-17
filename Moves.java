import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

import org.omg.CORBA.NVList;


class Move implements Comparable {
	private int p1,p2;
	
	public Move(String str) {
		p1=Usual.pos1(str);
		p2=Usual.pos2(str);
	}
	
	public Move(int pos1, int pos2){
		p1=pos1;
		p2=pos2;
	}
	
	public int getP1() {
		return p1;
	}
	
	public int getP2() {
		return p2;
	}
	
	public long getLongP1() {
		return 1L<<p1;
	}
	
	public long getLongP2() {
		return 1L<<p2;
	}
	
	public boolean equals(Object obj) {
		Move move2 = (Move) obj;
		return move2.getP1() == p1 && move2.getP2() == p2;
	}
	
	static char colo[] = {'a','b','c','d','e','f','g','h'};
	
	public String toString() {
		
		return Usual.stringPos(p1)+Usual.stringPos(p2);
	}
	@Override
	public int compareTo(Object arg0) {
		Move move2 = (Move) arg0;
		if  (move2.getP1() == p1 && move2.getP2() == p2)
			return 0;
		return 1;
	}
}

class Node implements Comparable{
	private Move move;
	private Vector<Node> moves;// = new Vector<Node>();
	public Node(Move mv){
		move = mv;
		moves = new Vector<Node>();
	} 
	
	public Node() {
		move = null;//new Move("iiii");
		moves = new Vector<Node>();
	}
	
	public Node addChild(Move mv){
		for (int i=0;i<moves.size();i++)
			if (moves.get(i).getMove().equals(mv))
				return moves.get(i);
		//if (moves.contains(new Node(mv))) 
			//return moves.elementAt(moves.indexOf(new Node(mv)));
		moves.add(new Node(mv));
		return moves.lastElement();
	}

	public Move getMove() {
		return move;
	}
	@Override
	public int compareTo(Object o) {
		Node nod = (Node) o;
		return move.compareTo(nod.getMove());
	}
	public Vector<Node> getChildren() {
		return moves;
	}
}

class MoveTree {
	private Node root = new Node();
	MoveTree(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line=reader.readLine()) != null) {
			Node nod = root;
			while (line.length()>=4) {
				String move = line.substring(0,4);
				nod = nod.addChild(new Move(move));
				line = line.substring(4);
			}
			
		}
	}
	public void addTree(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line;
		while ((line=reader.readLine()) != null) {
			Node nod = root;
			while (line.length()>=4) {
				String move = line.substring(0,4);
				nod = nod.addChild(new Move(move));
				line = line.substring(4);
			}
			
		}
	}
	public Node getRoot() {
		return root;
	}
}

class Openings {
	static MoveTree whiteMoves,blackMoves;
	static Node wcurrent,bcurrent;
	static boolean wvalid,bvalid;
	static void init() throws IOException {
		MoveTree whiteMoves = new MoveTree("pulsarCrazyWhite.txt");
		whiteMoves.addTree("atomicBookWhite.txt");
		MoveTree blackMoves = new MoveTree("pulsarCrazyBlack.txt");
		blackMoves.addTree("atomicBookBlack.txt");
		wcurrent=whiteMoves.getRoot();
		bcurrent=blackMoves.getRoot();
		wvalid=true;
		bvalid=true;
	}
	
	public static void reset() {
		wcurrent=whiteMoves.getRoot();
		bcurrent=blackMoves.getRoot();
		wvalid=true;
		bvalid=true;
	}
	public static boolean hasNext(byte color) {
		if (color==0)
			return wcurrent.getChildren().size()!=0 && wvalid;
		return bcurrent.getChildren().size()!=0 && bvalid;
	}
	public static Move getMove(byte color) {
		if (color==0) {
			int n = wcurrent.getChildren().size();
			Random r = new Random(n);
			Node next = wcurrent.getChildren().elementAt(r.nextInt(n));
			wcurrent=next;
			return next.getMove();
		}
		int n = bcurrent.getChildren().size();
		Random r = new Random(n);
		Node next = bcurrent.getChildren().elementAt(r.nextInt(n));
		bcurrent = next;
		return next.getMove();
	}
	public static void updateMove(Move m,byte color) {
		if (color==0) {
			if (wcurrent.getChildren().contains(new Node(m))) 
				wcurrent=wcurrent.getChildren().elementAt(wcurrent.getChildren().indexOf(m));
			else wvalid=false;
			return;
		}
		if (bcurrent.getChildren().contains(new Node(m))) 
			bcurrent=bcurrent.getChildren().elementAt(bcurrent.getChildren().indexOf(m));
		else bvalid=false;
	}
}

public class Moves {

	public static void Afis(FileWriter a,Node nod, String indent) throws IOException{
		Move mv = nod.getMove();
		if (mv!=null )
			a.write(indent+nod.getMove().toString()+"\n");
		Vector<Node> v = nod.getChildren();
		for (int i=0;i<v.size();i++)
			Afis(a,v.elementAt(i),indent+"\t");
	}
	public static void main(String args[]) throws IOException{
		MoveTree wtree = new MoveTree("pulsarCrazyWhite.txt");
		wtree.addTree("atomicBookWhite.txt");
		MoveTree btree = new MoveTree("pulsarCrazyBlack.txt");
		btree.addTree("atomicBookBlack.txt");
		FileWriter w = new FileWriter(new File("arbw.txt"));
		Afis(w,wtree.getRoot(),"");
		w.close();
		FileWriter b = new FileWriter(new File("arbb.txt"));
		Afis(b,wtree.getRoot(),"");
		b.close();
		
	}
}
