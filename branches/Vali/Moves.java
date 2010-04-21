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

	@Override 
	public boolean equals(Object n) {
		return move.equals(((Node) n).getMove());
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
	private static MoveTree tree;
	private static Node current;
	private static boolean valid;
	public static void reset() {
		tree = null;
		valid = false;
	}
	public static boolean isSet() {
		return tree!=null;
	}
	public static void init(int color) throws IOException {
		if (color==0) {
			tree = new MoveTree("pulsarCrazyWhite.txt");
			//tree.addTree("atomicBookWhite.txt");
			valid=true;
			current = tree.getRoot();
			} else { 
			tree = new MoveTree("pulsarCrazyBlack.txt");
			//tree.addTree("atomicBookBlack.txt");
			valid=true;
			current = tree.getRoot();
			}
	}
	public static Move getMove() {
		if (tree==null || valid==false)
			return null;
		if (current.getChildren().size()==0) {
			valid=false;
			return null;
		}
		Random r = new Random(System.currentTimeMillis());
		Node n = current.getChildren().elementAt(r.nextInt(current.getChildren().size()));
		//current = n;
		return n.getMove();
	}
	public static void makeMove(Move m) {
		if (current.getChildren().contains(new Node(m))) {
			current = current.getChildren().elementAt(current.getChildren().indexOf(new Node(m)));
		} else valid=false;
	}
	public static boolean hasNext() {
		return valid && current!=null && isSet();
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
