import java.io.*;

/**
 *
 */
public class Application
{
    /**
     *
     * @param file
     * @param function
     */
    private void run(String file, String function)
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

        String plainText1 = swapLeftBit(plainText);
        String keyText1 = swapLeftBit(keyText);

        int[][] plainBlock = getBlockFromBinary(plainText);
        int[][] keyBlock = getBlockFromBinary(keyText);

        try(PrintWriter out = new PrintWriter("AESoutput.txt"))
        {
            StringBuilder outText = new StringBuilder();

            switch(function.toLowerCase())
            {
                case ("--encrypt"):
                case ("--encode"):
                    AES[] versions = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                    AES[] comparisonP = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};
                    AES[] comparisonK = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                    int[][] plainBlock1 = getBlockFromBinary(plainText1);
                    int[][] keyBlock1 = getBlockFromBinary(keyText1);

                    for(int i = 0; i < 5; i++)
                    {
                        versions[i].encode(plainBlock, keyBlock);
                        comparisonP[i].encode(plainBlock1, keyBlock);
                        comparisonK[i].encode(plainBlock, keyBlock1);
                        for(int j = 0; j < 11; j++)
                        {
                            comparisonP[i].compareBits(j, versions[i].getRoundBlock(j), comparisonP[i].getRoundBlock(j));
                            comparisonK[i].compareBits(j, versions[i].getRoundBlock(j), comparisonK[i].getRoundBlock(j));
                        }
                    }

                    time = System.currentTimeMillis() - time;



                    outText.append("ENCRYPTION\n");
                    outText.append(String.format("Plaintext P: %1s", plainText));
                    outText.append(String.format("\nKey K: %1$s", keyText));
                    outText.append(String.format("\nCiphertext C: %1$s", AES.blockToBinary(versions[0].getOutBlock())));
                    outText.append(String.format("\nRunning time: %1$sms\n", time));
                    outText.append("Avalanche: \n");

                    outText.append(outputEncode("P", comparisonP));
                    outText.append("\n");
                    outText.append(outputEncode("K", comparisonK));
                    break;

                case ("--decrypt"):
                case ("--decode"):
                    AES decrypt = new AES0();

                    decrypt.decode(plainBlock, keyBlock);

                    outText.append("DECRYPTION\n");
                    outText.append(String.format("Ciphertext C: %1$s\n", plainText));
                    outText.append(String.format("Key K: %1$s\n", keyText));
                    outText.append(String.format("Plaintext P: %1$s\n", AES.blockToBinary(decrypt.getOutBlock())));
                    break;
                default:
                    outText.append("USAGE: java Application [filename] --[encrypt|encode|decrypt|decode]");
            }

            out.println(outText.toString());
            System.out.println(outText.toString());
        }
        catch(FileNotFoundException FNFe)
        {
            System.out.println(FNFe.getMessage());
        }
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
     * @param version
     */
    private String outputEncode(String modified, AES[] version)
    {
        StringBuilder sb = new StringBuilder();

        if(modified.equals("P"))
        {
            sb.append("P and Pi under K\n");
        }
        else
        {
            sb.append("P under K and Ki\n");
        }


        sb.append(String.format("%1$-7s%2$6s%3$6s%4$6s%5$6s%6$6s\n", "Round", "AES0", "AES1", "AES2", "AES3", "AES4"));

        sb.append(
                String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d\n",
                        0, version[0].getAvalanche()[0], version[1].getAvalanche()[0],
                        version[2].getAvalanche()[0], version[3].getAvalanche()[0],
                        version[4].getAvalanche()[0]));

        for(int i = 1; i < version[0].getAvalanche().length; i++)
        {
            sb.append(
                    String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d\n",
                            i, version[0].getAvalanche()[i], version[1].getAvalanche()[i],
                            version[2].getAvalanche()[i], version[3].getAvalanche()[i],
                            version[4].getAvalanche()[i]));
        }
        return sb.toString();
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

    /**
     *
     * @param input
     * @return
     */
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
