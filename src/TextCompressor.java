/******************************************************************************
 *  Compilation:  javac TextCompressor.java
 *  Execution:    java TextCompressor - < input.txt   (compress)
 *  Execution:    java TextCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   abra.txt
 *                jabberwocky.txt
 *                shakespeare.txt
 *                virus.txt
 * *
 *  % java DumpBinary 0 < abra.txt
 *  136 bits
 *  *
 *  % java TextCompressor - < abra.txt | java DumpBinary 0
 *  104 bits    (when using 8-bit codes)
 *  *
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
    private static final int EOF = 256;
    private static final int CODE_LENGTH = 12;
    private static final int MAX_NUM_CODES = 1 << CODE_LENGTH - 1;

    private static void compress() {

        TST tree = new TST();
        // Add all codes from extended ASCII
        for (int i = 0; i < 255; i++) {
            tree.insert("" + (char) i, i);
        }
        int code = 257;

        String text = BinaryStdIn.readString();

        int index = 0;
        int len = text.length();
        while (index < len) {
            String prefix = tree.getLongestPrefix(text.substring(index));
            BinaryStdOut.write(tree.lookup(prefix), CODE_LENGTH);
            int nextIndex = index + prefix.length();
            if (nextIndex < text.length()) {
                tree.insert(prefix + text.charAt(nextIndex), code++);
            }
            index = nextIndex;
        }

        BinaryStdOut.write(EOF, CODE_LENGTH);
        BinaryStdOut.close();
    }

    private static void expand() {
        String text = BinaryStdIn.readString();
        System.out.println(text);

        /*
        String[] map = new String[MAX_NUM_CODES];

        for (int i = 0; i < 255; i++) {
            map[i] = "" + (char) i;
        }

        int code = 257;

        String text = BinaryStdIn.readString();
        int index = 0;
        while (true) {
            int currCode = Integer.parseInt(text.substring(index, index + CODE_LENGTH));
            String currString = map[currCode];

            BinaryStdOut.write(currString, CODE_LENGTH);

            int nextCode = Integer.parseInt(text.substring(index + CODE_LENGTH, index + 2 * CODE_LENGTH));
            if (nextCode == EOF) {
                break;
            }
            String nextString = map[nextCode];

            map[code] = currString + nextString.charAt(0);
            code++;
        }

        */
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
