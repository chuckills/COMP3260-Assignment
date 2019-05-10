import java.io.*;

/*
======================================================
COMP3260 Assignment 2
Gregory Choice(c9311718) & Christopher Booth(c3229932)
======================================================
*/

/** Application.java
 *
 * This is the main runnable class for COMP3260 Assignment 2
 *
 */
public class Application
{
    /** run()
     *
     * This method controls the program flow.
     *
     * @param file - String, provides the file name to load and the prefix for the output file
     * @param function - String, directs the program to encrypt or decrypt (--[encrypt|encode|decrypt|decode])
     */
    private void run(String file, String function)
    {
        // Initialise current time
        long time = System.currentTimeMillis();

        // Initialise input strings
        String plainText = "";
        String keyText = "";
        String[] plainTextI = new String[128];
        String[] keyTextI = new String[128];

        // Get input from file
        try(BufferedReader input = new BufferedReader(new FileReader(file)))
        {
            plainText = input.readLine();
            keyText = input.readLine();

            if(plainText.length() % 32 != 0 || keyText.length() % 32 != 0)
            {
                throw new NumberFormatException("Input length must be multiple of 32");
            }
        }
        catch(IOException|NumberFormatException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        // Create comparison input strings
        try
        {
            plainTextI = swapBits(plainText);
            keyTextI = swapBits(keyText);
        }
        catch(NumberFormatException NFe)
        {
            System.out.println(NFe.getMessage());
            System.exit(1);
        }

        // Parse input strings to integer arrays
        int[][] plainBlock = getBlockFromBinary(plainText);
        int[][] keyBlock = getBlockFromBinary(keyText);

        // Open an output file and select to encode or decode
        try(PrintWriter out = new PrintWriter(file.substring(0, file.lastIndexOf('.')) + "_output.txt"))
        {
            StringBuilder outText = new StringBuilder();

            // Choose to encode or decode
            switch(function.toLowerCase())
            {
                case ("--encrypt"):
                case ("--encode"):
                    // Initialise an array of the five AES versions based on P and K
                    AES[] versions = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                    // Initialise an array of versions based on P1 and K
                    AES[] comparisonP = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                    // Initialise an array of versions based on P and K1
                    AES[] comparisonK = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

                    // Parse comparison strings to integer arrays
                    //int[][] plainBlockI = getBlockFromBinary(plainText1);
                    //int[][] keyBlockI = getBlockFromBinary(keyText1);

                    // Encode the input for all input strings and keys
                    for(int i = 0; i < 5; i++)
                    {
                        // Run the encryption algorithm using the three plaintext/key combinations
                        versions[i].encode(plainBlock, keyBlock);
                        for(int j = 0; j < 128; j++)
                        {
                            comparisonP[i].encode(getBlockFromBinary(plainTextI[j]), keyBlock);
                            comparisonK[i].encode(plainBlock, getBlockFromBinary(keyTextI[j]));

                            // Do the comparison with the alternate inputs
                            for(int k = 0; k < 11; k++)
                            {
                                comparisonP[i].compareBits(k, versions[i].getRoundBlock(k), comparisonP[i].getRoundBlock(k));
                                comparisonK[i].compareBits(k, versions[i].getRoundBlock(k), comparisonK[i].getRoundBlock(k));
                            }
                        }
                    }

                    // Calculate the running time
                    time = System.currentTimeMillis() - time;

                    // Generate the output for screen and file
                    outText.append("ENCRYPTION\n");
                    outText.append(String.format("Plaintext P: %1s", plainText));
                    outText.append(String.format("\nKey K: %1$s", keyText));
                    outText.append(String.format("\nCiphertext C: %1$s", AES.blockToBinary(versions[0].getOutBlock())));
                    outText.append(String.format("\nRunning time: %1$sms\n", time));
                    outText.append("Avalanche: \n");

                    // Get the avalanche report table for P1
                    outText.append(outputEncode('P', comparisonP));
                    outText.append("\n");

                    // Get the avalanche report table for K1
                    outText.append(outputEncode('K', comparisonK));
                    break;

                case ("--decrypt"):
                case ("--decode"):
                    AES decrypt = new AES0();

                    // Run the decryption algorithm
                    decrypt.decode(plainBlock, keyBlock);

                    // Generate the output for screen and file
                    outText.append("DECRYPTION\n");
                    outText.append(String.format("Ciphertext C: %1$s\n", plainText));
                    outText.append(String.format("Key K: %1$s\n", keyText));
                    outText.append(String.format("Plaintext P: %1$s\n", AES.blockToBinary(decrypt.getOutBlock())));
                    break;
                default:
                    // Incorrect command line parameters
                    System.out.println("USAGE (From compiled classes): java Application [filename] --[encrypt|encode|decrypt|decode]");
                    System.exit(1);
            }

            // Send the output to file and screen
            out.println(outText.toString());
            System.out.println(outText.toString());
            System.out.println();
            System.out.println("File output to " + file.substring(0, file.lastIndexOf('.')) + "_output.txt");
        }
        catch(FileNotFoundException FNFe)
        {
            System.out.println(FNFe.getMessage());
            System.exit(1);
        }
    }

    /** swapLeftBit()
     *
     * Toggles the left most bit of a binary string.
     *
     * @param inputText - String, A string of binary digits
     * @return - String, returns a binary string with the leftmost bit toggled
     */
    private String[] swapBits(String inputText) throws NumberFormatException
    {
        String[] bitArray = new String[128];

        for(int i = 0; i < 128; i++)
        {
            // Choose the value of the toggle
            if(inputText.charAt(i) == '1')
            {
                bitArray[i] = inputText.substring(0,i) + "0" + inputText.substring(i+1);
            }
            else if (inputText.charAt(i) == '0')
            {
                bitArray[i] = inputText.substring(0,i) + "1" + inputText.substring(i+1);
            }
            else
                throw new NumberFormatException("Input string not binary");
        }
        return bitArray;
    }

    /** outputEncode()
     *
     * Formats the output of the caomparison table for the encoding algorithms
     *
     * @param modified - char, either 'P' or 'K' denoting whether plaintext or key is modified
     * @param version - AES[], the array of AES encoding algorithms
     */
    private String outputEncode(char modified, AES[] version)
    {
        StringBuilder sb = new StringBuilder();

        // Choose the relevant table title
        if(modified == 'P')
        {
            sb.append("P and Pi under K\n");
        }
        else
        {
            sb.append("P under K and Ki\n");
        }

        // Format the column headers
        sb.append(String.format("%1$-7s%2$6s%3$6s%4$6s%5$6s%6$6s\n", "Round", "AES0", "AES1", "AES2", "AES3", "AES4"));

        // Format the initial row before encoding. Divide by 128 for the average.
        sb.append(String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d\n",
                        0, version[0].getAvalanche()[0]/128, version[1].getAvalanche()[0]/128,
                        version[2].getAvalanche()[0]/128, version[3].getAvalanche()[0]/128,
                        version[4].getAvalanche()[0]/128));

        // Format the comparison rows. Divide by 128 for the average.
        for(int i = 1; i < version[0].getAvalanche().length; i++)
        {
            sb.append(String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d\n",
                            i, version[0].getAvalanche()[i]/128, version[1].getAvalanche()[i]/128,
                            version[2].getAvalanche()[i]/128, version[3].getAvalanche()[i]/128,
                            version[4].getAvalanche()[i]/128));
        }
        return sb.toString();
    }

    /** getBlockFromBinary()
     *
     * Converts a binary string into a 4x4 integer array
     *
     * @param input - String, 128 bit binary string
     * @return - int[][], the converted binary string as a block of integers
     */
    private int[][] getBlockFromBinary(String input)
    {
        // Initialise a 4x4 block array
        int[][] decimal = new int[4][4];

        // Fill the array as int with the binary input string
        for(int i = 0, j = 0; i < 4; i++, j+=32)
        {
            try
            {
                decimal[0][i] = Integer.parseInt(input.substring(j, j + 8), 2);
                decimal[1][i] = Integer.parseInt(input.substring(j + 8, j + 16), 2);
                decimal[2][i] = Integer.parseInt(input.substring(j + 16, j + 24), 2);
                decimal[3][i] = Integer.parseInt(input.substring(j + 24, j + 32), 2);
            }
            catch(NumberFormatException NFe)
            {
                System.out.println("Input string not binary");
                System.exit(1);
            }
        }
        return decimal;
    }

    /** getBlockFromHex()
     *
     * This method was used during testing to take a hexadecimal input.
     * It is no longer needed for this implementation.
     *
     * @param input - String, 128 bit hexadecimal string
     * @return - int[][], the converted binary string as a block of integers
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

    /** main()
     *
     * The main method for execution
     *
     * @param args - String[], the filename followed by the processing option
     */
    public static void main(String[] args)
    {
        Application AES = new Application();
        AES.run(args[0], args[1]);
    }
}
