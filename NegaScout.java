import java.util.*;
public class NegaScout {
	public static final int INF = Integer.MAX_VALUE;
	public byte Player;
	public int depth;
	public Board brd;
	public int alpha;
	public int beta;
	
	public NegaScout(Board b, int depth, byte Player){
		brd = b;
		this.depth = depth;
		alpha = -INF;
		beta = INF;
		this.Player = Player;
	}
	
	public int negaScout(Board brd, int alpha,int beta, int depthleft, byte Play){
		if (depthleft == 0)
			return brd.evaluateBoard3(brd,Play);
		Vector<Move> v = brd.getAllMoves(Play);
		int scoutWindow = beta, alpha2 = alpha;
		for (int i = 0; i < v.size(); i++){
			Move m = v.get(i);
			Board b = brd.getCopy();
			b.updateBoard(m.getP1(),m.getP2(),(byte)b.W_QUEEN);
			int score = -negaScout(b,-scoutWindow,-alpha2,depthleft-1,(byte)(1-Play));
			if (score > alpha2)
				alpha2 = score;
			if (alpha2 >= beta)
				return alpha2;
			if (alpha2>=scoutWindow) {
				alpha2 = -negaScout(b,-beta,-alpha2, depthleft - 1,(byte)(1-Play));
				if (alpha2>= beta)
					return alpha2;
			}
			scoutWindow = alpha2 + 1;
		} 
		return alpha2;
	}
	
	public Move returnBestMove(){
		Move bestMove = null;
		int best = -INF;
	    Vector<Move> v = brd.getAllMoves(Player);
	    for (Move m : v){
	    	//Move m = v.get(i);
	    	Board b = brd.getCopy();
	    	b.updateBoard(m.getP1(), m.getP2(), (byte)brd.W_QUEEN);
	    	if (bestMove == null)
	    		bestMove = m;
	    	int score = -negaScout(b,alpha,beta,depth-1,(byte)(1-Player));
	    	if (score > best){
	    		bestMove = m;
	    		best = score;
	    	}
	    }
	   return bestMove;
	}
}
