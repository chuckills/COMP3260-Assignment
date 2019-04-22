import java.io.*;

public class Application
{
    /**
     *
     */
    private void run()
    {
        long time = System.currentTimeMillis();

        AES[] version = new AES[5];

        version[0] = new AES0();
        version[1] = new AES1();
        version[2] = new AES2();
        version[3] = new AES3();
        version[4] = new AES4();

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
        //System.out.println(String.format("%1$02X", Sbox.Sbox[0][0]));
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
        for(int i = 0; i < version[0].getAvalanche().length; i++)
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
    private int[][] getBlock(String input)
    {
        int[][] decimal = new int[4][4];
        int k = 0;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                decimal[i][j] = Integer.parseInt(input.substring(k * 8, k * 8 + 8), 2);
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
