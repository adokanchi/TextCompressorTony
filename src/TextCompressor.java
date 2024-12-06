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
 *  @author Zach Blick, Tony Dokanchi
 */
public class TextCompressor {


    final static int BLOCK_LENGTH = 8;
    final static int NUM_CHARS = 75;
    final static int NUM_STORED_WORDS = (1 << BLOCK_LENGTH) - NUM_CHARS;
    static String[] firstWords;

    private static void compress() {

        firstWords = new String[NUM_STORED_WORDS];

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

        int longestLen = 0;
        for (int i = 0; i < NUM_STORED_WORDS; i++) if (firstWords[i].length() > longestLen) longestLen = firstWords[i].length();

        // Fill in with longestLen chars from BinaryStdIn so we can check is any of the stored words are in the text.
        String in = "";
        while (in.length() < longestLen) {
            in += BinaryStdIn.readChar();
        }

        while (!in.isEmpty()) {
            boolean found = false;
            for (String word : firstWords) {
                if (in.substring(0, word.length()).equals(word)) {
                    BinaryStdOut.write(wordToCode(word));
                    found = true;
                }
            }
            if (!found) {
                BinaryStdOut.write(charToCode(in.charAt(0)));
                in = in.substring(1);
            }

            if (!BinaryStdIn.isEmpty()) {
                while (in.length() < longestLen) {
                    in += BinaryStdIn.readChar();
                }
            }
        }



        BinaryStdOut.close();
    }

    // Takes in a word from firstWords and outputs the code
    private static int wordToCode(String word) {
        for (int i = 0; i < NUM_STORED_WORDS; i++) {
            if (word.equals(firstWords[i])) {
                return i + NUM_CHARS;
            }
        }
        return -1;
    }

    private static int charToCode(char c) {
        // TODO: Add the map
        return -1;
    }

    private static String codeToWord(int code) {
        return firstWords[code - NUM_CHARS];
    }

    private static String codeToChar(int code) {
        // TODO: Add the map
        return null;
    }

    private static void expand() {

        while (!BinaryStdIn.isEmpty()) {
            int code = BinaryStdIn.readInt(BLOCK_LENGTH);
            if (code < NUM_CHARS) {
                BinaryStdOut.write(codeToChar(code));
            }
            else {
                BinaryStdOut.write(codeToWord(code));
            }
        }
        // TODO: Complete the expand() method



        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
