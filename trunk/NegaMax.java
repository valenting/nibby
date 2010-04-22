import java.util.*;

public class NegaMax {
	public static final int INF = Integer.MAX_VALUE;
	public byte Player;
	public int depth;
	public Board brd;
	
	public NegaMax( Board brd, byte Player, int depth){
			this.Player = Player;
			this.depth = depth;
			this.brd = brd;
	}
	
	public int negamax(Board brd,byte Play,int d){
		if ( d == 0)
			return brd.evaluateBoard(Play);
		int max = -INF;
		Vector<Move> v = brd.getAllMoves(Play);
		for (int i = 0; i < v.size(); i++){
			Move m = v.get(i); 
			Board b = brd.getCopy();
			b.updateBoard(m.getP1(),m.getP2(),(byte)b.W_QUEEN);
			int score = -negamax(b,(byte)(1-Play), d-1);
			if (score > max)
				max = score;
		}
	return max;
	}
	
	public Move returnBestMove(){
		Move bestMove = null;
		int best = -INF;
	    Vector<Move> v = brd.getAllMoves(Player);
	    for (Move m : v){
	    	Board b = brd.getCopy();
	    	//b.printBoard();
	    	b.updateBoard(m.getP1(), m.getP2(), (byte)b.W_QUEEN);
	    	if (bestMove == null)
	    		bestMove = m;
	    	int score = -negamax(b,(byte)(1-Player),depth);
	    	if (score > best){
	    		bestMove = m;
	    		best = score;
	    	}
	    }
	   return bestMove;
	}
	
}
