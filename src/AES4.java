/** AES4.java
 *
 * Derived AES class with addKey missing
 *
 */
public class AES4 extends AES
{
    /** Constructor
     *
     */
    public AES4()
    {
        super();
    }

    /**
     *
     * @param inState
     * @param keyBlock
     */
    public void encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        int[][] outState = round.addKey(inState, 1);

        for(int i = 1; i < 10; i++)
        {
            outState = round.subBytes(outState);
            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);


            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);


        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock = outState;
    }
}
