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
        expandKey();
    }

    /**
     *
     * @param keyBlock
     * @param decrypt
     */
    public Round(int[][] keyBlock, boolean decrypt)
    {
        this.decrypt = decrypt;
        expandKey();
    }

    /**
     *
     */
    private static void expandKey()
    {

    }

    /**
     *
     * @param inState
     * @return
     */
    public int[][] subBytes(int[][] inState)
    {
        int row;
        int col;

        if(!decrypt)
        {
            for(int i = 0; i < inState.length; i++)
            {
                for(int j = 0; j < inState[i].length; j++)
                {
                    String address = String.format("%1$02X", inState[i][j]);
                    row = Integer.valueOf(address.substring(0, 1), 16);
                    col = Integer.valueOf(address.substring(1), 16);
                    inState[i][j] = SBox.SBOX[row][col];
                }
            }
        }
        else
        {
            for(int i = 0; i < inState.length; i++)
            {
                for(int j = 0; j < inState[i].length; j++)
                {
                    String address = String.format("%1$02X", inState[i][j]);
                    row = Integer.valueOf(address.substring(0, 1), 16);
                    col = Integer.valueOf(address.substring(1), 16);
                    inState[i][j] = SBox.I_SBOX[row][col];
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
        int[][] outState = inState;

        if(!decrypt)
        {

        }
        else
        {

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
                //x×9=(((x×2)×2)×2)+x
                value ^= multiply(2, multiply(2, multiply(2, value)));
                break;
            case(0x0B):
                //x×11=((((x×2)×2)+x)×2)+x
                value ^= multiply(2, value ^ multiply(2, multiply(2, value)));
                break;
            case(0x0D):
                //x×13=((((x×2)+x)×2)×2)+x
                value ^= multiply(2, multiply(2, value ^ multiply(2, value)));
                break;
            case(0x0E):
                //x×14=((((x×2)+x)×2)+x)×2
                value = multiply(2, value ^ multiply(2, value ^ multiply(2, value)));
                break;
            default:
        }
        return value;
    }
}
