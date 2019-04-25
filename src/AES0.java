/**
 *
 */
public class AES0 extends AES
{
    /**
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

        // The commented sections are used for testing only //

/////////////////////////////////
        //StringBuilder tempSB = new StringBuilder("Start- ");
/////////////////////////////////

        Round round = new Round(keyBlock);

/////////////////////////////////
        /*for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", inState[0][j], inState[1][j], inState[2][j], inState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        int[][] outState = round.addKey(inState, 1);

/////////////////////////////////
        /*tempSB = new StringBuilder("AK  0- ");
        for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        for(int i = 1; i < 10; i++)
        {
            outState = round.subBytes(outState);

/////////////////////////////////
            /*tempSB = new StringBuilder(String.format("SB%1$3s- ", i));
            for(int j = 0; j < 4; j++)
                tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
            System.out.println(tempSB.toString());*/
/////////////////////////////////

            outState = round.shiftRows(outState);

/////////////////////////////////
            /*tempSB = new StringBuilder(String.format("SR%1$3s- ", i));
            for(int j = 0; j < 4; j++)
                tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
            System.out.println(tempSB.toString());*/
/////////////////////////////////

            outState = round.mixCols(outState);

/////////////////////////////////
            /*tempSB = new StringBuilder(String.format("MC%1$3s- ", i));
            for(int j = 0; j < 4; j++)
                tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
            System.out.println(tempSB.toString());*/
/////////////////////////////////

            outState = round.addKey(outState, i + 1);

/////////////////////////////////
            /*tempSB = new StringBuilder(String.format("AK%1$3s- ", i));
            for(int j = 0; j < 4; j++)
                tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
            System.out.println(tempSB.toString());*/
/////////////////////////////////

            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        outState = round.subBytes(outState);

/////////////////////////////////
        /*tempSB = new StringBuilder("SB 10- ");
        for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        outState = round.shiftRows(outState);

/////////////////////////////////
        /*tempSB = new StringBuilder("SR 10- ");
        for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        outState = round.addKey(outState, 11);

/////////////////////////////////
        /*tempSB = new StringBuilder("AK 10- ");
        for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);

/////////////////////////////////
        /*tempSB = new StringBuilder("Outpt- ");
        for(int j = 0; j < 4; j++)
            tempSB.append(String.format("%1$02X%2$02X%3$02X%4$02X", outState[0][j], outState[1][j], outState[2][j], outState[3][j]));
        System.out.println(tempSB.toString());*/
/////////////////////////////////

        outBlock = outState;
    }
 }