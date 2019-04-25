import java.io.*;

public class Application
{
    /**
     *
     */
    private void run(String function, String file)
    {
        long time = System.currentTimeMillis();
        String plainText = "";
        String keyText = "";

        try(BufferedReader input = new BufferedReader(new FileReader(file)))
        {
            plainText = input.readLine();
            keyText = input.readLine();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }

        /*String plainText1 = swapLeftBit(plainText);
        String keyText1 = swapLeftBit(keyText);*/

        /*int[][] plainBlock = getBlockFromBinary(plainText);
        int[][] keyBlock = getBlockFromBinary(keyText);*/
        int[][] plainBlock = getBlockFromHex(plainText);
        int[][] keyBlock = getBlockFromHex(keyText);

/*
        for(int i = 0; i < 4; i++)
            System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", plainBlock[i][0], plainBlock[i][1], plainBlock[i][2], plainBlock[i][3]));

        System.out.println();

        for(int i = 0; i < 4; i++)
            System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", keyBlock[i][0], keyBlock[i][1], keyBlock[i][2], keyBlock[i][3]));

        System.out.println();*/

        switch(function.toLowerCase())
        {
            case("encrypt"):
            case("encode"):
                AES[] versions = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                /*AES[] comparisonP = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};
                AES[] comparisonK = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                int[][] plainBlock1 = getBlock(plainText1);

                int[][] keyBlock1 = getBlock(keyText1);*/

                /*Round test = new Round(keyBlock);

                int[][] key = Round.getKeySchedule();

                System.out.println();

                for(int i = 0; i < 44; i++)
                {
                    System.out.println(String.format("%1$02X, %2$02X, %3$02X, %4$02X", key[i][0], key[i][1], key[i][2], key[i][3]));
                }*/

                for(int i = 0; i < 5; i++)
                {
                    versions[i].encode(plainBlock, keyBlock);
                    /*comparisonP[i].encode(plainBlock1, keyBlock);
                    comparisonK[i].encode(plainBlock, keyBlock1);*/
                    for(int j = 0; j < 11; j++)
                    {
                        /*comparisonP[i].compareBits(j, versions[i].getRoundBlock(j), comparisonP[i].getRoundBlock(j));
                        comparisonK[i].compareBits(j, versions[i].getRoundBlock(j), comparisonK[i].getRoundBlock(j));*/
                    }
                }

                time = System.currentTimeMillis() - time;

                System.out.println("ENCRYPTION");

                System.out.println("Plaintext P: " + AES.blockToHex(plainBlock));
                System.out.println("Key K: " + AES.blockToHex(keyBlock));
                System.out.println("Ciphertext C: " + AES.blockToHex(versions[0].getOutBlock()));

                System.out.println("Running time: " + time + "ms");

                /*outputEncode("P", comparisonP);
                System.out.println();
                outputEncode("K", comparisonK);*/
                break;

            case("decrypt"):
            case("decode"):
                AES decrypt = new AES0();

                decrypt.decode(plainBlock, keyBlock);
                System.out.println("DECRYPTION");

                System.out.println("Ciphertext C: " + plainText);
                System.out.println("Key K: " + keyText);
                System.out.println("Plaintext P: " + decrypt.blockToBinary());
                break;
            default:
                System.out.println("USAGE: java Application [encrypt|encode|decrypt|decode] [filename]");
        }
        //System.out.println(String.format("%1$02X", Sbox.Sbox[0][0]));
    }

    /**
     *
     * @param inputText
     * @return
     */
    private String swapLeftBit(String inputText)
    {
        if(inputText.startsWith("1"))
        {
            return "0" + inputText.substring(1);
        }
        else
        {
            return "1" + inputText.substring(1);
        }
    }

    /**
     *
     * @param modified
     */
    private void outputEncode(String modified, AES[] version)
    {
        if(modified.equals("P"))
        {
            System.out.println("P and Pi under K");
        }
        else
        {
            System.out.println("P under K and Ki");
        }

        System.out.println(String.format("%1$-7s%2$6s%3$6s%4$6s%5$6s%6$6s", "Round", "AES0", "AES1", "AES2", "AES3", "AES4"));

        System.out.println(
                String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d",
                        0, version[0].getAvalanche()[0], version[1].getAvalanche()[0],
                        version[2].getAvalanche()[0], version[3].getAvalanche()[0],
                        version[4].getAvalanche()[0]));
        for(int i = 1; i < version[0].getAvalanche().length; i++)
        {
            System.out.println(
                    String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d",
                            i, version[0].getAvalanche()[i], version[1].getAvalanche()[i],
                            version[2].getAvalanche()[i], version[3].getAvalanche()[i],
                            version[4].getAvalanche()[i]));
        }

    }

    /**
     *
     * @param input
     * @return
     */
    private int[][] getBlockFromBinary(String input)
    {
        int[][] decimal = new int[4][4];
        for(int i = 0, j = 0; i < 4; i++, j+=32)
        {
                decimal[0][i] = Integer.parseInt(input.substring(j, j + 8), 2);
                decimal[1][i] = Integer.parseInt(input.substring(j + 8, j + 16), 2);
                decimal[2][i] = Integer.parseInt(input.substring(j + 16, j + 24), 2);
                decimal[3][i] = Integer.parseInt(input.substring(j + 24, j + 32), 2);
        }
        return decimal;
    }

    private int[][] getBlockFromHex(String input)
    {
        int[][] decimal = new int[4][4];
        for(int i = 0, j = 0; i < 4; i++, j+=8)
        {
            decimal[0][i] = Integer.parseInt(input.substring(j, j + 2), 16);
            decimal[1][i] = Integer.parseInt(input.substring(j + 2, j + 4), 16);
            decimal[2][i] = Integer.parseInt(input.substring(j + 4, j + 6), 16);
            decimal[3][i] = Integer.parseInt(input.substring(j + 6, j + 8), 16);
        }
        return decimal;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args)
    {
        Application AES = new Application();
        AES.run(args[0], args[1]);
    }
}
