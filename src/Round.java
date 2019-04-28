/**
 *
 */
public class Round
{
    private boolean decrypt;
    private static int[][] keySchedule;

    /**
     *
     * @param keyBlock
     */
    public Round(int[][] keyBlock)
    {
        decrypt = false;
        expandKey(keyBlock);
    }

    /**
     *
     * @param keyBlock
     * @param decrypt
     */
    public Round(int[][] keyBlock, boolean decrypt)
    {
        this.decrypt = decrypt;
        expandKey(keyBlock);
    }


    /**
     *
     * @param keyBlock
     */
    private static void expandKey(int[][] keyBlock)
    {
        keySchedule = new int[44][4];
        for(int i = 0; i < 4; i++)
        {
            keySchedule[i] = new int []{keyBlock[0][i], keyBlock[1][i], keyBlock[2][i], keyBlock[3][i]};
        }

        int[] temp = new int[4];
        for(int i = 4; i < 44; i++)
        {
            System.arraycopy(keySchedule[i-1], 0, temp, 0, 4);

            if(i % 4 == 0)
            {
                int[] rCon = new int[]{0x00, 0x00, 0x00, 0x00};
                switch(i)
                {
                    case(4):
                        rCon[0] = 0x01;
                        break;
                    case(8):
                        rCon[0] = 0x02;
                        break;
                    case(12):
                        rCon[0] = 0x04;
                        break;
                    case(16):
                        rCon[0] = 0x08;
                        break;
                    case(20):
                        rCon[0] = 0x10;
                        break;
                    case(24):
                        rCon[0] = 0x20;
                        break;
                    case(28):
                        rCon[0] = 0x40;
                        break;
                    case(32):
                        rCon[0] = 0x80;
                        break;
                    case(36):
                        rCon[0] = 0x1B;
                        break;
                    case(40):
                        rCon[0] = 0x36;
                        break;
                }

                System.arraycopy(rotWord(temp), 0, temp, 0, 4);

                System.arraycopy(subWord(temp), 0, temp, 0, 4);

                temp = new int[]{temp[0]^rCon[0], temp[1]^rCon[1], temp[2]^rCon[2], temp[3]^rCon[3],};

            }
            keySchedule[i] = new int[] {keySchedule[i-4][0]^temp[0], keySchedule[i-4][1]^temp[1], keySchedule[i-4][2]^temp[2], keySchedule[i-4][3]^temp[3],};
        }
    }

    /**
     *
     * @param word
     * @return
     */
    private static int[] rotWord(int[] word)
    {
        return new int[] {word[1], word[2], word[3], word[0]};
    }

    /**
     *
     * @param word
     * @return
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

    /**
     *
     * @param inState
     * @return
     */
    public int[][] subBytes(int[][] inState)
    {
        if(!decrypt)
        {
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

    /**
     *
     * @param inState
     * @return
     */
    public int[][] shiftRows(int[][] inState)
    {
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

    /**
     *
     * @param inState
     * @return
     */
    public int[][] mixCols(int[][] inState)
    {
        int[][] outState = new int[4][4];

        if(!decrypt)
        {
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

    /**
     *
     * @param inState
     * @param round
     * @return
     */
    public int[][] addKey(int[][] inState, int round)
    {
        int[][] outState = new int[4][4];
        int keyOffset = 0;
        if(!decrypt)
        {
            switch(round)
            {
                case(1):
                    keyOffset = 0;
                    break;
                case(2):
                    keyOffset = 4;
                    break;
                case(3):
                    keyOffset = 8;
                    break;
                case(4):
                    keyOffset = 12;
                    break;
                case(5):
                    keyOffset = 16;
                    break;
                case(6):
                    keyOffset = 20;
                    break;
                case(7):
                    keyOffset = 24;
                    break;
                case(8):
                    keyOffset = 28;
                    break;
                case(9):
                    keyOffset = 32;
                    break;
                case(10):
                    keyOffset = 36;
                    break;
                case(11):
                    keyOffset = 40;
                    break;

            }
        }
        else
        {
            switch(round)
            {
                case(1):
                    keyOffset = 40;
                    break;
                case(2):
                    keyOffset = 36;
                    break;
                case(3):
                    keyOffset = 32;
                    break;
                case(4):
                    keyOffset = 28;
                    break;
                case(5):
                    keyOffset = 24;
                    break;
                case(6):
                    keyOffset = 20;
                    break;
                case(7):
                    keyOffset = 16;
                    break;
                case(8):
                    keyOffset = 12;
                    break;
                case(9):
                    keyOffset = 8;
                    break;
                case(10):
                    keyOffset = 4;
                    break;
                case(11):
                    keyOffset = 0;
                    break;
            }
        }

        // Commented section used for testing only //

/////////////////////////////////
        /*StringBuilder tempSB = new StringBuilder(String.format("KS%1$3s- ", round-1));
        for(int i = 0; i < 4; i++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", keySchedule[keyOffset][i], keySchedule[keyOffset + 1][i], keySchedule[keyOffset + 2][i], keySchedule[keyOffset + 3][i]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                outState[i][j] = inState[i][j]^keySchedule[j+keyOffset][i];
            }
        }

        return outState;
    }

    /**
     *
     * @param multiplier
     * @param value
     * @return
     */
    private int multiply(int multiplier, int value)
    {
        switch(multiplier)
        {
            case(0x02):
                if(value >= 128)
                {
                    value <<= 1;
                    value -= 256;
                    value ^= 0x1B;
                }
                else
                {
                    value <<= 1;
                }
                break;
            case(0x03):
                value ^= multiply(2, value);
                break;
            case(0x09):
                value ^= multiply(2, multiply(2, multiply(2, value)));
                break;
            case(0x0B):
                value ^= multiply(2, value ^ multiply(2, multiply(2, value)));
                break;
            case(0x0D):
                value ^= multiply(2, multiply(2, value ^ multiply(2, value)));
                break;
            case(0x0E):
                value = multiply(2, value ^ multiply(2, value ^ multiply(2, value)));
                break;
            default:
        }
        return value;
    }
}
