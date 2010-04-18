
public class dePusInBoard {

    public int evaluateBoard(Board board, int side) {
        int whiteMaterial = 0, blackMaterial = 0;
        int[] values = {0, 100, 500, 300, 300, 900};
        for (int i = 1; i <= 5; i++) {
            whiteMaterial += values[i] * Long.bitCount(pieces[i] & color[0]);
            blackMaterial += values[i] * Long.bitCount(pieces[i] & color[1]);

        }
        // Bonus for bishop pair
        if (Long.bitCount(pieces[4] & color[0]) == 2) {
            whiteMaterial += 50;
        }
        if (Long.bitCount(pieces[4] & color[1]) == 2) {
            blackMaterial += 50;
        }
        // Penalty for having no pawns
        if (Long.bitCount(pieces[1] & color[0]) == 0) {
            whiteMaterial -= 50;
        }
        if (Long.bitCount(pieces[1] & color[1]) == 0) {
            blackMaterial -= 50;
        }
		
	if (isCheckMate(side)
			return 	-20000;
	
	if (!avoidCheckPosition(side))
	if (side ==0)
        blackMaterial+=1000;
	else
		whiteMaterial+=1000;
        
        
        if (side == 0) {
            return whiteMaterial - blackMaterial;
        } else {
            return blackMaterial - whiteMaterial;
        }
    }

}
