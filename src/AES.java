public abstract class AES
{
    protected int[] avalanche;

    abstract int[][] encode(int[][] plain);
    abstract int[][] decode(int[][] cipher);

    public void expandKey(int[][] key)
    {

    }

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
}
