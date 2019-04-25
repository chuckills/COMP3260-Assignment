public class AES0 extends AES
{
    public AES0()
    {
        super();
    }

    public void encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        int[][] outState = round.addKey(inState, 1);
        /*System.out.println();

        for(int i = 0; i < 4; i++)
            System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", outState[i][0], outState[i][1], outState[i][2], outState[i][3]));*/

        for(int i = 0; i < 9; i++)
        {
            outState = round.subBytes(outState);
            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);
            outState = round.addKey(outState, i + 1);
            /*if(i==0)
            {
                System.out.println();

                for(int j = 0; j < 4; j++)
                    System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));
            }*/

            System.arraycopy(outState, 0, roundBlocks[i+1], 0, outState.length);
        }

        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);
        outState = round.addKey(outState, 10);

        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock = outState;
    }
 }
