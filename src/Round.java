public class Round
{
    private boolean decrypt;
    private static int[][] keySchedule;

    public Round(int[][] keyBlock)
    {
        decrypt = false;
        expandKey();
    }

    public Round(int[][] keyBlock, boolean decrypt)
    {
        this.decrypt = decrypt;
        expandKey();
    }

    private static void expandKey()
    {

    }

    public int[][] subBytes(int[][] inState)
    {
        int[][] outState = inState;

        if(!decrypt)
        {

        }
        else
        {

        }

        return outState;
    }

    /**
     *
     * @param inState
     * @return
     */
    public int[][] shiftRows(int[][] inState)
    {
        /*int[][] outState = new int[4][4];
        System.arraycopy(inState[0], 0, outState[0], 0, 4);*/
        int swap;
        if(!decrypt)
        {

            for(int i = 0; i < 3; i++)
            {
                int[] shift = new int[4];

                shift[0] = inState[i+1][(i+1)%4];
                shift[1] = inState[i+1][(i+2)%4];
                shift[2] = inState[i+1][(i+3)%4];
                shift[3] = inState[i+1][i%4];

                inState[i+1] = shift;
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
            {
                int[] shift = new int[4];

                shift[(i+1)%4] = inState[i+1][0];
                shift[(i+2)%4] = inState[i+1][1];
                shift[(i+3)%4] = inState[i+1][2];
                shift[i%4] = inState[i+1][3];

                inState[i+1] = shift;
            }
        }

        return inState;
    }

    public int[][] mixCols(int[][] inState)
    {
        int[][] outState = inState;

        if(!decrypt)
        {

        }
        else
        {

        }

        return outState;
    }

    public int[][] addKey(int[][] inState, int round)
    {
        int[][] outState = inState;

        if(!decrypt)
        {

        }
        else
        {

        }

        return outState;
    }
}
