/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *
 *  % java DumpBinary 0 < alice.txt
 *  1104064 bits
 *  % java TextCompressor - < alice.txt | java DumpBinary 0
 *  480760 bits
 *  = 43.54% compression ratio!
 ******************************************************************************/

/**
 *  The {@code TextCompressor} class provides static methods for compressing
 *  and expanding natural language through textfile input.
 *
 *  @author Zach Blick, YOUR NAME HERE
 */
public class TextCompressor {


    final static int BLOCK_LENGTH = 8;
    final static int NUM_CHARS = 75;
    final static int NUM_STORED_WORDS = (1 << BLOCK_LENGTH) - NUM_CHARS;

    private static void compress() {

        // TODO: Complete the compress() method
        String[] firstWords = new String[NUM_STORED_WORDS];

        for (int i = 0; i < NUM_STORED_WORDS; i++) {
            String word = "";
            while (true) {
                char nextChar = BinaryStdIn.readChar();
                if (nextChar == ' ') {
                    firstWords[i] = word;
                    break;
                }
                else {
                    word += nextChar;
                }
            }
        }



        BinaryStdOut.close();
    }

    private static void expand() {

        // TODO: Complete the expand() method

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
