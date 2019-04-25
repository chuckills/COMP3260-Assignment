public class AES0 extends AES
{
    public AES0()
    {
        super();
    }

    public void encode(int[][] inState, int[][] keyBlock)
    {
        Round round = new Round(keyBlock);
        for(int j = 0; j < 4; j++)
            System.out.println(String.format("Start- %1$02X, %2$02X, %3$02X, %4$02X", inState[j][0], inState[j][1], inState[j][2], inState[j][3]));

        System.arraycopy(inState, 0, roundBlocks[0], 0, inState.length);

        int[][] outState = round.addKey(inState, 1);

        for(int j = 0; j < 4; j++)
            System.out.println(String.format("AK  0- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));

        for(int i = 1; i < 10; i++)
        {
            outState = round.subBytes(outState);
            for(int j = 0; j < 4; j++)
                System.out.println(String.format("SB%5$3s- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3], i));
            outState = round.shiftRows(outState);
            for(int j = 0; j < 4; j++)
                System.out.println(String.format("SR%5$3s- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3], i));
            outState = round.mixCols(outState);
            for(int j = 0; j < 4; j++)
                System.out.println(String.format("MC%5$3s- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3], i));
            outState = round.addKey(outState, i + 1);
            for(int j = 0; j < 4; j++)
                System.out.println(String.format("AK%5$3s- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3], i));

            System.arraycopy(outState, 0, roundBlocks[i], 0, outState.length);
        }

        outState = round.subBytes(outState);
        for(int j = 0; j < 4; j++)
            System.out.println(String.format("SB 10- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));
        outState = round.shiftRows(outState);
        for(int j = 0; j < 4; j++)
            System.out.println(String.format("SR 10- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));
        outState = round.addKey(outState, 11);
        for(int j = 0; j < 4; j++)
            System.out.println(String.format("AK 10- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));

        System.arraycopy(outState, 0, roundBlocks[10], 0, outState.length);
        for(int j = 0; j < 4; j++)
            System.out.println(String.format("Outpt- %1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));

        outBlock = outState;
    }
 }
/*
        System.out.println();

        for(int i = 0; i < 4; i++)
            System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", outState[i][0], outState[i][1], outState[i][2], outState[i][3]));
*/

            /*
            System.out.println();



            if(i+1==10)
            {
                System.out.println();

                for(int j = 0; j < 4; j++)
                    System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));
            }

            for(int j = 0; j < 4; j++)
            System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", outState[j][0], outState[j][1], outState[j][2], outState[j][3]));




            */
