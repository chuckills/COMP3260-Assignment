public abstract class AES
{

    protected int[][] outBlock;
    protected int[] avalanche;
    protected int[][][] roundBlocks;

    public AES()
    {
        avalanche = new int[11];
        roundBlocks = new int [11][4][4];
    }

    public abstract void encode(int[][] inState, int[][] keyBlock);

    public void decode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock, true);

        int[][] outState = round.addKey(inState, 1);

        for(int i = 0; i < 9; i++)
        {
            outState = round.shiftRows(outState);
            outState = round.subBytes(outState);
            outState = round.addKey(outState, i + 1);
            outState = round.mixCols(outState);
        }

        outState = round.shiftRows(outState);
        outState = round.subBytes(outState);
        outState = round.addKey(outState, 10);

        outBlock = outState;
    }

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

    public int[] getAvalanche()
    {
        return avalanche;
    }

    public int[][] getRoundBlock(int round)
    {
        return roundBlocks[round];
    }

    public int[][] getOutBlock()
    {
        return outBlock;
    }

    public String blockToBinary()
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                sb.append(String.format("%8s", Integer.toBinaryString(outBlock[i][j])).replace(' ', '0'));
            }
        }

        return sb.toString();
    }

    public static String blockToHex(int[][] block)
    {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                sb.append(String.format("%2s", Integer.toHexString(block[i][j])).replace(' ', '0'));
            }
        }

        return sb.toString();
    }
}
