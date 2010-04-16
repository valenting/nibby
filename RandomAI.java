import java.util.*;

public class RandomAI {

	public Board brd;
	public RandomAI(Board b){
		this.brd = b;
	}
	
	public String move(byte side){
		Random r = new Random();
		int position = 0;;
		long onePiece = 0L,allMoves=0L,oneMove=0L;
		int i = 10000;
		if (brd.checkmate)
			return "resign";
		while(i>0){
			r = new Random();
			position = r.nextInt(64);
			onePiece = 1L << position;
			if((onePiece & color[side]) != 0L){//este piesa proprie
				
				allMoves = brd.getValidMoves(position);
				if(allMoves != 0L){	//	exista mutari valide pentru piesa selectata
					while(allMoves != 0L){
						oneMove = Long.highestOneBit(allMoves);
						allMoves ^= oneMove;
						if(r.nextInt(12)<2)
							break;
					}
				return "move " + brd.intermediaryToSANMove(onePiece,oneMove);			
					
				}
			}
			i--;
		}
		return "resign";
	}
}
