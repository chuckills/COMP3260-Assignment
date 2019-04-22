public class AES2 extends AES
{
    public AES2()
    {
        avalanche = new int[10];
    }

    public int[][] encode(int[][] plain)
    {
        Round round = new Round();

        int[][] cipher = round.addKey(plain);

        for(int i = 0; i < 9; i++)
        {
            plain = cipher;
            cipher = round.subBytes(cipher);

            cipher = round.mixCols(cipher);
            cipher = round.addKey(cipher);
            compareBits(i, plain, cipher);
        }

        plain = cipher;
        cipher = round.subBytes(cipher);

        cipher = round.addKey(cipher);

        compareBits(10, plain, cipher);

        return cipher;
    }

    public int[][] decode(int[][] cipher)
    {
        Round round = new Round(true);

        int[][] plain = round.addKey(cipher);

        for(int i = 0; i < 9; i++)
        {

            plain = round.subBytes(plain);
            plain = round.addKey(plain);
            plain = round.mixCols(plain);
        }


        plain = round.subBytes(plain);
        plain = round.addKey(plain);

        return plain;
    }
}
