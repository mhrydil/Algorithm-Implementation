import java.util.*;

public class Substitute implements SymCipher {

/*
Substitute: This class must implement SymCipher and meet the following specifications:

-   It will have two constructors, one without any parameters and one that takes a byte array.
    The parameterless constructor will create a random 256 byte array which is a permutation of the 256 possible byte
    values and will serve as a map from bytes to their substitution values. For example, if location 65 of the key array
    has the value 92, it means that byte value 65 will map into byte value 92. Note that you will also need an inverse
    mapping array for this cipher, which can be easily derived from the substitution array (so you only need to send the
    original substitution array to the server). Be careful with this class since byte values can be negative, but array
    indices cannot be negative – this issue can be resolved with some thought. The other constructor will use the byte
    array parameter as its key. The SecureChatClient will call the parameterless constructor and the SecureChatServer
    calls the version with a parameter.

-   To implement the encode() method, convert the String parameter to an array of bytes, then iterate through all
    of the bytes, substituting the appropriate bytes from the key. Again, be careful with negative byte values.

-   To decode, simply reverse the substitution (using your decode byte array) and convert the
    resulting bytes back to a String.
 */

    private static byte[] key;

    public Substitute(){
        Random r = new Random();
        key = new byte[256];
        for (int i=0; i<256; i++){
            key[i] = (byte) i; //essentially, key[i] = i - 128
        }
        for(int i=0; i<256; i++){
            int randomPos = r.nextInt(Integer.MAX_VALUE) % 256; // randomPos is a random integer from 0-255
            swap(key, i, randomPos); //swap the value at position i with the value at the random position
        }

    }

    public Substitute(byte[] byteKey){
        key = byteKey;

    }
    public byte[] getKey(){
        return key;
    }

    public byte[] encode(String s){
        byte[] sArray = s.getBytes();
        for(int i=0; i<sArray.length; i++){
            sArray[i] = key [(int) sArray[i]];
        }
        return sArray;

    }

    public String decode(byte[] bytes){
        byte[] decodedKey = new byte[256];
        // key maps elements from index to a random value (eg. A = 65 = random value, so key[65] = someRandomValue
        // decoded key maps elements from index(someRandomValue) to a byte (65) which corresponds to a character (A)
        // since indices can't be negative, the byte value has 128 added to it. So, if the encoded key has the code
        // -114, the value that corresponds with that code is at index 14 (-114 + 128) of the decodedKey array.
        for (int i=0; i < decodedKey.length; i++){
            for (int j=0; j < key.length; j++){
                if(key[j] + 128 == i) decodedKey[i] = (byte) j; //this is the inverse of the key
            }
        }
        for(int i=0; i<bytes.length; i++){
            bytes[i] = decodedKey[bytes[i]+128];

        }
        return new String(bytes);
    }

    private void swap(byte[] b, int first, int second){
        byte temp = b[first];
        b[first] = b[second];
        b[second] = temp;
    }

    public static void main(String[] args){ //for testing purposes
        Substitute sub = new Substitute();
        for(int i=0; i<key.length; i++){
            System.out.print(i + ": " + key[i] + " ");
        }
        System.out.println();

        String testString = "The quick brown fox jumps over the lazy dog! @#$@#%(&)(*&";
        System.out.println("Test String: " + testString);
        byte[] encodedS = sub.encode(testString);
        System.out.println("Encoded bytes: ");
        for(int i=0; i<encodedS.length; i++){
            System.out.print(testString.charAt(i) + ": " + encodedS[i] + " ");
        }
        System.out.println();
        System.out.println("Decoded String: " + sub.decode(encodedS));
//        Arrays.sort(key);
//        for(int i=0; i<key.length; i++){
//            System.out.print(key[i] + " ");
//        }
    }
}
