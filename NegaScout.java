import java.util.*;
public class NegaScout {
	public Board brd;
	public int INF = Integer.MAX_VALUE;
	public int depth;
	public byte Player;
	public int alpha;
	public int beta;
	
	public NegaScout(Board b, byte Player, int depth){
		brd = b;
		alpha = -INF;
		beta = INF;
		this.depth = depth;
		this.Player = Player;
	}
	
	private int negaScout( Board brd, int alpha, int beta, int d,byte Player ){ 
		int a,b;
		if (d == this.depth)
			return brd.evaluateBoard3(brd,Player);
		a = alpha;
	    b = beta;
	    Vector<Move> v = brd.getAllMoves(Player);
	    for (int i = 0; i< v.size(); i++){
	    	Board bd = brd.getCopy();
	    	Move m = v.get(i);
	    	bd.updateBoard(m.getP1(), m.getP2(), (byte)bd.W_QUEEN);
	    	int t = - negaScout(bd,-b,-a,d+1,(byte)(1-Player));
	    	if ( (t > a) && (t < beta) && (i > 0) && (d < this.depth -1))
	    		a = -negaScout(bd,-beta,-t,d+1,(byte)(1-Player));
	    	a = Math.max(a,t);
	    	if (a >= beta)
	    		return a;
	    	b = a + 1;
	    }
	    return a;
	}
	
	public Move returnBestMove(){
		Move bestMove = null;
		int best = -INF;
	    Vector<Move> v = brd.getAllMoves(Player);
	    for (Move m : v){
	    	Board b = brd.getCopy();
	    	b.updateBoard(m.getP1(), m.getP2(), (byte)b.W_QUEEN);
	    	if (bestMove == null)
	    		bestMove = m;
	    	int score = -negaScout(b,alpha,beta,0,(byte)(1-Player));
	    	if (score > best){
	    		bestMove = m;
	    		best = score;
	    	}
	    }
	  return bestMove;
	}

}
