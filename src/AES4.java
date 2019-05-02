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

    /** encode()
     *
     * Encodes the input block array with the input key array
     *
     * @param inState - int[][], The input state block array
     * @param keyBlock - int[][], The input key block array
     */
    public void encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);

        // Copy initial input state to round 0
        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        // Initial add round key
        int[][] outState = round.addKey(inState, 1);

        // Repeat round phases 10 times
        for(int i = 1; i < 10; i++)
        {
            outState = round.subBytes(outState);
            outState = round.shiftRows(outState);
            outState = round.mixCols(outState);
            // addKey() removed

            // Copy round output to round block array
            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);
        // addKey() removed

        // Copy final round output to round block array
        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock = outState;
    }
}
