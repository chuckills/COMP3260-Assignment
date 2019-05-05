/** Round.java
 *
 * Contains all methods required for the phases of each round
 *
 */
public class Round
{
    private boolean decrypt;
    private static int[][] keySchedule;

    /** Constructor for encoding
     *
     * @param keyBlock - int[][], 4x4 integer key
     */
    public Round(int[][] keyBlock)
    {
        decrypt = false;
        expandKey(keyBlock);
    }

    /** Constructor for encoding or decoding
     *
     * @param keyBlock - int[][], 4x4 integer key
     * @param decrypt - boolean, true if decoding, false if encoding
     */
    public Round(int[][] keyBlock, boolean decrypt)
    {
        this.decrypt = decrypt;
        expandKey(keyBlock);
    }


    /** expandKey()
     *
     * This method derives 44 32 bit key words from the initial keyblock array
     *
     * @param keyBlock - int[][], The initial key array
     */
    private static void expandKey(int[][] keyBlock)
    {
        // Initialise the key schedule array
        keySchedule = new int[44][4];

        // Populate the first four words with the original key
        for(int i = 0; i < 4; i++)
        {
            keySchedule[i] = new int []{keyBlock[0][i], keyBlock[1][i], keyBlock[2][i], keyBlock[3][i]};
        }

        // Initialise a temporary word
        int[] temp = new int[4];

        // Generate the remaining key words
        for(int i = 4; i < 44; i++)
        {
            // Copy the word at the previous index to the temporary word
            System.arraycopy(keySchedule[i-1], 0, temp, 0, 4);

            // Get the Round Constant value on every fourth round
            if(i % 4 == 0)
            {
                int[] rCon = new int[]{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36};

                // Run RotWord on the temp array
                System.arraycopy(rotWord(temp), 0, temp, 0, 4);

                // Run SubWord on the temp array
                System.arraycopy(subWord(temp), 0, temp, 0, 4);

                // XOR the temp array with the Round Constant
                temp = new int[]{temp[0]^rCon[i/4-1], temp[1], temp[2], temp[3]};
            }

            // XOR the temp array with the key word 4 indexes lower
            keySchedule[i] = new int[] {keySchedule[i-4][0]^temp[0], keySchedule[i-4][1]^temp[1], keySchedule[i-4][2]^temp[2], keySchedule[i-4][3]^temp[3]};
        }
    }

    /** rotWord()
     *
     * Performs a 1-byte rotation to the left
     *
     * @param word - int[], the word to be rotated
     * @return - int[], returns word rotated 1-byte to the left
     */
    private static int[] rotWord(int[] word)
    {
        return new int[] {word[1], word[2], word[3], word[0]};
    }

    /** subWord()
     *
     * Performs an SBox substitution on the word
     *
     * @param word - int[], the word to be substituted
     * @return - int[], returns word after an SBox substitution
     */
    private static int[] subWord(int[] word)
    {
        int[] newWord = new int[4];

        for(int j = 0; j < word.length; j++)
        {
            newWord[j] = SBox.SBOX.get(word[j]);
        }

        return newWord;
    }

    /** subBytes()
     *
     * Performs a block SBox/ISBox substitution
     *
     * @param inState - int[][], The input state block array
     * @return - int[][], the output block array after substitution
     */
    public int[][] subBytes(int[][] inState)
    {
        // Choose encrypt or decrypt
        if(!decrypt)
        {
            // Substitute from SBox
            for(int i = 0; i < inState.length; i++)
            {
                for(int j = 0; j < inState[i].length; j++)
                {
                    inState[i][j] = SBox.SBOX.get(inState[i][j]);
                }
            }
        }
        else
        {
            // Substitute from ISBox
            for(int i = 0; i < inState.length; i++)
            {
                for(int j = 0; j < inState[i].length; j++)
                {
                    inState[i][j] = SBox.ISBOX.get(inState[i][j]);
                }
            }
        }

        return inState;
    }

    /** shiftRows()
     *
     * @param inState - int[][], The input state block array
     * @return - int[][], the output block array after row shift
     */
    public int[][] shiftRows(int[][] inState)
    {
        // Choose encode or decode
        if(!decrypt)
        {
            // Shift the rows to the left
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
            // Shift the rows to the right
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

    /** mixCols()
     *
     * @param inState - int[][], The input state block array
     * @return - int[][], the output block array after mixing columns
     */
    public int[][] mixCols(int[][] inState)
    {
        int[][] outState = new int[4][4];

        // Choose encode or decode
        if(!decrypt)
        {
            // Apply the encoding mix matrix to the input state array
            for(int j = 0; j < 4; j++)
            {
                outState[0][j] = multiply(0x02, inState[0][j]) ^ multiply(0x03, inState[1][j]) ^ inState[2][j] ^ inState[3][j];
                outState[1][j] = inState[0][j] ^ multiply(0x02, inState[1][j]) ^ multiply(0x03, inState[2][j]) ^ inState[3][j];
                outState[2][j] = inState[0][j] ^ inState[1][j] ^ multiply(0x02, inState[2][j]) ^ multiply(0x03, inState[3][j]);
                outState[3][j] = multiply(0x03, inState[0][j]) ^ inState[1][j] ^ inState[2][j] ^ multiply(0x02, inState[3][j]);
            }
        }
        else
        {
            // Apply the decoding mix matrix to the input state array
            for(int j = 0; j < 4; j++)
            {
                outState[0][j] = multiply(0x0E, inState[0][j]) ^ multiply(0x0B, inState[1][j]) ^ multiply(0x0D, inState[2][j]) ^ multiply(0x09, inState[3][j]);
                outState[1][j] = multiply(0x09, inState[0][j]) ^ multiply(0x0E, inState[1][j]) ^ multiply(0x0B, inState[2][j]) ^ multiply(0x0D, inState[3][j]);
                outState[2][j] = multiply(0x0D, inState[0][j]) ^ multiply(0x09, inState[1][j]) ^ multiply(0x0E, inState[2][j]) ^ multiply(0x0B, inState[3][j]);
                outState[3][j] = multiply(0x0B, inState[0][j]) ^ multiply(0x0D, inState[1][j]) ^ multiply(0x09, inState[2][j]) ^ multiply(0x0E, inState[3][j]);
            }
        }

        return outState;
    }

    /** addKey()
     *
     * @param inState - int[][], The input state block array
     * @param round - int, the current round number
     * @return - int[][], returns a block after XOR with the round key
     */
    public int[][] addKey(int[][] inState, int round)
    {
        int[][] outState = new int[4][4];
        int keyOffset = 0;

        // Choose encode or decode
        if(!decrypt)
        {

            int[] roundOffset = {0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40};
            keyOffset = roundOffset[round-1];
        }
        else
        {
            int[] roundOffset = {40, 36, 32, 28, 24, 20, 16, 12, 8, 4, 0};
            keyOffset = roundOffset[round-1];
        }

        // Add the round key to the current block
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                outState[i][j] = inState[i][j]^keySchedule[j+keyOffset][i];
            }
        }

        return outState;
    }

    /** multiply()
     *
     * Performs the mixCols matrix multiplication calculations using the shortcut in the text
     *
     * @param multiplier - int, The number of times to multiply by
     * @param value - int, The value to be multiplied
     * @return - int, returns the required result of the multiplication
     */
    private int multiply(int multiplier, int value)
    {
        switch(multiplier)
        {
            case(0x02):
                if(value >= 128)
                {
                    // Shift bits left by 1 to multiply by 2
                    value <<= 1;

                    // XOR with int equivalent to x^8 + x^4 + x^3 + x + 1
                    value ^= 0x11B;
                }
                else
                {
                    // Shift bits left by 1 to multiply by 2
                    value <<= 1;
                }
                break;
            case(0x03):
                // 3 = 1 + 2
                value ^= multiply(2, value);
                break;
            case(0x09):
                // 9 = 1 + 2 x 2 x 2
                value ^= multiply(2, multiply(2, multiply(2, value)));
                break;
            case(0x0B):
                // B = 1 + 2 x (1 + 2 x 2)
                value ^= multiply(2, value ^ multiply(2, multiply(2, value)));
                break;
            case(0x0D):
                // D = 1 + 2 x (2 x (1 + 2))
                value ^= multiply(2, multiply(2, value ^ multiply(2, value)));
                break;
            case(0x0E):
                // E = 2 x (1 + 2 x (1 + 2))
                value = multiply(2, value ^ multiply(2, value ^ multiply(2, value)));
                break;
            default:
        }
        return value;
    }
}
