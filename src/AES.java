public abstract class AES
{
    protected int[] avalanche;

    public AES()
    {
        avalanche = new int[10];
    }

    abstract int[][] encode(int[][] inState, int[][] keyBlock);
    abstract int[][] decode(int[][] inState, int[][] keyBlock);

    public void compareBits(int round, int[][] preRound, int[][] postRound)
    {
        String binaryDifference;

        for (int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                binaryDifference = Integer.toBinaryString(preRound[i][j] ^ postRound[i][j]);
                for(int k = 0; k < binaryDifference.length(); k++)
                {
                    if(binaryDifference.charAt(k) == '1')
                    {
                        avalanche[round-1]++;
                    }
                }
            }
        }
    }

    public int[] getAvalanche()
    {
        return avalanche;
    }
}
