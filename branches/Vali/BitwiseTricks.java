

public class BitwiseTricks {	
	// Array for deBruijn multiplication
	// See https://chessprogramming.wikispaces.com/Bitscan#DeBruijnMultiplation
	private static final int index64[] = {
			63,  0, 58,  1, 59, 47, 53,  2,
			60, 39, 48, 27, 54, 33, 42,  3,
			61, 51, 37, 40, 49, 18, 28, 20,
			55, 30, 34, 11, 43, 14, 22,  4,
			62, 57, 46, 52, 38, 26, 32, 41,
			50, 36, 17, 19, 29, 10, 13, 21,
			56, 45, 25, 31, 35, 16,  9, 12,
			44, 24, 15,  8, 23,  7,  6,  5
	};
	
	// deBruijn's "magical" constant
	private static final long debruijn64 = 0x07EDD5E59A4E28C2L;
	
	// Get the index of the least significant bit set in the bitboard
	public static int bitScanForward(long bitboard) {
		// "mask" will isolate the least significant one bit (LS1B)
		long mask = bitboard & (-bitboard);
		// Returns the index via deBruijn's perfect hashing
		return index64[(int) ((mask * debruijn64 ) >>> 58)];
	}
	
	public static long highestOneBit(long i)
	{
	  i |= (i >> 1);
	  i |= (i >> 2);
	  i |= (i >> 4);
	  i |= (i >> 8);
	  i |= (i >> 16);
	  i |= (i >> 32);
	  return i - (i >>> 1);
	}

}