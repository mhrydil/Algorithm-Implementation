import java.io.*;

import TriePackageMod.*;

/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

@SuppressWarnings("Duplicates")
public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static final int L = 65536;       // number of codewords = 2^W, 2^16
//    private static final int W = 12;         // codeword width

    public static void compress() throws IOException {
        int width = 9;
        int curr = 0;
        boolean done = false;
        BufferedInputStream input = new BufferedInputStream(System.in);
        TrieSTMod<Integer> st = new TrieSTMod<>();
        StringBuilder currString = new StringBuilder();

        for (int i = 0; i < R; i++) { //this loop initializes the symbolTable with all of the ASCII characters
            currString.setLength(0); //reset the StringBuilder to be empty
            currString.append((char) i); //adds the ASCII character associated with i to currString
            st.put(currString, i); //add the ASCII character to the symbol table
        }
        int code = R+1;  // R is codeword for EOF

        currString.setLength(0);
        while(!done) {
            if (currString.length() == 0) {
                curr = input.read();
                if (curr == -1) done = true;
                else currString.append((char) curr);
            }

            while (st.contains(currString) && !done) {
                curr = input.read();
                if (curr == -1) {
                    done = true;
                    BinaryStdOut.write(st.get(currString), width);
                    //st.put(currString, code++);
                }
                else {
                    currString.append((char) curr);
                }
            } //when this while loop ends, we have a string with a new character

            if (code < L && !done) { //Keep filling the table
                st.put(currString, code++); //add new string to dictionary and increment code
                currString.deleteCharAt(currString.length() - 1);
                BinaryStdOut.write(st.get(currString), width);
                if (code == Math.pow(2, width)) width++;
                currString.setLength(0);
                currString.append((char) curr);
            }

            if (code == L){ //if the table is full, finish printing the output with the symbol table as it was
                while (!done){

                    while(st.contains(currString) && !done){
                        curr = input.read();
                        if (curr == -1){
                            done = true;
                            if (currString.length()>0) BinaryStdOut.write(st.get(currString), width);
                        }
                        else{
                            currString.append((char) curr);
                        }
                    }

                    currString.deleteCharAt(currString.length()-1);
                    if(currString.length()>0) BinaryStdOut.write(st.get(currString), width);
                    currString.setLength(0);
                    currString.append((char)curr);
                }
            }

        }
        BinaryStdOut.write(R,width);
        BinaryStdOut.close();
    }


    public static void expand() {
        String[] st = new String[L];
        int width = 9;
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(width);
        String val = st[codeword];

        while (true) {
            if(width<17 && i == Math.pow(2, width)-1){
                width++;
            }
            BinaryStdOut.write(val);

            if(i == Math.pow(2, width)-1) codeword = BinaryStdIn.readInt(width-1);
            else codeword = BinaryStdIn.readInt(width);
            if (codeword == R) break;

            String s = st[codeword];
            if (i == codeword){ // special case hack
                s = val + val.charAt(0);
            }
            if (i < L){
                st[i] = val + s.charAt(0);
                i++;
            }
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) throws IOException {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
