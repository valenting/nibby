/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication37;

/**
 *
 * @author Costin
 */
public class eval {

    private static final int PAWN_SCORE = 100;
    private static final int KNIGHT_SCORE = 310;
    private static final int BISHOP_SCORE = 305;
    private static final int ROOK_SCORE = 500;
    private static final int QUEEN_SCORE = 850;
    private static final int KING_SCORE = 64000;
    private static final int PieceScore[] = {PAWN_SCORE,ROOK_SCORE, KNIGHT_SCORE, BISHOP_SCORE,
        QUEEN_SCORE, KING_SCORE
    };

    private static final int AttackScore[] = {0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 1, 1, 1, 1, 0,
        0, 1, 8, 8, 8, 8, 1, 0,
        0, 1, 8, 25, 25, 8, 1, 0,
        0, 1, 8, 25, 25, 8, 1, 0,
        0, 1, 8, 8, 8, 8, 1, 0,
        0, 1, 1, 1, 1, 1, 1, 0,
        0, 0, 0, 0, 0, 0, 0, 0,};
    private static final int IsolatedPawnPenalty[] = {10, 12, 14, 18, 18, 14, 12, 10};
    private static final int PiecePosScore[][] = {
        // Pawn scores White
        {
            0, 0, 0, 0, 0, 0, 0, 0,
            20, 26, 26, 28, 28, 26, 26, 20,
            12, 14, 16, 21, 21, 16, 14, 12,
            8, 10, 12, 18, 18, 12, 10, 8,
            4, 6, 8, 16, 16, 8, 6, 4,
            2, 2, 4, 6, 6, 4, 2, 2,
            0, 0, 0, -4, -4, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        },
        // Rook scores White
        {
            10, 10, 10, 10, 10, 10, 10, 10,
            5, 5, 5, 10, 10, 5, 5, 5,
            0, 0, 5, 10, 10, 5, 0, 0,
            0, 0, 5, 10, 10, 5, 0, 0,
            0, 0, 5, 10, 10, 5, 0, 0,
            0, 0, 5, 10, 10, 5, 0, 0,
            0, 0, 5, 10, 10, 5, 0, 0,
            0, 0, 5, 10, 10, 5, 0, 0,},
        // Knight scores White
        {
            -40, -10, - 5, - 5, - 5, - 5, -10, -40,
            - 5, 5, 5, 5, 5, 5, 5, - 5,
            - 5, 5, 10, 15, 15, 10, 5, - 5,
            - 5, 5, 10, 15, 15, 10, 5, - 5,
            - 5, 5, 10, 15, 15, 10, 5, - 5,
            - 5, 5, 8, 8, 8, 8, 5, - 5,
            - 5, 0, 5, 5, 5, 5, 0, - 5,
            -50, -20, -10, -10, -10, -10, -20, -50,},
        // Bishop scores White
        {
            -40, -20, -15, -15, -15, -15, -20, -40,
            0, 5, 5, 5, 5, 5, 5, 0,
            0, 10, 10, 18, 18, 10, 10, 0,
            0, 10, 10, 18, 18, 10, 10, 0,
            0, 5, 10, 18, 18, 10, 5, 0,
            0, 0, 5, 5, 5, 5, 0, 0,
            0, 5, 0, 0, 0, 0, 5, 0,
            -50, -20, -10, -20, -20, -10, -20, -50
        },
        // Queen scores White
        {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 10, 10, 10, 10, 0, 0,
            0, 0, 10, 15, 15, 10, 0, 0,
            0, 0, 10, 15, 15, 10, 0, 0,
            0, 0, 10, 10, 10, 10, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
        },
        // King scores White
        {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            12, 8, 4, 0, 0, 4, 8, 12,
            16, 12, 8, 4, 4, 8, 12, 16,
            24, 20, 16, 12, 12, 16, 20, 24,
            24, 24, 24, 16, 16, 6, 32, 32
        },
        // King end-game scores White
        {
            -30, -5, 0, 0, 0, 0, -5, -30,
            -5, 0, 0, 0, 0, 0, 0, -5,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 5, 5, 0, 0, 0,
            0, 0, 0, 5, 5, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -40, -10, -5, -5, -5, -5, -10, -40
        }
    };


    public int evaluateBoard(Board board, int side) {
        int whiteMaterial = 0, blackMaterial = 0, pos = 0, tip = 0;
        long onePiece, table, remainingPieces;
        
        if (board.isCheckMate((byte) side)) {
            return -20000;
        }
        for (int i = 1; i <= 5; i++) {
            whiteMaterial += PieceScore[i-1] * Long.bitCount(board.pieces[i] & board.color[0]);
            blackMaterial += PieceScore[i-1] * Long.bitCount(board.pieces[i] & board.color[1]);

        }
        // Bonus for bishop pair
        if (Long.bitCount(board.pieces[4] & board.color[0]) == 2) {
            whiteMaterial += 50;
        }
        if (Long.bitCount(board.pieces[4] & board.color[1]) == 2) {
            blackMaterial += 50;
        }
        // Penalty for having no pawns
        if (Long.bitCount(board.pieces[1] & board.color[0]) == 0) {
            whiteMaterial -= 50;
        }
        if (Long.bitCount(board.pieces[1] & board.color[1]) == 0) {
            blackMaterial -= 50;
        }

        if (!board.avoidCheckPosition((byte) side)) {
            if (side == 0) {
                blackMaterial += 1000;
            } else {
                whiteMaterial += 1000;
            }
        }

        // position scores 
        remainingPieces = board.table & board.color[0];
        while (remainingPieces != 0) {
            onePiece = remainingPieces & -remainingPieces;
            pos = Long.numberOfTrailingZeros(onePiece);
            tip = board.getPieceType(pos) & 7;
            remainingPieces -= onePiece;
            whiteMaterial += PiecePosScore[tip - 1][pos];

        }
        remainingPieces = board.table & board.color[1];
        while (remainingPieces != 0) {
            onePiece = remainingPieces & -remainingPieces;
            pos = Long.numberOfTrailingZeros(onePiece);
            tip = board.getPieceType(pos) & 7;
            remainingPieces -= onePiece;
            blackMaterial += PiecePosScore[tip - 1][63 - pos];

        }


        if (side == 0) {
            return whiteMaterial - blackMaterial;
        } else {
            return blackMaterial - whiteMaterial;
        }
    }

}
