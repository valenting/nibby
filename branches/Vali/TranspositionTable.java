import java.util.*;

public class TranspositionTable {
	// valorile hash ale tablei carora le corespunde intrarea
    private long[] stateHash;
    // score-ul pentru starea careia ii corespunde
    private short[] score;
    // adancimea la care a fost cautata starea de pe pozitia corespunzatoare
    private byte[] depth;
    // indica daca score este exact, o limita sau invalid
    private byte[] flag;
    // o valoare hash pentru cea mai buna mutare
    private short[] bestMoveHash;
    // numarul de elemente din TranspositionTable
    private int capacity;

    // construieste o noua TranspositionTable de capacitate data
	public TranspositionTable(int capacity)
	{
        this.capacity = capacity;
        this.stateHash = new long[capacity];
        this.score = new short[capacity];
        this.depth = new byte[capacity];
        this.flag = new byte[capacity];
        this.bestMoveHash = new short[capacity];
	}
	public void store(long stateHash, int depth, int score, int flag, short bestMoveHash)
	{
        int index = index(stateHash);
        this.stateHash[index] = stateHash;
        this.depth[index] = (byte)depth;
        this.score[index] = (short)score;
        this.flag[index] = (byte)flag;
        this.bestMoveHash[index] = bestMoveHash;
	}

    public boolean exists(long hash)
    {
        return (stateHash[index(hash)]==hash);
    }

    public short getScore(long hash) {
        return score[index(hash)];
    }

    public byte getDepth(long hash) {
        return depth[index(hash)];
    }

    public byte getFlag(long hash) {
        return flag[index(hash)];
    }

    public short getBestMoveHash(long hash) {
        return bestMoveHash[index(hash)];
    }
	
	public int size()
	{
		return this.capacity;
	}

    private int index(long hash)
    {
        return (int)(Math.abs(hash) % this.capacity);
    }
		
	
}
