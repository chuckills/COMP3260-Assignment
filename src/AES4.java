public class AES4 extends AES
{
    public AES4()
    {
        super();
    }

    public int[][] encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        int[][] outState = round.addKey(inState, 0);

        for(int i = 0; i < 9; i++)
        {
            inState = outState;
            outState = round.subBytes(outState);
            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);

            compareBits(i, inState, outState);
        }

        inState = outState;
        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);


        compareBits(10, inState, outState);

        return outState;
    }

    public int[][] decode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock, true);

        int[][] outState = round.addKey(inState, 0);

        for(int i = 0; i < 9; i++)
        {
            outState = round.shiftRows(outState);
            outState = round.subBytes(outState);

            outState = round.mixCols(outState);
        }

        outState = round.shiftRows(outState);
        outState = round.subBytes(outState);


        return outState;
    }
}
