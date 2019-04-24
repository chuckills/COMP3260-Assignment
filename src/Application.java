import java.io.*;
import java.util.Arrays;

public class Application
{
    /**
     *
     */
    private void run()
    {
        long time = System.currentTimeMillis();

        AES[] version = {new AES0(), new AES1(), new AES2(), new AES3(), new AES4()};

        String inputText = "";
        String keyText = "";

        try(BufferedReader input = new BufferedReader(new FileReader("input.txt")))
        {
            inputText = input.readLine();
            keyText = input.readLine();
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }

        int[][] inputBlock = getBlock(inputText);
        int[][] keyBlock = getBlock(keyText);

        version[0].compareBits(0, inputBlock, keyBlock);

        time = System.currentTimeMillis() - time;

        System.out.println("ENCRYPTION");

        System.out.println("Plaintext P: " + inputText);
        System.out.println("Key K: " + keyText);
        System.out.println("Ciphertext C: " + "");

        System.out.println("Running time: " + time + "ms");
        outputResult("P", version);
        outputResult("K", version);

        /*System.out.println(Arrays.toString(inputBlock[0]));
        System.out.println(Arrays.toString(inputBlock[1]));
        System.out.println(Arrays.toString(inputBlock[2]));
        System.out.println(Arrays.toString(inputBlock[3]));

        System.out.println();
*/
        /*Round test = new Round(keyBlock);

        int[][] testBlock = {{0x87, 0xF2, 0x4D, 0x97},
                {0x6E, 0x4C, 0x90, 0xEC},
                {0x46, 0xE7, 0x4A, 0xC3},
                {0xA6, 0x8C, 0xD8, 0x95}};*/


        //System.out.println(String.format("%1$02X", Sbox.Sbox[0][0]));
        Round test = new Round(keyBlock);
        System.out.println();
        for(int i = 0; i < Round.getKeySchedule().length; i++)
            System.out.println(Arrays.toString(Round.getKeySchedule()[i]));
    }

    /**
     *
     * @param modified
     */
    private void outputResult(String modified, AES[] version)
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
                        0, version[0].getAvalanche()[9], version[1].getAvalanche()[9],
                        version[2].getAvalanche()[9], version[3].getAvalanche()[9],
                        version[4].getAvalanche()[9]));
        for(int i = 0; i < version[0].getAvalanche().length; i++)
        {
            System.out.println(
                    String.format("%1$-7d%2$6d%3$6d%4$6d%5$6d%6$6d",
                            i+1, version[0].getAvalanche()[i], version[1].getAvalanche()[i],
                            version[2].getAvalanche()[i], version[3].getAvalanche()[i],
                            version[4].getAvalanche()[i]));
        }

    }

    /**
     *
     * @param input
     * @return
     */
    private int[][] getBlock(String input)
    {
        int[][] decimal = new int[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                decimal[j][i] = Integer.parseInt(input.substring(k * 8, k * 8 + 8), 2);
                k++;
                //bytes[i] = String.format("%1$02X", decimal);
            }
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
        AES.run();
    }
}
