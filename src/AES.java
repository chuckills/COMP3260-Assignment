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

    /**
     *
     * @param inState
     * @param keyBlock
     */
    public abstract void encode(int[][] inState, int[][] keyBlock);

    /**
     *
     * @param inState
     * @param keyBlock
     */
    public void decode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock, true);

        int[][] outState = round.addKey(inState, 1);

        for(int i = 1; i < 10; i++)
        {
            outState = round.shiftRows(outState);
            outState = round.subBytes(outState);
            outState = round.addKey(outState, i + 1);
            outState = round.mixCols(outState);
        }

        outState = round.shiftRows(outState);
        outState = round.subBytes(outState);
        outState = round.addKey(outState, 11);

        outBlock = outState;
    }

    /**
     *
     * @param round
     * @param block
     * @param blockOne
     */
    public void compareBits(int round, int[][] block, int[][] blockOne)
    {
        String binaryDifference;

        for (int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                binaryDifference = Integer.toBinaryString(block[i][j] ^ blockOne[i][j]);
                for(int k = 0; k < binaryDifference.length(); k++)
                {
                    if(binaryDifference.charAt(k) == '1')
                    {
                        avalanche[round]++;
                    }
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public int[] getAvalanche()
    {
        return avalanche;
    }

    /**
     *
     * @param round
     * @return
     */
    public int[][] getRoundBlock(int round)
    {
        return roundBlocks[round];
    }

    /**
     *
     * @return
     */
    public int[][] getOutBlock()
    {
        return outBlock;
    }

    /**
     *
     * @param block
     * @return
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

    /**
     *
     * @param block
     * @return
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
