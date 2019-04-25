public class AES1 extends AES
{
    public AES1()
    {
        super();
    }

    public void encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        int[][] outState = round.addKey(inState, 1);

        for(int i = 0; i < 9; i++)
        {

            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);
            outState = round.addKey(outState, i + 1);
            System.arraycopy(outState, 0, roundBlocks[i+1], 0, outState.length);
        }
        inState = outState;

        outState = round.shiftRows(outState);
        outState = round.addKey(outState, 10);

        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock =  outState;
    }
}
