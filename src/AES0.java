/** AES0.java
 *
 * Derived AES class containing all round phases
 *
 */
public class AES0 extends AES
{
    /** Constructor
     *
     */
    public AES0()
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
            outState = round.addKey(outState, i + 1);

            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);
        outState = round.addKey(outState, 11);

        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock = outState;
    }
 }