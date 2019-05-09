/*
======================================================
COMP3260 Assignment 2
Gregory Choice(c9311718) & Christopher Booth(c3229932)
======================================================
*/

/** AES.java
 *
 * Base class for all variations of AES encoding algorithms.
 * Contains abstract header for encoding algorithm and all
 * other shared methods for derived classes.
 *
 */
public abstract class AES
{
    protected int[][] outBlock;
    protected int[] avalanche;
    protected int[][][] roundBlocks;

    /** Constructor
     *
     */
    public AES()
    {
        avalanche = new int[11];
        roundBlocks = new int [11][4][4];
    }

    /** encode()
     *
     * Encodes the input block array with the input key array
     *
     * @param inState - int[][], The input state block array
     * @param keyBlock - int[][], The input key block array
     */
    public abstract void encode(int[][] inState, int[][] keyBlock);

    /** decode()
     *
     * Decodes the input block array with the input key array
     *
     * @param inState - int[][], The input state block array
     * @param keyBlock - int[][], The input key block array
     */
    public void decode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock, true);

        // Initial add round key
        int[][] outState = round.addKey(inState, 1);

        // Repeat round phases 10 times
        for(int i = 1; i < 10; i++)
        {
            outState = round.shiftRows(outState);
            outState = round.subBytes(outState);
            outState = round.addKey(outState, i + 1);
            outState = round.mixCols(outState);
        }

        // Run final round
        outState = round.shiftRows(outState);
        outState = round.subBytes(outState);
        outState = round.addKey(outState, 11);

        outBlock = outState;
    }

    /** compareBits()
     *
     * Counts the number of bits that differ between two block arrays
     *
     * @param round - int, the current round number
     * @param block - int[][], the input block array
     * @param blockOne - int[][], the block to compare
     */
    public void compareBits(int round, int[][] block, int[][] blockOne)
    {
        for (int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                // XOR corresponding cells and count the number of 1's
                avalanche[round] += Integer.bitCount(block[i][j] ^ blockOne[i][j]);
            }
        }
    }

    /** getAvalanche()
     *
     * Gets the array of differences for this version of the algorithm
     *
     * @return - int[], an array of difference counts for each round
     */
    public int[] getAvalanche()
    {
        return avalanche;
    }

    /** getRoundBlock
     *
     * Gets the block that is the result of the specified round number
     *
     * @param round - int, the current round number
     * @return - int[][], returns the resultant block array from the specified round number
     */
    public int[][] getRoundBlock(int round)
    {
        return roundBlocks[round];
    }

    /** getOutBlock()
     *
     * Gets the final block to return from the algorithm
     *
     * @return - int[][], returns the final resultant array of the algorithm
     */
    public int[][] getOutBlock()
    {
        return outBlock;
    }

    /** blockToBinary()
     *
     * Converts the block array to a binary string
     *
     * @param block - int[][], the input block array
     * @return - String, returns a 128 bit binary string
     */
    public static String blockToBinary(int[][] block)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                sb.append(String.format("%8s", Integer.toBinaryString(block[j][i])).replace(' ', '0'));
            }
        }

        return sb.toString();
    }

    /** blockToHex()
     *
     * Converts the block array to a hexadecimal string
     *
     * @param block - int[][], the input block array
     * @return - String returns a 128 bit hexadecimal string
     */
    public static String blockToHex(int[][] block)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                sb.append(String.format("%2s", Integer.toHexString(block[j][i])).replace(' ', '0'));
            }
        }

        return sb.toString();
    }
}
