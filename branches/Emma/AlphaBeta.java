import java.util.*;
public class AlphaBeta {
	public static final int INF = Integer.MAX_VALUE;
	public byte Player;
	public int depth;
	public Board brd;
	public int alpha;
	public int beta;
	
	public AlphaBeta(Board b, int depth, byte Player){
		brd = b;
		this.depth = depth;
		alpha = -INF;
		beta = INF;
		this.Player = Player;
	}
	
	public int alphaBeta(Board brd, int alpha,int beta, int depthleft, byte Play){
		if (depthleft == 0)
			return brd.evaluateBoard2(Play);
		Vector<Move> v = brd.getAllMoves(Play);
		for (int i = 0; i < v.size(); i++){
			Move m = v.get(i);
			Board b = brd.getCopy();
			b.updateBoard(m.getP1(),m.getP2(),(byte)b.W_QUEEN);
			int score = -alphaBeta(b,-beta,-alpha,depthleft-1,(byte)(1-Play));
			if (score >= beta)
				return beta;
			if (score > alpha)
				alpha = score;
		} 
		return alpha;
	}
	
	public Move returnBestMove(){
		int bestMove = -1;
		int best = -INF;
	    Vector<Move> v = brd.getAllMoves(Player);
	    for (int i = 0; i < v.size(); i++){
	    	Move m = v.get(i);
	    	Board b = brd.getCopy();
	    	b.updateBoard(m.getP1(), m.getP2(), (byte)b.W_QUEEN);
	    	if (bestMove == -1)
	    		bestMove = i;
	    	int score = -alphaBeta(b,alpha,beta,depth,(byte)(1-Player));
	    	if (score > best){
	    		bestMove = i;
	    		best = score;
	    	}
	    }
	   if (bestMove != -1)
		   return v.get(bestMove);
	   else return null;
	}
}
