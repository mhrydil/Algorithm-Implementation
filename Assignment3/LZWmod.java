import java.io.*;


/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - r < input.txt > outputCompressed.lzw  (compress with dictionary reset)
 *                                         ^^^^^^^^^^^^^^^^^^^^^^ Redirects output to file
 *  Execution:    java LZW - n < input.txt > outputCompressed.lzw (compress without dictionary reset)
 *                                         ^^^^^^^^^^^^^^^^^^^^^^ Redirects output to file
 *  Execution:    java LZW + < input.txt  (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 * Matt Hrydil - CS1501 Summer 2019 - Assignment 3
 *************************************************************************/

@SuppressWarnings("Duplicates")
public class LZWmod {
    private static final int R = 256;        // number of input chars
    private static final int MIN_WIDTH = 9;
    private static final int MAX_WIDTH = 16;

    public static void compress() throws IOException {
        BinaryStdOut.write(false); // writes a single bit at the beginning of the file (false = no reset dictionary)
        int width = MIN_WIDTH;
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
            if (currString.length() == 0) { //the first time through this loop, add a character to the currString and continue
                curr = input.read();
                if (curr == -1) done = true;
                else currString.append((char) curr);
            }

            while (st.contains(currString) && !done) { //add characters to currString until currString is not in the symbol table
                curr = input.read();
                if (curr == -1) {
                    done = true;
                    BinaryStdOut.write(st.get(currString), width);
                    //st.put(currString, code++);
                }
                else {
                    currString.append((char) curr); //add the next character to currString
                }
            } //when this while loop ends, we have a string with a new character

            if (code < Math.pow(2, MAX_WIDTH) && !done) { //Keep filling the table
                if (code == Math.pow(2, width)) width++; //if code is a power of 2, we need to start saving the codes with the next bit width
                st.put(currString, code++); //add new string to dictionary and increment code(for next time through)
                currString.deleteCharAt(currString.length() - 1);   //delete the last character from currString before writing to output
                                                                    // otherwise, st.get(currString) wouldn't work
                BinaryStdOut.write(st.get(currString), width);
                currString.setLength(0);
                currString.append((char) curr); //start the next string with the last character of the string we just added to our ST
            }

            else if (code == Math.pow(2, MAX_WIDTH)){ //if the table is full, finish printing the output with the symbol table as it was
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

                    if (!done) {
                        currString.deleteCharAt(currString.length() - 1);
                        if (currString.length() > 0) BinaryStdOut.write(st.get(currString), width);
                        currString.setLength(0);
                        currString.append((char) curr);
                    }
                }
            }
        }
        BinaryStdOut.write(R,width);
        BinaryStdOut.close();
    }

    public static void compressReset() throws IOException {
        BinaryStdOut.write(true); //writes a single bit at the beginning of the file (true = reset dictionary)
        int width = MIN_WIDTH;
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
            if (currString.length() == 0) { //the first time through this loop, add a character to the currString and continue
                curr = input.read();
                if (curr == -1) done = true;
                else currString.append((char) curr);
            }

            while (st.contains(currString) && !done) { //add characters to currString until currString is not in the symbol table
                curr = input.read();
                if (curr == -1) {
                    done = true;
                    BinaryStdOut.write(st.get(currString), width);
                }
                else {
                    currString.append((char) curr);
                }
            } //when this while loop ends, we have a string with a new character

            if (code < Math.pow(2, MAX_WIDTH) && !done) { //Keep filling the table
                if (code == Math.pow(2, width)) width++;
                st.put(currString, code++); //add new string to dictionary and increment code
                currString.deleteCharAt(currString.length() - 1);
                BinaryStdOut.write(st.get(currString), width);
                currString.setLength(0);
                currString.append((char) curr);
            }

            else if (code == Math.pow(2, MAX_WIDTH)){ //if the table is full, clear the symbol table, refill it with ASCII, loop back up
                currString.deleteCharAt(currString.length()-1);
                BinaryStdOut.write(st.get(currString), width);
                st.clear();
                for (int i = 0; i < R; i++) { //this loop initializes the symbolTable with all of the ASCII characters
                    currString.setLength(0); //reset the StringBuilder to be empty
                    currString.append((char) i); //adds the ASCII character associated with i to currString
                    st.put(currString, i); //add the ASCII character to the symbol table
                }
                currString.setLength(0);
                currString.append((char)curr);
                code = R+1;
                width = MIN_WIDTH;

            }
        }
        BinaryStdOut.write(R,width);
        BinaryStdOut.close();
    }



    public static void expand() {
        boolean initial = BinaryStdIn.readBoolean();    //reads the first bit of the compressed file (if 1, the compression algorithm
                                                        //used dictionary reset)
        if (initial) expandWithReset();
        else expandWithoutReset();
    }

    private static void expandWithoutReset(){
        String[] st = new String[(int)Math.pow(2, MAX_WIDTH)];
        boolean full = false;
        int width = 9;
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(width);
        String val = st[codeword];

        while (!full) {
            if(width<MAX_WIDTH && i == Math.pow(2, width)-1){ //max width of the codewords should be 16 bits
                width++;
            }
            BinaryStdOut.write(val);

            if(i == Math.pow(2, width)-1 && width != MAX_WIDTH) codeword = BinaryStdIn.readInt(width-1);
            else codeword = BinaryStdIn.readInt(width);
            if (codeword == R) break;

            String s = st[codeword];
            if (i == codeword){ // special case hack
                s = val + val.charAt(0);
            }
            if (i < Math.pow(2, MAX_WIDTH)){
                st[i] = val + s.charAt(0);
                i++;
                if (i == Math.pow(2, MAX_WIDTH)) full = true;
            }
            val = s;
        }

        while(full){ //in this loop, the table is full
            BinaryStdOut.write(val);

            codeword = BinaryStdIn.readInt(width);
            if(codeword == R) break; // R is codeword for EOF

            String s = st[codeword];
            val = s;
        }

        BinaryStdOut.close();
    }

    private static void expandWithReset(){
        String[] st = new String[(int)Math.pow(2, MAX_WIDTH)];
        boolean full = false;
        int width = 9;
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(width);
        String val = st[codeword];

        while (!full) {
            if(width<16 && i == Math.pow(2, width)-1){ //max width of the codewords should be 16 bits
                width++;
            }
            BinaryStdOut.write(val);

            if(i == Math.pow(2, width)-1 && width != 16) codeword = BinaryStdIn.readInt(width-1);
            else codeword = BinaryStdIn.readInt(width);
            if (codeword == R) break;

            String s = st[codeword];
            if (i == codeword){ // special case hack
                s = val + val.charAt(0);
            }
            if (i < Math.pow(2, MAX_WIDTH)){
                st[i] = val + s.charAt(0);
                i++;
                if (i == Math.pow(2, MAX_WIDTH)) full = true;
            }
            val = s;

            if(full){
                BinaryStdOut.write(val);
                width = 9;
                codeword = BinaryStdIn.readInt(width);
                if (codeword == R) break;
                for(int j = 257; j < Math.pow(2, MAX_WIDTH); j++){
                    st[j] = null;
                }
                val = st[codeword];
                i = 257;
                full = false;
            }
        }

        BinaryStdOut.close();


    }



    public static void main(String[] args) throws IOException {
        if (args[0].equals("-")){
            if (args[1].equals("r")){
                compressReset();
            }
            else if(args[1].equals("n")){
                compress();
            }
        }
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }

}
