public class AES0 extends AES
{
    public AES0()
    {
        super();
    }

    public int[][] encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        int[][] outState = round.addKey(inState, 1);

        for(int i = 0; i < 9; i++)
        {
            inState = outState;
            outState = round.subBytes(outState);
            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);
            outState = round.addKey(outState, i + 1);
            //compareBits(i+1, inState, outState);
        }

        inState = outState;
        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);
        outState = round.addKey(outState, 10);

        //compareBits(10, inState, outState);

        return outState;
    }

    public int[][] decode(int[][] inState, int[][] keyBlock)
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

        return outState;
    }
 }
