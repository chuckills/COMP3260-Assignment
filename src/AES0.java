/*
======================================================
COMP3260 Assignment 2
Gregory Choice(c9311718) & Christopher Booth(c3229932)
======================================================
*/

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
            outState = round.addKey(outState, i + 1);

            // Copy round output to round block array
            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        // Run final round
        outState = round.subBytes(outState);
        outState = round.shiftRows(outState);
        outState = round.addKey(outState, 11);

        // Copy final round output to round block array
        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

        outBlock = outState;
    }
 }