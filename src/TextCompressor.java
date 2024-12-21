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
    private static final int BLOCK_LEN = 12;
    private static final int MAX_CODES = 1 << BLOCK_LEN;

    private static void compress() {
        // Read in all 256 codes from ASCII
        TST tree = new TST();
        for (int i = 0; i < EOF; i++) {
            tree.insert("" + (char) i, i);
        }

        String text = BinaryStdIn.readString();

        int index = 0;
        int length = text.length();
        int codeIndex = EOF + 1;
        while (index < length) {
            String prefix = tree.getLongestPrefix(text, index);
            BinaryStdOut.write(tree.lookup(prefix), BLOCK_LEN);
            int len = prefix.length();
            if (index + len < length && codeIndex < MAX_CODES) {
                char lookahead = text.charAt(index + len);
                prefix += lookahead;
                tree.insert(prefix, codeIndex++);
            }
            index += len;
        }

        BinaryStdOut.write(EOF, BLOCK_LEN);


        BinaryStdOut.close();
    }

    private static void expand() {

        // Read in all 256 codes from ASCII
        String[] prefixes = new String[MAX_CODES];
        for (int i = 0; i < EOF; i++) {
            prefixes[i] = "" + (char) i;
        }

        int codeIndex = EOF + 1;
        String lastPrefix = prefixes[BinaryStdIn.readInt(BLOCK_LEN)];
        BinaryStdOut.write(lastPrefix);
        while (!BinaryStdIn.isEmpty()) {
            int nextCode = BinaryStdIn.readInt(BLOCK_LEN);
            if (nextCode == EOF) break;
            String nextPrefix = "";
            if (nextCode < codeIndex) {
                nextPrefix = prefixes[nextCode];
            }
            else if (nextCode == codeIndex) {
                nextPrefix = lastPrefix + lastPrefix.charAt(0);
            }
            else {
                // Should never reach here, only here for testing
                for (int i = 0; i < 1000; i++) BinaryStdOut.write(false);
            }
            if (codeIndex < MAX_CODES) {
                prefixes[codeIndex++] = lastPrefix + nextPrefix.charAt(0);
            }
            lastPrefix = nextPrefix;
            BinaryStdOut.write(lastPrefix);
        }

        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
